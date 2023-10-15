package org.example.basicapp;

import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.TextDetection;
import java.nio.ByteBuffer;

public class ImageTextDetect {
	
	String photo ="klklkl";
	private List<TextDetection> textDetections;
	
	public ImageTextDetect detect(ByteBuffer byteBuffer) {
	
		AmazonRekognition rekognitionClient2 = AmazonRekognitionClientBuilder.defaultClient();
	
	    DetectTextRequest request2 = new DetectTextRequest()
	            .withImage(new Image().withBytes(byteBuffer));
		
	    try {
	        DetectTextResult result2 = rekognitionClient2.detectText(request2);
	        textDetections = result2.getTextDetections();
        }
		
		catch(AmazonRekognitionException e) {
		    e.printStackTrace();
		 }
	    return this;
	}
	
	public List<TextDetection> result() {
		
		return textDetections;
	
	}
	

}
