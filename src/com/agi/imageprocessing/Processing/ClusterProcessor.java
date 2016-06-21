package com.agi.imageprocessing.Processing;

import java.util.ArrayList;
import java.util.List;


/* 
 * This class collects all the raw data and processes it from a high level.
 * 
 */

public class ClusterProcessor {
	
	private ClusterCollection clusters; // Contains all the raw data
	private List<Face> faces; // Raw processed data in the form of faces
	private int indexOfPrimaryFace; // Index of the face with the largest surface area
	
	public ClusterProcessor() {
		clusters = new ClusterCollection();
		faces = new ArrayList<Face>();

	}
	
	// Called before a new continuous data load is to be supplied
	public void reset() {
		this.clusters.clear();
		this.faces.clear();
		
	}
	
	// Called after all the data has been supplied
	public void process() {
		this.clusters.process(faces);
		
		// Find primary face
		this.indexOfPrimaryFace = -1;
		int maxArea = 0;
		for(int i = 0; i < this.getFaceCount(); i++) {
			int width = this.faces.get(i).getWidth();
			int height = this.faces.get(i).getHeight();
			int area = width * height;
			if(area > maxArea) {
				maxArea = area;
				this.indexOfPrimaryFace = i;
				
			}
			
		}
		
	}
	
	public void addFrame(Face entry) {
		this.clusters.addFrame(entry);
		
	}
	
	public List<Face> getFaces() {
		return faces;
		
	}
	
	public int getFaceCount() {
		return this.faces.size();
		
	}
	
	public Face getPrimaryFace() {
		if(this.indexOfPrimaryFace == -1) {
			return null;
			
		}
		return this.faces.get(this.indexOfPrimaryFace);
		
	}
	
	
}
