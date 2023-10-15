package org.example.basicapp;

import java.util.List;

import com.amazonaws.services.rekognition.model.Instance;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.Parent;

public class PrintFoundLabel {
	
	public void printLabel(List<Label> labels, String fileName) {
		
	    System.out.println("Detected labels for " + fileName + "\n");
	    for (Label label : labels) {
	        System.out.println("Label: " + label.getName());
	        System.out.println("Confidence: " + label.getConfidence().toString() + "\n");
	
	        List<Instance> instances = label.getInstances();
	        System.out.println("Instances of " + label.getName());
	        if (instances.isEmpty()) {
	            System.out.println("  " + "None");
	        } else {
	            for (Instance instance : instances) {
	                System.out.println("  Confidence: " + instance.getConfidence().toString());
	                System.out.println("  Bounding box: " + instance.getBoundingBox().toString());
	            }
	        }
	        System.out.println("Parent labels for " + label.getName() + ":");
	        List<Parent> parents = label.getParents();
	        if (parents.isEmpty()) {
	            System.out.println("  None");
	        } else {
	            for (Parent parent : parents) {
	                System.out.println("  " + parent.getName());
	            }
	        }

	        
	       
	    }
		
		
	}

}
