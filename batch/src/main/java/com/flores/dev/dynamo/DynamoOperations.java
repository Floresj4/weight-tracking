package com.flores.dev.dynamo;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;

public abstract class DynamoOperations {

	private final DynamoDbClient client;
	
	public DynamoOperations(DynamoDbClient client) {
		this.client = client;
	}
	
	public abstract CreateTableRequest createTable();
	
	public DynamoDbClient getClient() {
		return this.client;
	}
}
