/**
 * 
 */
package com.example.students.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @author alamalcantara
 *
 */
@Configuration
public class AmazonS3Config {

	@Value("${s3.accessKey}")
	private String accessKey;
	
	@Value("${s3.secretKey}")
	private String secretKey;
	
	public AWSCredentials getCredentials() {
		return new BasicAWSCredentials(accessKey, secretKey);
	}
	
	@Bean
	public AmazonS3 connectToS3() {
		return AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
				.withRegion(Regions.US_EAST_2)
				.build();
	}
	
}
