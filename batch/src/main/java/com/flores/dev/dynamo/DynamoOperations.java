package com.flores.dev.dynamo;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConsumedCapacity;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Slf4j
public abstract class DynamoOperations {

	private final DynamoDbClient client;

	public DynamoOperations(DynamoDbClient client) {
		this.client = client;
	}

	public CreateTableResponse createTable() {
		try {
			String tableName = getTableName();
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
				
				CreateTableRequest createTableRequest = getCreateTableRequest();
				CreateTableResponse createTableResponse = client.createTable(createTableRequest);
				
				//wait until DynamoDb is finished
				DynamoDbWaiter waiter = client.waiter();
				WaiterResponse<DescribeTableResponse> waiterResponse = waiter.waitUntilTableExists(describeTableRequest);
				waiterResponse.matched().response()
					.map(String::valueOf)
					.ifPresent(log::info);

				return createTableResponse;
			}		
			
		}
		catch(DynamoDbException e) {
			log.error(e.getMessage());
		}	
		
		return null;
	}
	
	public DynamoDbClient getClient() {
		return this.client;
	}

	public abstract CreateTableRequest getCreateTableRequest();

	public abstract Map<String, AttributeValue> getItemMap(String[] args);
	
	public PutItemRequest getPutItemRequest(Map<String, AttributeValue> item) {
		return PutItemRequest.builder()
				.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
				.tableName(getTableName())
				.item(item)
				.build();
	}
	
	public abstract String getPartitionKeyName();
	
	public abstract String getSortKeyName();

	public abstract String getTableName();
	
	public PutItemResponse putItem(String[] args) {
	
		Map<String, AttributeValue> itemMap = getItemMap(args);
		PutItemRequest putItemRequest = getPutItemRequest(itemMap);

		return client.putItem(putItemRequest);
	}

	public Map<String, AttributeValue> getItem(String partitionKey, String sortKey) {
		Map<String, AttributeValue> item = new HashMap<>();

		item.put(getPartitionKeyName(), AttributeValue.builder()
				.s(partitionKey)
				.build());
		
		item.put(getSortKeyName(), AttributeValue.builder()
				.s(sortKey)
				.build());

		GetItemRequest getItemRequest = GetItemRequest.builder()
				.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
				.tableName(getTableName())
				.key(item)
				.build();
		
		GetItemResponse getItemResponse = client.getItem(getItemRequest);
		ConsumedCapacity consumedCapacity = getItemResponse.consumedCapacity();
		log.info("GetItem complete.  Comsumed capacity: {}", consumedCapacity);	

		return getItemResponse.item();
	}
}
