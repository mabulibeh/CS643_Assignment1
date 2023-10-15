package org.example.basicapp;

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import com.amazonaws.services.rekognition.model.TextDetection;

public class PrintFoundText {
	
	public void printText(List<TextDetection> textDetection, String fileName) throws IOException {
		
		
		try {
			FileWriter out1 = new FileWriter("Results.txt", true);
			out1.write("# Found a car picture in file: " + fileName + ": " +"\n"); 
	    	System.out.println("# Found a car picture in file: " + fileName);
	    	
	    	if (textDetection.size()>0) {
		    	for (TextDetection text: textDetection) {
		    		out1.write("    Found text" + text.getDetectedText() + "\n");
		            System.out.println("    Found text: " + text.getDetectedText());
		    	
		    		}
	    	}else {
				out1.write("    No text found in this picture" + "\n" + "\n" ); 
				System.out.println("    No text found in this picture" + "\n" + "\n");
	    		
	    	}
	    	out1.write("\n" + "\n");
	    	System.out.println("\n");
	       	out1.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
