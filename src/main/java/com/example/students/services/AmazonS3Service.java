package com.example.students.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * @author alamalcantara
 *
 */
@Service
public class AmazonS3Service {

	@Autowired
	private AmazonS3 s3;
	
	@Value("${bucket.name}")
	private String bucketName;
	
	private static final Logger log = LoggerFactory.getLogger(AmazonS3Service.class);
	
	/** 
	 * Method to delete all the objects in the specified path
	 * @param path -> path (key)
	 * */
	public void deleteObjects(String path) {
		ObjectListing objectListing = s3.listObjects(bucketName, path);
		
		for(S3ObjectSummary os : objectListing.getObjectSummaries() ) {
			log.info("OBJECT KEY: {}", os.getKey());
			s3.deleteObject(bucketName, os.getKey());
		}
	}
	
	/** 
	 * Method to upload a file in the specified path
	 * @param pathToUpload -> path (key)
	 * @param fileToUpload -> to be uploaded
	 * */
	public void putObject(String pathToUpload, File fileToUpload) throws SdkClientException, AmazonServiceException {
		s3.putObject(bucketName, pathToUpload, fileToUpload);
	}
	
	
	/** 
	 * Method to get an object from the specified path
	 * @param pathToSearch -> path (key)
	 * @return S3Object
	 * */
	public S3Object getS3Object(String pathToSearch) throws SdkClientException, 
		AmazonServiceException {
		return s3.getObject(bucketName, pathToSearch);
	}
	
}
