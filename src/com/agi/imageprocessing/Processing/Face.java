package com.agi.imageprocessing.Processing;

/* 
 * This class contains all the information of a detected face.
 * 
 */

public class Face {

	private int x;
	private int y;
	private int width;
	private int height;
	private int angle;
	
	public Face(int x, int y, int width, int height, int angle) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = angle;
		
	}
	
	public int getX() {
		return this.x;
		
	}
	
	public int getY() {
		return this.y;
		
	}
	
	public int getWidth() {
		return this.width;
		
	}
	
	public int getHeight() {
		return this.height;
		
	}
	
	public int getAngle() {
		return this.angle;
		
	}
	
	
}
