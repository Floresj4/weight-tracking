package com.flores.dev.dynamo.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
@EnableDynamoDBRepositories
public class DynamoDbConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;
    
    @Bean
    public AmazonDynamoDB amazonDynamoDb() {
    	
    	return AmazonDynamoDBClientBuilder.standard()
    			.withCredentials(amazonAWSCredentials())
    			.build();
    }
    
    @Bean
    public AWSCredentialsProvider amazonAWSCredentials() {
    	return new AWSStaticCredentialsProvider(
    			new BasicAWSCredentials(amazonAWSAccessKey, 
    					amazonAWSSecretKey));
    }
}
