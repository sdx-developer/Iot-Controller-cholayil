package com.sdx.platform.config;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import org.codehaus.groovy.control.CompilationFailedException;

public class ShiftSchedularModule 
	{
		static String m = "started";

		public static void main(String[] args)
				throws ParseException, InterruptedException, CompilationFailedException, IOException {

			String shift1 = Memory.getAppProperties().getString("Moduleshift");
			//String shift2 = Memory.getAppProperties().getString("shift2Time");
			String[] shft = shift1.split("-");
			//System.out.println((shft[0]));
			//System.out.println((shft[1]));
		
			
			
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date shiftStartTime = dateFormatter.parse(java.time.LocalDate.now() +" "+(shft[0]));
			Date shiftEndTime = dateFormatter.parse(java.time.LocalDate.now() +" "+(shft[1]));
			
	         //System.out.println(":::::::::::::::::::::::llllllllll"+shiftStartTime);
			Timer timer = new Timer();
			
			timer.schedule(new ShiftHandler(), shiftStartTime);
			timer.schedule(new ShiftHandler(), shiftEndTime);
	    
		 

		}

	}


