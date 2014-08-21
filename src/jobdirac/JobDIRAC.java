/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jobdirac;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author dida
 */
public class JobDIRAC {

    /**
     * @param args the command line arguments
     */
    final String gridDir = "/biomed/user/l/louacheni";
    final String diracDir = "/home/dida/DIRAC/scripts";
    final String filesDir = "/home/dida/DIRAC";
    int j;

    //function to open jdl directory
    public static ArrayList<String> jdlFileJob(String jdlDir) {
        String filesDir = "/home/dida/DIRAC";
        String diracDir = "/home/dida/DIRAC/scripts";
        ArrayList<String> jobID = new ArrayList<String>();
        ArrayList<File> listFile = new ArrayList<File>();
        File files = new File(jdlDir);
        for (File file : files.listFiles()) {
            if (file.isFile()) {
                listFile.add(file);
                System.out.println("File name " + file.getName());
            }
        }
        return jobID;
    }

    //function to count files within a directory
    public static int countFile(String jdlDir) {
        int count = 0;
        File files = new File(jdlDir);
        for (File f : files.listFiles()) {
            if (f.isDirectory()) {
                System.out.println("This is a directory : " + f);
                countFile(f.getName());
            } else {
                count++;
            }
        }
        return count;
    }

    // //execute command
    public static ArrayList<String> executeShellCommand(String command) {
        ArrayList<String> jobsID = new ArrayList<String>();
        String lineCommand = "";
        try {
            Runtime env = Runtime.getRuntime();
            Process process = env.exec(command);
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);

            while ((lineCommand = input.readLine()) != null) {
                System.out.println(lineCommand);
                jobsID.add(lineCommand);
            }
            int exitVal = process.waitFor();
            System.out.println("Exited with error code " + exitVal);
            process.getInputStream().close();
            input.close();
            process.destroy();
        } catch (Exception e) {
            //e.printStackTrace();
            lineCommand = "Error: " + e.getMessage();
        }

        return jobsID;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        //function to open directory
        ArrayList<String> listF = new ArrayList<String>();
        ArrayList<String> output = new ArrayList<String>();
        String filesDir = "/home/dida/DIRAC";
        String diracDir = "/home/dida/DIRAC/scripts";
        String jdlDir = filesDir + "/genFileJDL";
        jdlFileJob(jdlDir);
        int count = countFile(jdlDir);
        //generate jdl file to submit job
        for (int j = 1; j <= count; j++) {
            //execute command shell line
            long start = System.currentTimeMillis(); //start time of job submission
            String command = filesDir + "/scripts/dirac-wms-job-submit" + " " + jdlDir + "/docking-" + j + ".jdl";
            output = executeShellCommand(command);
            long stop = System.currentTimeMillis();
            //jobID.add(output);
            long timeJob = stop - start;
            System.out.println("Elapsed " + timeJob + "ms");
            //check the status of job 
            //checkStatus(output);
        }
    }

}
