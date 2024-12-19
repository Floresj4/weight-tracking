package com.flores.dev.dynamo;

import java.net.URI;
import java.util.Optional;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Slf4j
public class SpringDynamo {

	private static final String LOCAL_DB_ENDPOINT = "http://localhost:8000";

	private static final String AMAZON_AWS_ACCESS_KEY = "Sample";

	private static final String AMAZON_AWS_SECRET_KEY = "Sample";

	public static void main(String args[]) throws Exception {

		DynamoDbClient client = DynamoDbClient.builder()
				.credentialsProvider(StaticCredentialsProvider
						.create(AwsBasicCredentials.create(AMAZON_AWS_ACCESS_KEY, 
								AMAZON_AWS_SECRET_KEY)))
				.region(Region.US_EAST_1)
				.endpointOverride(new URI(LOCAL_DB_ENDPOINT))
				.build();
		
		DynamoDbWaiter waiter = client.waiter();
		
		String tableName = "guids";
		String key = "id";
		
		CreateTableRequest createTable = CreateTableRequest.builder()
				.attributeDefinitions(AttributeDefinition.builder()
						.attributeName(key)
						.attributeType(ScalarAttributeType.S)
						.build())
				.keySchema(KeySchemaElement.builder()
						.attributeName(key)
						.keyType(KeyType.HASH)
						.build())
				.provisionedThroughput(ProvisionedThroughput.builder()
						.readCapacityUnits(10L)
						.writeCapacityUnits(10L)
						.build())
				.tableName(tableName)
				.build();
		
		try {
			CreateTableResponse response = client.createTable(createTable);
			DescribeTableRequest describeTable = DescribeTableRequest.builder()
					.tableName(tableName)
					.build();
			
			//wait until DynamoDb is finished
			WaiterResponse<DescribeTableResponse> waiterResponse = waiter.waitUntilTableExists(describeTable);
			waiterResponse.matched()
					.response()
					.ifPresent(System.out::println);;
			
			String newTableName = response.tableDescription().tableName();
			System.out.println(newTableName);
		}
		catch(DynamoDbException e) {
			log.error(e.getMessage());
		}
	}
}
