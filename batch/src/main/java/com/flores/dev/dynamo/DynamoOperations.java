package com.flores.dev.dynamo;

import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;

public interface DynamoOperations {

	public CreateTableRequest createTable();
	
}
