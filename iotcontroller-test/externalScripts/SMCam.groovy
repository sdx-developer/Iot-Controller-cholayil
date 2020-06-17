import org.json.JSONObject

import org.opencv.core.Core;
import com.sdx.platform.EventHandling.SedaEventDistributer;
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

import com.sdx.platform.EventHandling.RunSedaApp
import com.sdx.platform.EventHandling.SedaEndPointRouter
import com.sdx.platform.config.Memory

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;




class SMCam {
	static void main(String[] args)throws TesseractException {
		Memory.appProperties = appProperties;
		PerformanceEventGen performance=new PerformanceEventGen();
		 

		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoCapture capture = new VideoCapture("Media1.mp4");
		if (!capture.isOpened()) {
			println("Unable to open: " + capture);
			System.exit(0);
		}

		Mat frame = new Mat(), fgMask = new Mat();
		while (true) {
			//************** To Read the frames***********
			capture.read(frame);

			//************* REGION OF INTEREST*********
			Rect rectCrop = new Rect(325, 280, 350, 70);
			Mat croppedImage = new Mat(frame, rectCrop);
			Mat dst = new Mat();

			//*************IMAGE PROCESSING*****
			Imgproc.threshold(croppedImage, dst, 110, 255, Imgproc.THRESH_BINARY);
			Imgcodecs.imwrite("croppedImage.jpg", dst);
			println("Image Processed");

			//**********TESSERACT***************
			Tesseract tesseract = new Tesseract();
			tesseract.setDatapath("T:/JAVA_IOT/Tess4J-3.4.8-src/Tess4J/tessdata");
			tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");


			//********REMOVE WHITE SPACE IN CONSOLE***********
			String result = tesseract.doOCR(new File("croppedImage.jpg"));
			result = result.replaceAll("\\s+", "");
			
			println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
			performance.buildPerformancePayload(eventVal);
			println("stream falvour:::::::::::::::::"+ Memory.appProperties.getString("streamFlavor"));
			
			if (result.length()==6) {
				Memory.appProperties.getString("streamFlavor");
				println("result:::::::::::::::::"+ result );
				
			}

			else {
				println("VALUES EXCEEDED : ERROR");
			}


			if (frame.empty()) {
				break;
			}

			HighGui.imshow("ROI", croppedImage);


			int keyboard = HighGui.waitKey(30);
			if (keyboard == 'q' || keyboard == 27) {
				break;
			}
		}

		HighGui.waitKey();
		System.exit(0);
	}
	
	
	
}



