package com.sdx.platform.groovy.scripts

import com.sdx.platform.EventHandling.AvailabilityEventGen;
import com.sdx.platform.config.Memory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


class Camfm {

	int var;
	
	int firstCount =0;
	int count=1;

		TimeUnit time = TimeUnit.SECONDS;

	int sample() {
		String Status = "";

		while(true) {
			try{
				
				if(Memory.getAppProperties().getString("MachineStatus").equalsIgnoreCase("started")) {
			  if(count%2==0){

				Random objGenerator = new Random();

				int randomNumber = objGenerator.nextInt(20000);
				int var = randomNumber;
				System.out.println("stop");
				Status = "stop"
				AvailabilityEventGen.buildAvailabilityPayloadModule(Status);
			}
			 if(!(count%2==0)){
				int var=0000;
				Status = "start"
				AvailabilityEventGen.buildAvailabilityPayloadModule(Status);
				System.out.println("start");
			}
			count++;
		}}catch (Exception e) {
									e.printStackTrace();
								}
								time.sleep(10);
		}
	}

	static void main(String[] args) {

		Camfm fm = new Camfm();
		fm.sample();
	}
}
