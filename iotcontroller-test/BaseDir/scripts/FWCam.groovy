import com.sdx.platform.EventHandling.QualityEventGen;
import com.sdx.platform.EventHandling.ShiftDetails
import com.sdx.platform.config.Memory;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import com.sdx.platform.quartz.service.impl.AppProperties;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
class FWCam {

	static CommonUtil commonUtil = new CommonUtil();
	static void main(String[] args) throws Exception, NumberFormatException, IOException, ParseException {

		int smCamNumber = 100110;
		TimeUnit time = TimeUnit.MINUTES;
		
		Random rnd = new Random();
		int errorCount=0;
		int totalGoodCount=0;
		String currentDate = commonUtil.dateToStringConv(new Date());
		String result ="";

	while (true) {
			 int number = rnd.nextInt(222222);
			 String random = String.format("%06d", number);
			 
			 errorCount=0;
			 
			 int randomNumber = Integer.parseInt(random);
			if(Memory.getAppProperties().getString("flowwrapStatus").equalsIgnoreCase("started")) {
			try {
				smCamNumber++;
				
				if (smCamNumber>randomNumber) {
					
					result=String.valueOf(smCamNumber);
					//System.out.println("Quality count for check  "+ result);
					totalGoodCount++;
					Memory.setRealTimeTotalGC(String.valueOf(totalGoodCount));
					Memory.setRealTimeFlowrapCount(String.valueOf(smCamNumber));
					QualityEventGen.buildQualityPayloadModule(result);
					
				}else {
					
					//System.out.println("I am Negative Count");
					errorCount++;
				}
				if((ShiftDetails.shiftTiming())!=null) {
				if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("date")) {
				if (currentDate.equals(Memory.getAppProperties().getString("qualErrCountCurrentDate"))) {
							
							int previousErrorCount= Integer.parseInt(Memory.getAppProperties().getString("errorCountQual"));
							int totalErrorCount=previousErrorCount+errorCount;
							String errorcount = Integer.toString(totalErrorCount);
							// Memory.getAppProperties().setProperty("errorCountQual",macPerfCnt);
							Memory.clearAndAddProperty("errorCountQual", errorcount);
							 EventsFunction.refreshPropertyFile();
							
					  }else {
						  
						  String macErrorCnt = Integer.toString(errorCount);
						  Memory.clearAndAddProperty("errorCountQual", macErrorCnt);
						  Memory.clearAndAddProperty("qualErrCountCurrentDate", currentDate);
						  EventsFunction.refreshPropertyFile();
		}}else if(Memory.getIotProperties().getString("resetCount").equalsIgnoreCase("shift")){
			
		if (Memory.getAppProperties().getString("qualErrorShift").equalsIgnoreCase(ShiftDetails.shiftTiming())) {
							
							int previousErrorCount= Integer.parseInt(Memory.getAppProperties().getString("errorCountQual"));
							int totalErrorCount=previousErrorCount+errorCount;
							String errorcount = Integer.toString(totalErrorCount);
							// Memory.getAppProperties().setProperty("errorCountQual",macPerfCnt);
							Memory.clearAndAddProperty("errorCountQual", errorcount);
							 EventsFunction.refreshPropertyFile();
							
					  }else {
						  
						  String macErrorCnt = Integer.toString(errorCount);
						  Memory.clearAndAddProperty("errorCountQual", macErrorCnt);
						 Memory.clearAndAddProperty("qualErrorShift",ShiftDetails.shiftTiming() );
						  EventsFunction.refreshPropertyFile();
		}
		}}
		
							if (errorCount==1){
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
										 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
										 
										 FileWriter fw=new FileWriter("D:\\Delete.txt",true);
								fw.write(errorCount+" FLOW_WRAP "+timestamp+"\n");
							   fw.close();
									}
				
				time.sleep(1);
			} catch (Exception e) {

			}
		}

	}
	}
}
