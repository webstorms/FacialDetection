package com.agi.imageprocessing;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.agi.imageprocessing.Processing.Face;
import com.agi.imageprocessing.Processing.ClusterProcessor;


/* 
 * This class attempts to detect faces in a given source mat. All potential detected candidates are fed into the
 * cluster processor to reduce the amount of false detections. Once all information has been processed it can be
 * accessed via the public interface of the class.
 * 
 */

public class FaceDetector {  

	public static final int RES_WIDTH = 1280 / 8;
	public static final int RES_HEIGHT = 1024 / 8;
	public static final int INCREMENT = 4;
	public static final int RANGE = INCREMENT * 13;
	public static final Point CENTRE = new Point(RES_WIDTH/2, RES_HEIGHT/2);
	
	private CascadeClassifier face_cascade; // Classifier that detects faces
	private ClusterProcessor processor; // Process the source into clusters of points (faces)
	
	public FaceDetector() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
		face_cascade = new CascadeClassifier("resources/lbpcascade_frontalface.xml");
		// face_cascade = new CascadeClassifier("resources/haarcascade_frontalface_alt.xml"); // Alternate classifier
		processor = new ClusterProcessor();
		if(face_cascade.empty()) throw new Exception("Face CascadeClassifier is empty");

	}
	
	// Attempts to find all the faces in the provided mat across all the different angles
	
	public boolean findFaces(Mat currentFrame) {
		if(currentFrame.empty()) return false;
		
		this.processor.reset();
		for(int ang = -RANGE; ang <= RANGE; ang += INCREMENT) {
			Mat temporaryFrame = rotate(currentFrame, ang);
			this.detect(temporaryFrame, ang);

		}
		this.processor.process();
		
		return true;
		
	}
	
	public List<Face> getFaces() {
		return this.processor.getFaces();
		
	}
	
	public int getFaceCount() {
		return this.processor.getFaceCount();
		
	}
	
	public Face getPrimaryFace() {
		return this.processor.getPrimaryFace();
		
	}
	
	// Detect faces in the given frame situated at a certain angle
	
	private void detect(Mat inputframe, int angle) {  
		Mat mRgba = new Mat();  
		Mat mGrey = new Mat();  
		inputframe.copyTo(mRgba);
		inputframe.copyTo(mGrey);  
		Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(mGrey, mGrey);

		// Faces
		MatOfRect detections = new MatOfRect(); 
		face_cascade.detectMultiScale(mGrey, detections);

		for(Rect rect : detections.toArray()) {
			Point center = transform((int) (rect.x + rect.width * 0.5), (int) (rect.y + rect.height * 0.5), angle); // Center of the potential detected face
			Face data = new Face((int) center.x, (int) center.y, rect.width, rect.height, angle);
			this.processor.addFrame(data);

		}
		
	}

	// Rotates a source frame to the given angle
	
	private Mat rotate(Mat src, double angle) {
		Mat dst = new Mat();
		Point pt = new Point(RES_WIDTH/2, RES_HEIGHT/2);
		Mat r = Imgproc.getRotationMatrix2D(pt, angle, 1.0);
		Imgproc.warpAffine(src, dst, r, new Size(RES_WIDTH, RES_HEIGHT));
		return dst;

	}
	
	// Apply a rotation transformation on a point (x, y) by a given angle
	
	private Point transform(int x, int y, int angle) {
		Point newPoint = new Point();
		float alpha = (float) Math.toRadians(angle);
		newPoint.x = (x - RES_WIDTH/2) * Math.cos(alpha) - (y - RES_HEIGHT/2) * Math.sin(alpha) + RES_WIDTH/2;
		newPoint.y = (x - RES_WIDTH/2) * Math.sin(alpha) + (y - RES_HEIGHT/2) * Math.cos(alpha) + RES_HEIGHT/2;

		return newPoint;

	}
	
	
}