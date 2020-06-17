package com.sdx.platform.EventHandling;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
	
	
	public static String dateToStringConv(Date inDate) {
	  
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       
		String mDate = simpleDateFormat.format(inDate);
		
		return mDate;
	}
	
	public static String dateToStringConvWithTime(Date inDate) {
		  
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
       
		String mDate = simpleDateFormat.format(inDate);
		
		return mDate;
	}

}
