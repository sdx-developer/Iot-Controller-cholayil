
package com.sdx.platform.EventHandling;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.sdx.platform.config.Memory;

public class ShiftDetails {

	public static String shiftTimingOld() throws IOException, ParseException {

		String shift1 = Memory.getIotProperties().getString("shift1");
		String shift2 = Memory.getIotProperties().getString("shift2");
		String shift3 = Memory.getIotProperties().getString("shift3");

		String shiftName = "";
		String[] shft = shift1.split("-");
		System.out.println((shft[0]));
		String[] shft1 = shift2.split("-");
		System.out.println((shft1[0]));
		String[] shft2 = shift3.split("-");
		System.out.println((shft2[0]));
		Date shft1StartTime = new SimpleDateFormat("HH:mm").parse(shft[0]);
		Date shft1EndTime = new SimpleDateFormat("HH:mm").parse(shft[1]);
		Date shft2StartTime = new SimpleDateFormat("HH:mm").parse(shft1[0]);
		Date shft2EndTime = new SimpleDateFormat("HH:mm").parse(shft1[1]);
		Date shft3StartTime = new SimpleDateFormat("HH:mm").parse(shft2[0]);
		Date shft3EndTime = new SimpleDateFormat("HH:mm").parse(shft2[1]);

		DateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		Date Today = sdf.parse(sdf.format(cal.getTime()));

		if (Today.after(shft1StartTime) && Today.before(shft1EndTime)) {
			shiftName = "1";
		} else if (Today.after(shft2StartTime) && Today.before(shft2EndTime)) {
			shiftName = "2";
		} else {
			shiftName = "3";
		}

		return shiftName;
	}

	public static String shiftTiming() throws IOException, ParseException {

		String shift1 = Memory.getIotProperties().getString("First");
		String shift2 = Memory.getIotProperties().getString("Second");

		String shiftName = null;
		String[] shft = shift1.split("-");
		String[] shft1 = shift2.split("-");

		Date shft1StartTime = new SimpleDateFormat("HH:mm").parse(shft[0]);
		Date shft1EndTime = new SimpleDateFormat("HH:mm").parse(shft[1]); 
		Date shft2StartTime = new SimpleDateFormat("HH:mm").parse(shft1[0]);
		Date shft2EndTime = new SimpleDateFormat("HH:mm").parse(shft1[1]);

		DateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		Date Today = sdf.parse(sdf.format(cal.getTime()));

		if (Today.after(shft1StartTime) && Today.before(shft1EndTime)) {
			shiftName = "1";
		}else if (Today.after(shft2StartTime) && Today.before(shft2EndTime)) {
			shiftName = "2";
		}

		return shiftName;
	}

	public static String shiftTimingdemo() throws IOException, ParseException {

		String shift1 = Memory.getAppProperties().getString("shift1Time");
		//String shift2 = Memory.getAppProperties().getString("shift2Time");
		System.out.println("::::::::::::inside the shiftdetails" + shift1 );

		String shiftName = "";
		String[] shft = shift1.split("-");
		System.out.println((shft[0]));
		/*String[] shft1 = shift2.split("-");
		System.out.println((shft1[0]));
*/
		
		  Date shft1StartTime = new SimpleDateFormat("HH:mm:ss").parse(shft[0]); Date
		  shft1EndTime = new SimpleDateFormat("HH:mm:ss").parse(shft[1]);
		 
		 
		  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  Calendar cala = Calendar.getInstance();
			Date shift1EndTime = shft1EndTime;
		    cala.setTime(shift1EndTime);
			cala.add(Calendar.SECOND,100);
			String shift1Time = df.format(cala.getTime());
            Date date = dateFormatter.parse(shift1Time);
            
         
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		Date Today = sdf.parse(sdf.format(cal.getTime()));

		if ((Today.after(shft1StartTime) && Today.before(date))||(Today.equals(shft1StartTime) || Today.equals(shft1EndTime))) {
			//System.out.println("::::::::::::::::::::::::::::::::inside into sgift1");
			shiftName = "1";
			}

		 else {
			shiftName=null;
		}
		Memory.clearAndAddProperty("shiftName", shiftName);
		ShiftEventGen.refreshPropertyFile();
		
		
		return shiftName;
	}
}
