package org.example.basicapp;

import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Instance;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.Parent;
import java.nio.ByteBuffer;

public class ImageLabelDetect {
	
	private List<Label> labels;
	
	public ImageLabelDetect detect(ByteBuffer byteBuffer){
		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
		
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(byteBuffer))
                .withMaxLabels(10)
                .withMinConfidence(77F);
	
		try {
		    DetectLabelsResult result = rekognitionClient.detectLabels(request);
		    labels = result.getLabels();
		

		} catch (AmazonRekognitionException e) {
		    e.printStackTrace();
		}

		return this;
	}
	
	public List<Label> result(){
		
		return labels;
	}
	
	// check if label has car with confidence higher than 90% return true
	public boolean hasCar() {
		
		for (Label label: labels) {
			if (label.getName().equals("Car")) {
				if (label.getConfidence() > 90) {
					System.out.println("Found a " + label.getName() + " picture!");
					System.out.print(" with confidence level of: " + label.getConfidence());
					return true;
					
					
				}
				
			}
		}
		return false;
	}

}
