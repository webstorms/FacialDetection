package com.agi.imageprocessing.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;  

import org.opencv.core.Core;  
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import com.agi.imageprocessing.FaceDetector;
import com.agi.imageprocessing.Processing.Face;

/*
 * This class provides a graphical implementation of the FaceDetector class. You can observe a continuous video stream
 * with information regarding all the detected faces on the left hand side of the screen. All faces are enclosed by an oval.
 * 
 */

public class GUI extends JPanel {
	
	public static final int SCREEN_WIDTH = 1280 / 2;
	public static final int SCREEN_HEIGHT = 1024 / 2;
	
	private static FaceDetector faceDetector;
	private static VideoCapture webCam;
	private static Mat currentFrame;	
	private static final int UPDATE_RATE = 30;
	
	public GUI() {
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		Thread gameThread = new Thread() {
			public void run() {
				while (true) {
					repaint(); 
					try {
						Thread.sleep(1000 / UPDATE_RATE);
					} catch (InterruptedException ex) { }
				}
			}
		};
		gameThread.start();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		webCam.read(currentFrame);
		faceDetector.findFaces(currentFrame);
		
		if(currentFrame == null || currentFrame.empty()) return;
		g.drawImage(getImage(draw(currentFrame)), 0, 0, GUI.SCREEN_WIDTH, GUI.SCREEN_HEIGHT, null);
		g.drawString("Count: " + faceDetector.getFaceCount(), 10, 20);
		
		for(int i = 0; i < faceDetector.getFaceCount(); i++) {
			g.drawString("Face " + i, 15, 40 + i * 100);
			g.drawString("X " + faceDetector.getFaces().get(i).getX(), 20, 40 + i * 100 + 25);
			g.drawString("Y " + faceDetector.getFaces().get(i).getY(), 20, 40 + i * 100 + 50);
			g.drawString("Angle " + faceDetector.getFaces().get(i).getAngle(), 20, 40 + i * 100 + 75);
			
		}
		

	}

	public BufferedImage getImage(Mat inputframe) {
		MatOfByte mb = new MatOfByte();  
		Highgui.imencode(".bmp", inputframe, mb);
		try {  
			return ImageIO.read(new ByteArrayInputStream(mb.toArray())); 
		} catch (IOException e) { }  

		return null;

	}

	public static Mat draw(Mat inputframe) {
		Mat mRgba = new Mat();  
		Mat mGrey = new Mat();  
		inputframe.copyTo(mRgba);  
		Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
		
		for(Face face : faceDetector.getFaces()) {
			Point center = new Point(face.getX(), face.getY());
			Core.ellipse(mGrey, center, new Size(face.getWidth() * 0.4, face.getHeight() * 0.7), face.getAngle(), 0, 360, new Scalar(255, 0, 255), 4, 8, 0);

		}
		
		return mGrey;

	}
	
	public static void main(String[] args) throws Exception {
		faceDetector = new FaceDetector();
		webCam = new VideoCapture(1);
		webCam.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, FaceDetector.RES_WIDTH);
		webCam.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, FaceDetector.RES_HEIGHT);
		currentFrame = new Mat();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("webcam view");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new GUI());
				frame.pack();
				frame.setVisible(true);
				
			}
		});
		
	}
	
	
}
