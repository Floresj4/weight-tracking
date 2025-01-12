package com.flores.dev.dynamo;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ConsumedCapacity;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.WriteRequest;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Slf4j
public class SpringDynamo {

	public static final Random RANDOM = new Random(System.currentTimeMillis());
	
	public static final String ATTRIBUTE_GUID = "guid";
	public static final String ATTRIBUTE_ENTRY_DATE = "entry-date";
	public static final String ATTRIBUTE_VALUE = "value";

	private static final String LOCAL_DB_ENDPOINT = "http://localhost:8000";

	private static final String AMAZON_AWS_ACCESS_KEY = "Sample";

	private static final String AMAZON_AWS_SECRET_KEY = "Sample";

	public static CreateTableRequest createWeightTableRequest() {
		log.info("Creating Weight table request");

		//define table attributes
		AttributeDefinition guidAttribute = AttributeDefinition.builder()
				.attributeType(ScalarAttributeType.S)
				.attributeName(ATTRIBUTE_GUID)
				.build();
		
		AttributeDefinition dateAttribute = AttributeDefinition.builder()
				.attributeType(ScalarAttributeType.S)
				.attributeName(ATTRIBUTE_ENTRY_DATE)
				.build();

		//define table primary key attributes
		KeySchemaElement partitionKey = KeySchemaElement.builder()
				.keyType(KeyType.HASH)
				.attributeName(ATTRIBUTE_GUID)
				.build();
		
		KeySchemaElement sortKey = KeySchemaElement.builder()
				.keyType(KeyType.RANGE)
				.attributeName(ATTRIBUTE_ENTRY_DATE)
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
				DynamoDbWaiter waiter = client.waiter();
				WaiterResponse<DescribeTableResponse> waiterResponse = waiter.waitUntilTableExists(describeTableRequest);
				waiterResponse.matched()
						.response()
						.ifPresent(System.out::println);;
				
				String newTableName = createTableResponse.tableDescription()
						.tableName();

				log.info("{} created.", newTableName);
			}
			
			String userGuid = UUID.randomUUID()
					.toString();

			String entryDate = "2024-01-04";
			
			putSingleItem(client, tableName, userGuid, entryDate);

			getSingleItem(client, tableName, userGuid, entryDate);
			
			batchItemRequest(client, tableName, userGuid);
			
		}
		catch(DynamoDbException e) {
			log.error(e.getMessage());
		}
	}

	public static void batchItemRequest(DynamoDbClient client, String tableName, String userGuid) {
		Map<String, List<WriteRequest>> requestItems = new HashMap<>();
		
		int numberOfEntries = RANDOM.nextInt(25);
		log.info("Attempting to add {} entries", numberOfEntries);
		
		for(int i = 0; i < numberOfEntries; i++) {			
			Map<String, AttributeValue> item = getItemMap(userGuid);
			
			List<WriteRequest> putItems = requestItems.getOrDefault(tableName, new ArrayList<>());
			PutRequest put = PutRequest.builder()
					.item(item)
					.build();

			putItems.add(WriteRequest.builder()
					.putRequest(put)
					.build());
			
			requestItems.put(tableName, putItems);
		}
		
		BatchWriteItemRequest batchItemRequeust = BatchWriteItemRequest.builder()
				.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
				.requestItems(requestItems)
				.build();
		
		BatchWriteItemResponse batchItemResponse = client.batchWriteItem(batchItemRequeust);
		List<ConsumedCapacity> consumedCapacity = batchItemResponse.consumedCapacity();

		String capacity = consumedCapacity.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(
						System.lineSeparator()));
		
		log.info("BatchWriteItemRequest complete.  Consumed capacity: {}", capacity);

	}
	
	public static void getSingleItem(DynamoDbClient client, String tableName, String userGuid, String entryDate) {
		Map<String, AttributeValue> item = new HashMap<>();

		AttributeValue guid = AttributeValue.builder()
				.s(userGuid)
				.build();
		
		item.put(ATTRIBUTE_GUID, guid);
		AttributeValue entryDateAttr = AttributeValue.builder()
				.s(entryDate)
				.build();

		item.put(ATTRIBUTE_ENTRY_DATE, entryDateAttr);
		
		GetItemRequest getItemRequest = GetItemRequest.builder()
				.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
				.tableName(tableName)
				.key(item)
				.build();
		
		GetItemResponse getItemResponse = client.getItem(getItemRequest);
		ConsumedCapacity consumedCapacity = getItemResponse.consumedCapacity();
		log.info("GetItem complete.  Comsumed capacity: {}", consumedCapacity);	
		
		Map<String, AttributeValue> responseItem = getItemResponse.item();
		log.info("Returned {}", responseItem);
	}
	
	public static void putSingleItem(DynamoDbClient client, String tableName, String userGuid, String entryDate) {
		//create a single item to put
		Map<String, AttributeValue> item = getItemMap(userGuid, entryDate, "159.0");			
		PutItemRequest putItemRequest = PutItemRequest.builder()
				.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
				.tableName(tableName)
				.item(item)
				.build();

		PutItemResponse putItemResponse = client.putItem(putItemRequest);
		ConsumedCapacity consumedCapacity = putItemResponse.consumedCapacity();
		log.info("PutItem complete.  Comsumed capacity: {}", consumedCapacity);
	}
	
	public static Map<String, AttributeValue> getItemMap(String userGuid) {
		String date = LocalDate.of(
				RANDOM.nextInt(2022, 2024),
				RANDOM.nextInt(1, 12),
				RANDOM.nextInt(1, 28))
				.toString();
		
		String value = String.valueOf(RANDOM.nextInt(150, 200));
		
		return getItemMap(userGuid, date, value);
	}
	
	public static Map<String, AttributeValue> getItemMap(String userGuid, String entryDate, String value) {
		Map<String, AttributeValue> item = new HashMap<>();

		AttributeValue guid = AttributeValue.builder()
				.s(userGuid)
				.build();
		
		item.put(ATTRIBUTE_GUID, guid);
		AttributeValue entryDateAttr = AttributeValue.builder()
				.s(entryDate)
				.build();

		item.put(ATTRIBUTE_ENTRY_DATE, entryDateAttr);
		AttributeValue valueAttr = AttributeValue.builder()
				.n(value)
				.build();

		item.put(ATTRIBUTE_VALUE, valueAttr);
		return item;
	}
}
