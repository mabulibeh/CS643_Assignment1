package org.example.basicapp;
import java.util.List;


import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.TextDetection;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sqs.model.Message;

import java.nio.ByteBuffer;
import com.amazonaws.util.IOUtils;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class App{



    public static void main(String[] args) throws Exception {

    	String queueUrl = "https://sqs.us-east-1.amazonaws.com/113375730020/Motaz_Assignment1_SQS.fifo";
    	AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
 
   	
// get list of files in bucket
    	AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    	String bucketName = "njit-cs-643";

// ECSession variable is to determine which application will run depending on EC2 instance
    	// Select = 1 for first EC2 session and 2 for second EC2 session
    	int ECSession = 2;
    	
    	
//EC1   
    	if (ECSession == 1) {
	    	ObjectListing objectListing = s3Client.listObjects(bucketName);
	    	List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
	    	String objectKey = "";
	    	
	    	for (S3ObjectSummary objectSummary : objectSummaries) {
	    		objectKey = objectSummary.getKey();
	//Read images from S3 bucket
	            
	            S3Object s3Object = s3Client.getObject(bucketName, objectKey);
	            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
	            ByteBuffer imageBytes = ByteBuffer.wrap(IOUtils.toByteArray( objectInputStream));
	            
	 //Detect labels in image        
	            ImageLabelDetect imageLabelDetect = new ImageLabelDetect().detect(imageBytes);
	            List<Label> resultLabel = imageLabelDetect.result();
	
	 //Check if has car with confidence level above 90% send to SQS
	            
	            if (imageLabelDetect.hasCar()){
		            
	            	System.out.println(", file name is: " + objectKey + "\n" + "\n");
		        	String messageGroupId = "g2";
		        	SendMessageRequest send_msg_request = new SendMessageRequest()
		        	        .withQueueUrl(queueUrl)
		        	        .withMessageBody(objectKey)
		        	        .withMessageGroupId(messageGroupId);
		        	sqs.sendMessage(send_msg_request);
		            
		            //label2.printLabel(resultLabel, objectKey);
		        	
	            
		    	}
	    	}
		    	SendMessageRequest send_msg_request = new SendMessageRequest()
		    	        .withQueueUrl(queueUrl)
		    	        .withMessageBody("-1")
		    	        .withMessageGroupId("g2");
		    	sqs.sendMessage(send_msg_request);
		    	System.out.println("Done scanning all objects in S3 bucket. -1 is sent as last message");
	    	
    	}	
    	
    	
 //Ec2
 //Read from SQS and detect text in pictures then save results in txt file
    	
    	
    	if (ECSession == 2) {
	    	String msg ="";
	    	while (!msg.equals("-1")) {
	    	ReceiveMessageRequest receive_msg_request = new ReceiveMessageRequest()
	    			.withQueueUrl(queueUrl)
	    			.withMaxNumberOfMessages(1);
	    	ReceiveMessageResult receiveResult = sqs.receiveMessage(receive_msg_request);
	    	
	    	if (receiveResult.getMessages().size()> 0) {
		    	Message message = receiveResult.getMessages().get(0);
		    	msg = message.getBody();
		    		sqs.deleteMessage(queueUrl , message.getReceiptHandle());
		    		
		    	if (msg.equals("-1")) {
		    		System.out.println("----Reached the end of the queue. Process is compelte");
		    		break;
		    	}
		        S3Object s3Object = s3Client.getObject(bucketName, msg);
		        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
		    	ByteBuffer imageBytes = ByteBuffer.wrap(IOUtils.toByteArray( objectInputStream));
		        
		    	
		        //Detect texts in image and store them in a list of TextDetection
		        ImageTextDetect imageTextDetect = new ImageTextDetect().detect(imageBytes);
		        List<TextDetection> resultText = imageTextDetect.result();
		 
		        //Print text results
		        PrintFoundText text1 = new PrintFoundText();
		        text1.printText(resultText, message.getBody());
	    	}
		        
	    	}
            
    	}	

    	
        }
    }


