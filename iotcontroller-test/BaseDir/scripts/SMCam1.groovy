import com.sdx.platform.EventHandling.PerformanceEventGen;
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
import java.util.concurrent.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

class SMCam {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
    static Logger log = LoggerFactory.getLogger(SMCam.class);
	 static CommonUtil commonUtil = new CommonUtil();
	static void main(String[] args) throws Exception {
   
		
		// System.load("A://Iot//opencv//build//java//x64//opencv_videoio_ffmpeg411_64.dll");
		String status = Memory.getAppProperties().getString("stampingIOTStatus");
		
		Runnable runner = new Runnable() {
					@Override
					public void run() {
						Runtime.getRuntime().load0(groovy.lang.GroovyClassLoader.class,
								"C:\\Program Files\\opencv\\build\\java\\x64\\opencv_java412.dll");
						Mat frame = new Mat();
						Mat croppedImage = null;
						String result = null;
						boolean grabbed = false;
						double finalResult = 0;
						double bestResult = 0;
						double resultint = 0;
						Rect rectCrop = new Rect(1000, 490, 1390, 300);
						Tesseract tesseract = new Tesseract();
						tesseract.setDatapath("D:\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
						tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");
						NumberFormat nf = NumberFormat.getInstance();
						Thread t = new Thread();
						croppedImage = new Mat();
						int count = 3;
						int errCount=0;
						int refreshInterval=0;
						int boundLimitValue = 500;
						String currentDate = commonUtil.dateToStringConv(new Date());
						TimeUnit time = TimeUnit.SECONDS;
						int timeToSleep =0; 
					
					
							
							while(true)  {
								
								// **********OPEN STREAMING*******
								VideoCapture capture = null;
								if (capture==null){
								capture = new VideoCapture("rtsp://admin:Admin123@192.168.1.64:554");
							}
								errCount=0;
								

								if(Memory.getAppProperties().getString("stampingIOTStatus").equalsIgnoreCase("started")) {
								try {

								
									
									/* if (capture == null) {
										
									} */
									// ****NEGATIVE CASE*********
									if (capture.isOpened()) {
										grabbed = capture.grab();
									}
									if (!capture.isOpened() || !grabbed) {
										
										
										capture.release();
										capture = null;
										
										errCount++;
									
									}
									if (grabbed) {
										capture.read(frame);
									}

									if (!(frame == null)) {

										// ***********IMAGE PROCESSING***

										// Imgproc.threshold(frame,croppedImage, 600, 900, Imgproc.THRESH_BINARY);
										// Imgproc.threshold(frame,croppedImage, 700, 500, Imgproc.THRESH_BINARY);
										// Imgproc.cvtColor(frame,croppedImage, Imgproc.COLOR_BGR2HSV);
										// Imgproc.threshold(frame,croppedImage, 50, 255, Imgproc.THRESH_BINARY);
										Imgproc.cvtColor(frame, croppedImage, Imgproc.COLOR_RGB2GRAY);
										Imgcodecs.imwrite("croppedImage.jpg", croppedImage);
									}

								} catch (Exception e) {
									e.printStackTrace();
								}

								// *******TESSERACT READING********

								try {
									result = tesseract.doOCR(mat2BufferedImage(new Mat(croppedImage, rectCrop)));
									// result = tesseract.doOCR(new File("croppedImage.jpg"));
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (!(result == null)) {
									result = result.replaceAll("\\s+", "");
								}
								// *******CONVERTING STRING RESULT TO INTEGER *******
								try {
									if (!(result.isEmpty()) && (result.length() == 6)) {

										resultint = nf.parse(result).doubleValue();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								// ***********CHECKING THE RESUT AS EXCEEDED LENGTH***********
								
								try {
									if (!(result.length() == 6)) {
										
										errCount++;
									}
									// ***********CHECKING THE UPDATED RESULT WITH THE BEST RESULT**********
									if (result.length() == 6) {
										
										count = 0;
									} else if (count % 3 == 0) {
										
										if(count % 15 == 0) {

											
										}
									}
									if (result.length() == 6 && resultint > bestResult && (!(bestResult == 0))) {
									
										// ***********PASSING THE RESULT***********
										PerformanceEventGen.buildPerformancePayload(result);
									}

									// ***********CHECKING THE RESUT AS RIGHT LENGTH***********

									if (result.length() == 6 && resultint > bestResult||(bestResult == 0)) {
										bestResult = resultint;

									}else{
										errCount++;
										
									}
									if((errCount)>1){
						errCount--;
						
						}
						
						  if (currentDate.equals(Memory.getAppProperties().getString("perErrCountCurrentDate"))) { 
						 
				          int previousErrorCount = Integer.parseInt(Memory.getAppProperties().getString("errorCountPer"));
						  int totalErrorCount=previousErrorCount+errCount;
						  String errorcount = Integer.toString(totalErrorCount);
						  // Memory.getAppProperties().setProperty("errorCountPer",macPerfCnt);
						  Memory.clearAndAddProperty("errorCountPer", errorcount);
						   EventsFunction.refreshPropertyFile();			
								
					}else {
			              
			              String macErrorCnt = Integer.toString(errCount);
			              Memory.clearAndAddProperty("errorCountPer", macErrorCnt);
			              Memory.clearAndAddProperty("perErrCountCurrentDate", currentDate);
			              EventsFunction.refreshPropertyFile();
		}			
									if (frame.empty()) {

									}

									//capture.release();

								} catch (Exception e) {
									e.printStackTrace();
								}
								try
								{
									if (errCount==1){
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
										 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
									FileWriter fw=new FileWriter("D:\\Delete.txt",true);
								fw.write(errCount+" STAMPING_MACHINE "+timestamp+"\n");
							   fw.close();
									}
								refreshInterval=Memory.getAppProperties().getInt("modulesRefreshInterval");
								timeToSleep = time.convert(refreshInterval,TimeUnit.MINUTES)-2;
								time.sleep(timeToSleep); 
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block e1.printStackTrace();
								}

							}
						}
					}
				};

		new Thread(runner).start();
		//}
	}

	static BufferedImage mat2BufferedImage(Mat matrix) throws Exception {
		BufferedImage bi = null;
		try {
			MatOfByte mob = new MatOfByte();
			Imgcodecs.imencode(".jpg", matrix, mob);
			byte[] ba = mob.toArray();
			bi = ImageIO.read(new ByteArrayInputStream(ba));
			return bi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bi;

	}

}

/*
 * static BufferedImage mat2BufferedImage(Mat matrix) throws Exception {
 * MatOfByte mob = new MatOfByte(); Imgcodecs.imencode(".jpg", matrix, mob); {
 * vars ->
 *
 * } { vars ->
 *
 * }[] = mob.toArray();
 *
 * BufferedImage bi = ImageIO.read(new ByteArrayInputStream({ vars ->
 *
 * })); return bi; }
 */