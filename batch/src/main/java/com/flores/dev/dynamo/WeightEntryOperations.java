package com.flores.dev.dynamo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
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
	public String getPartitionKeyName() {
		return ATTRIBUTE_GUID;
	}
	
	@Override
	public String getSortKeyName() {
		return ATTRIBUTE_ENTRY_DATE;
	}

	@Override
	public String getTableName() {
		return "Weights";
	}

	@Override
	public Map<String, AttributeValue> getItemMap(String[] args) {
		log.info("Parsing WeightEntryCommand arguments for item map");
		WeightEntryCommand command = new WeightEntryCommand();
		
		JCommander.newBuilder()
		.addCommand(command)
		.build()
		.parse(args);
		
		Map<String, AttributeValue> item = new HashMap<>();
		item.put(ATTRIBUTE_GUID, AttributeValue.builder()
				.s(command.getGuid())
				.build());
		
		item.put(ATTRIBUTE_ENTRY_DATE, AttributeValue.builder()
				.s(command.getDate())
				.build());

		item.put(ATTRIBUTE_VALUE, AttributeValue.builder()
				.n(command.getValue())
				.build());

		return item;
	}
	
	@Data
	@Parameters(separators = "=")
	public static class WeightEntryCommand {
		
		@Parameter(names = "guid", required = true)
		private String guid;
		
		@Parameter(names = "date", required = true)
		private LocalDate date;
		
		@Parameter(names = "value", required = true)
		private double value;
		
		public String getDate() {
			return date.toString();
		}
		
		public String getValue() {
			return String.valueOf(value);
		}
	}

}
