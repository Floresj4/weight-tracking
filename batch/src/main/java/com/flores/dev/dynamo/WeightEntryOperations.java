package com.flores.dev.dynamo;

import java.util.Map;

import com.beust.jcommander.Parameters;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@Slf4j
public class WeightEntryOperations extends DynamoOperations {

	public static final String ATTRIBUTE_GUID = "guid";
	public static final String ATTRIBUTE_ENTRY_DATE = "entry-date";
	public static final String ATTRIBUTE_VALUE = "value";
	
	public WeightEntryOperations(DynamoDbClient client) {
		super(client);
	}

	public CreateTableRequest getCreateTableRequest() {
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

	@Override
	public String getTableName() {
		return "Weights";
	}

	@Override
	public Map<String, AttributeValue> getItemMap(String[] args) {
		return null;
	}
	
	@Data
	@Parameters(separators = "=")
	public static class WeightentryCommand {
		private String guid;
		private String date;
		private double value;
	}

}
