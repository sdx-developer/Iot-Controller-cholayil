package com.sdx.platform.quartz.service.impl;

import java.io.File;
import java.util.Date;

public class JobType {
	public static void main(String[] args){

        long numDays = 8;   //this needs to be a long.

        //WARNING!  OLD FILES IN THIS DIRECTORY WILL BE DELETED.
        String dir = "C:\\temp";
        //IF YOU ACCIDENTALLY POINT THIS TO C:\\Windows or other sensitive
        //directory (and run it) you will be living in the house of pain.

        File directory = new File(dir);
        File[] fList = directory.listFiles();

        if (fList != null){
            for (File file : fList){
                if (file.isFile() &&
                    file.getName().contains("MyLogFile_")){

                    long diff = new Date().getTime() - file.lastModified();
                    long cutoff = (numDays * (24 * 60 * 60 * 1000));

                    if (diff > cutoff) {
                      file.delete();
                    }
                }
            }
        }
    }
}

