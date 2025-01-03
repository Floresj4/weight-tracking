package com.flores.dev.dynamo;

import java.net.URI;
import java.time.Instant;

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
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Slf4j
public class SpringDynamo {

	private static final String LOCAL_DB_ENDPOINT = "http://localhost:8000";

	private static final String AMAZON_AWS_ACCESS_KEY = "Sample";

	private static final String AMAZON_AWS_SECRET_KEY = "Sample";

	public static CreateTableRequest createWeightTableRequest() {
		log.info("Creating Weight table request");

		//define table attributes
		String guid = "guid";
		AttributeDefinition guidAttribute = AttributeDefinition.builder()
				.attributeType(ScalarAttributeType.S)
				.attributeName(guid)
				.build();
		
		String entryDate = "entry-date";
		AttributeDefinition dateAttribute = AttributeDefinition.builder()
				.attributeType(ScalarAttributeType.S)
				.attributeName(entryDate)
				.build();

		//define table primary key attributes
		KeySchemaElement partitionKey = KeySchemaElement.builder()
				.keyType(KeyType.HASH)
				.attributeName(guid)
				.build();
		
		KeySchemaElement sortKey = KeySchemaElement.builder()
				.keyType(KeyType.RANGE)
				.attributeName(entryDate)
				.build();
		
		String tableName = "Weights";
		return CreateTableRequest.builder()
				.attributeDefinitions(guidAttribute, dateAttribute)
				.keySchema(partitionKey, sortKey)
				.provisionedThroughput(ProvisionedThroughput.builder()
						.readCapacityUnits(5L)
						.writeCapacityUnits(5L)
						.build())
				.tableName(tableName)
				.build();	
	}
	
	public static void main(String args[]) throws Exception {

		DynamoDbClient client = DynamoDbClient.builder()
				.credentialsProvider(StaticCredentialsProvider
						.create(AwsBasicCredentials.create(AMAZON_AWS_ACCESS_KEY, 
								AMAZON_AWS_SECRET_KEY)))
				.region(Region.US_EAST_1)
				.endpointOverride(new URI(LOCAL_DB_ENDPOINT))
				.build();
		
		DynamoDbWaiter waiter = client.waiter();


		
		try {
			String tableName = "Weights";
			DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
					.tableName(tableName)
					.build();

			try {
				//check if the table exists first
				DescribeTableResponse describeTableResponse = client.describeTable(describeTableRequest);
				
				Instant creationDate = describeTableResponse.table().creationDateTime();
				log.info("{} table created {}", tableName, creationDate);
			}
			catch(ResourceNotFoundException e) {
				log.info("Table {} does not exist.  Creating...", tableName);
				
				CreateTableRequest createTableRequest = createWeightTableRequest();
				CreateTableResponse createTableResponse = client.createTable(createTableRequest);
				
				//wait until DynamoDb is finished
				WaiterResponse<DescribeTableResponse> waiterResponse = waiter.waitUntilTableExists(describeTableRequest);
				waiterResponse.matched()
						.response()
						.ifPresent(System.out::println);;
				
				String newTableName = createTableResponse.tableDescription()
						.tableName();

				log.info("{} created.", newTableName);
			}
		}
		catch(DynamoDbException e) {
			log.error(e.getMessage());
		}
	}
}
