package com.flores.dev.dynamo;

import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
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
	
	public abstract String getTableName();

//	String userGuid = UUID.randomUUID()
//			.toString();
//
//	String entryDate = "2024-01-04";
//	
//	putSingleItem(client, tableName, userGuid, entryDate);
//
//	getSingleItem(client, tableName, userGuid, entryDate);
//	
//	batchItemRequest(client, tableName, userGuid);
}
