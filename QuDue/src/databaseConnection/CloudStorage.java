package databaseConnection;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;


public class CloudStorage {
	 
	public static String bucketName = "qudue";
	
	
	public static File getObject(String key) {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        File localFile = null;
        try {

             localFile = new File(key);
            s3Client.getObject(new GetObjectRequest(bucketName, key), localFile);
         
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
            		" means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
            		" the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        
      //Now your file will have your image saved 
        boolean success = localFile.exists() && localFile.canRead(); 
                
        if (success){
        	return localFile;
        } else {
        	return null;
        }
        
        
    }
}