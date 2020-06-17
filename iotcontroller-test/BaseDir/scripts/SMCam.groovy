import com.sdx.platform.EventHandling.PerformanceEventGen;
import com.sdx.platform.EventHandling.ShiftDetails

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import com.sdx.platform.config.Memory;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
//import java.awt.Rectangle
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.opencv.core.MatOfByte;
import com.sdx.platform.EventHandling.CommonUtil;
import com.sdx.platform.EventHandling.EventsFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sdx.platform.EventHandling.CommonUtil;
class SMCam {

	static CommonUtil commonUtil = new CommonUtil();
	static void main(String[] args) throws Exception,NumberFormatException, IOException, ParseException {

		int smCamNumber = 111110;
		TimeUnit time = TimeUnit.MINUTES;

		Random rnd = new Random();
		int errorCount=0;
		int totalProductionCount=0;
		String currentDate = commonUtil.dateToStringConv(new Date());
		String result ="";

		while (true) {
			int number = rnd.nextInt(222222);
			String random = String.format("%06d", number);

			errorCount=0;

			int randomNumber = Integer.parseInt(random);

			if(Memory.getAppProperties().getString("stampingIOTStatus").equalsIgnoreCase("started")) {
				try {
					smCamNumber++;
					
					if (smCamNumber>randomNumber) {

						result=String.valueOf(smCamNumber);
						System.out.println("Performance count for check  "+ result);
                            
						totalProductionCount++;
						Memory.setRealTimeTotalPC(String.valueOf(totalProductionCount));
						Memory.setRealTimeStampingCount(String.valueOf(smCamNumber));
						PerformanceEventGen.buildPerformancePayloadmodule(result);
						
					}else {

						//System.out.println("I am Negative Count");
						errorCount++;
					}
					if((ShiftDetails.shiftTiming())!=null) {
					if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("date")) {
					if (currentDate.equals(Memory.getAppProperties().getString("perErrCountCurrentDate"))) {

						int previousErrorCount = Integer.parseInt(Memory.getAppProperties().getString("errorCountPer"));
						int totalErrorCount=previousErrorCount+errorCount;
						String errorcount = Integer.toString(totalErrorCount);



						// Memory.getAppProperties().setProperty("errorCountPer",macPerfCnt);
						Memory.clearAndAddProperty("errorCountPer", errorcount);
						EventsFunction.refreshPropertyFile();

					}else {

						String macErrorCnt = Integer.toString(errorCount);
						Memory.clearAndAddProperty("errorCountPer", macErrorCnt);
						Memory.clearAndAddProperty("perErrCountCurrentDate", currentDate);
						EventsFunction.refreshPropertyFile();
					}}else if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("shift")){
			          println(":::::"+Memory.getAppProperties().getString("perErrorShift"));
			         if (Memory.getAppProperties().getString("perErrorShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
						 int previousErrorCount = Integer.parseInt(Memory.getAppProperties().getString("errorCountPer"));
						 int totalErrorCount=previousErrorCount+errorCount;
						 String errorcount = Integer.toString(totalErrorCount);
 
 
 
						 // Memory.getAppProperties().setProperty("errorCountPer",macPerfCnt);
						 Memory.clearAndAddProperty("errorCountPer", errorcount);
						 EventsFunction.refreshPropertyFile();
 
					 }else {
 
						 String macErrorCnt = Integer.toString(errorCount);
						 Memory.clearAndAddProperty("errorCountPer", macErrorCnt);
						 Memory.clearAndAddProperty("perErrorShift",ShiftDetails.shiftTiming() );
        				 EventsFunction.refreshPropertyFile();
					 }}}
					if (errorCount==1){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						FileWriter fw=new FileWriter("D:\\Delete.txt",true);
						fw.write(errorCount+" StampingMachine "+timestamp+"\n");
						fw.close();

					}
					time.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
					time.sleep(1);
				}
			}


		}
	}
}