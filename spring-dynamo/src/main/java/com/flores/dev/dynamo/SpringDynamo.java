package com.flores.dev.dynamo;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SpringDynamo {

	private static final String amazonDynamoDBEndpoint = "http://localhost:8000";

	private static final String amazonAWSAccessKey = "Sample";

	private static final String amazonAWSSecretKey = "Sample";

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(setterPrefix = "with")
	@DynamoDBTable(tableName = "WeightTable")
	public static class WeightItem {

		@DynamoDBHashKey
		private String id;
		
		@DynamoDBAttribute
		private String date;

		@DynamoDBAttribute
		private Double value;
	}

	public static AmazonDynamoDB amazonDynamoDb() {
		return AmazonDynamoDBClientBuilder.standard()
				.withCredentials(amazonAWSCredentials())
				.withEndpointConfiguration(new AwsClientBuilder
						.EndpointConfiguration("http://localhost:8000", "us-west-1"))
				.build();
	}

	public static AWSCredentialsProvider amazonAWSCredentials() {
		return new AWSStaticCredentialsProvider(new 
				BasicAWSCredentials(amazonAWSAccessKey, 
						amazonAWSSecretKey));
	}

	public static void main(String args[]) throws Exception {

		AmazonDynamoDB amazonDynamoDb = amazonDynamoDb();

		DynamoDBMapper dynamoMapper = new DynamoDBMapper(amazonDynamoDb);

	}
}
