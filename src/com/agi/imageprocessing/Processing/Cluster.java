package com.agi.imageprocessing.Processing;

import java.util.ArrayList;
import java.util.List;


/* 
 * This class takes points as input and determines whether those points belong to this cluster or not.
 * If the class has been provided enough points the class will construct a face object.
 */

public class Cluster {

	// The radius a point needs to be within from the center of the cluster in order to be a valid entry
	private static int DISTANCE_THRESHOLD = 10;
	
	// The amount of data entries the sample needs in order for it to be a face
	private static int SIZE_THRESHOLD = 5;
	
	private List<Face> data;
	private int xMean;
	private int yMean;
	
	public Cluster() {
		data = new ArrayList<Face>();
		
	}
	
	public boolean add(Face frame) {
		// No data has been added yet hence this frame must belong to this cluster
		if(this.getSampleSize() == 0) {
			this.xMean = frame.getX();
			this.yMean = frame.getY();
			data.add(frame);
			return true;
			
		}
		
		int x = frame.getX();
		int y = frame.getY();
		int distanceSquared = (int) (Math.pow((this.xMean - x), 2) + Math.pow((this.yMean - y), 2));
		
		if(distanceSquared <= Math.pow(DISTANCE_THRESHOLD, 2)) {
			// Update the current average position
			int tempXMean = this.xMean * this.getSampleSize() + frame.getX();
			int tempYMean = this.yMean * this.getSampleSize() + frame.getY();
			tempXMean /= (this.getSampleSize() + 1);
			tempYMean /= (this.getSampleSize() + 1);
			this.xMean = tempXMean;
			this.yMean = tempYMean;
			
			data.add(frame);
			return true;
			
		}
		
		return false;
		
	}
	
	private int getSampleSize() {
		return this.data.size();
		
	}
	
	public Face getFace() {
		float n = getSampleSize();
		
		if(n >= SIZE_THRESHOLD) {
			int widthTotal = 0;
			int heightTotal = 0;
			float angleTotal = 0;
			
			for(Face entry : data) {
				widthTotal += entry.getWidth();
				heightTotal += entry.getHeight();
				angleTotal += entry.getAngle();
				
			}
			
			return new Face(xMean, yMean, Math.round(widthTotal/n), Math.round(heightTotal/n), Math.round(angleTotal/n));
			
		}
		return null;
		
	}
	
	
}
