/**
 * 
 */
package com.example.students.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @author alamalcanta
 *
 */
@Configuration
public class AmazonS3Config {

	
	public AWSCredentials getCredentials() {
		return new BasicAWSCredentials(
				"AKIARM6TCOPTR3ZDI4XC", 
				"JJtNvNpznVBGQB+4I50cTRLV6I8jhO9dXG/t6CR+");
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
