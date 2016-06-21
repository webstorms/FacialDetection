package com.agi.imageprocessing.Processing;

import java.util.ArrayList;
import java.util.List;


/* 
 * This class contains a list of Cluster object. It acts as an intermediate step in the process.
 * It will create new clusters if a point cannot be added to a previous cluster.
 * 
 */

public class ClusterCollection {

	private List<Cluster> clusters;

	public ClusterCollection() {
		this.clusters = new ArrayList<Cluster>();
		
	}
	
	public void clear() {
		this.clusters.clear();

	}

	public void process(List<Face> faces) {
		for(Cluster cluster : this.clusters) {
			Face face = cluster.getFace();
			if(face != null) {
				faces.add(face);

			}

		}

	}

	public void addFrame(Face entry) {
		boolean added = false;
		
		for(Cluster cluster : this.clusters) {			
			boolean state = cluster.add(entry);
			if(!added && state) {
				added = true;

			}

		}

		if(!added) {
			clusters.add(new Cluster());
			clusters.get(clusters.size() - 1).add(entry);

		}

	}

	
}
