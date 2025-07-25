package com.flores.dev.dynamo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
public class WeightsUsersOperations extends DynamoOperations {

	public static final String ATTRIBUTE_GUID = "guid";	
	public static final String ATTRIBUTE_FIRST_NAME = "first-name";
	public static final String ATTRIBUTE_LAST_NAME = "last-name";
	public static final String ATTRIBUTE_YEARS = "years";
	
	public WeightsUsersOperations(DynamoDbClient client) {
		super(client);
	}
	
	@Override
	public CreateTableRequest getCreateTableRequest() {
		log.info("Creating Weights Users table request");
		
		AttributeDefinition guidAttribute = AttributeDefinition.builder()
				.attributeType(ScalarAttributeType.S)
				.attributeName(ATTRIBUTE_GUID)
				.build();

		AttributeDefinition lastNameAttribute = AttributeDefinition.builder()
				.attributeType(ScalarAttributeType.S)
				.attributeName(ATTRIBUTE_LAST_NAME)
				.build();
		
		//define table primary key attributes
		KeySchemaElement partitionKey = KeySchemaElement.builder()
				.keyType(KeyType.HASH)
				.attributeName(ATTRIBUTE_GUID)
				.build();
		
		KeySchemaElement sortKey = KeySchemaElement.builder()
				.keyType(KeyType.RANGE)
				.attributeName(ATTRIBUTE_LAST_NAME)
				.build();
		
		String tableName = "WeightsUsers";
		return CreateTableRequest.builder()
				.attributeDefinitions(guidAttribute, lastNameAttribute)
				.keySchema(partitionKey, sortKey)
				.provisionedThroughput(ProvisionedThroughput.builder()
						.readCapacityUnits(5L)
						.writeCapacityUnits(5L)
						.build())
				.tableName(tableName)
				.build();
	}

	public Map<String, AttributeValue> getItemMap(String args[]) {
		log.info("Parsing WeightUserCommand arguments for item map");
		WeightsUsersCommand command = new WeightsUsersCommand();
		
		//parse commandline arguments
		JCommander.newBuilder()
		.acceptUnknownOptions(true)
		.addObject(command)
		.build()
		.parse(args);

		Map<String, AttributeValue> item = new HashMap<>();
		item.put(ATTRIBUTE_GUID, AttributeValue.builder()
				.s(command.getGuid())
				.build());

		item.put(ATTRIBUTE_FIRST_NAME, AttributeValue.builder()
				.s(command.getFirstname())
				.build());

		item.put(ATTRIBUTE_LAST_NAME, AttributeValue.builder()
				.s(command.getLastname())
				.build());

		List<Integer> years = command.getYears();
		if(!years.isEmpty()) {

			//convert to a sorted collection
			Collection<String> numberSet = years.stream()
					.map(String::valueOf)
					.collect(Collectors.toCollection(TreeSet::new));
			
			item.put(ATTRIBUTE_YEARS, AttributeValue.builder()
					.ns(numberSet)
					.build());
		}

		return item;
	}

	public String getSortKeyName() {
		return ATTRIBUTE_LAST_NAME;				
	}
	
	public String getTableName() {
		return "WeightsUsers";
	}

	public String getPartitionKeyName() {
		return ATTRIBUTE_GUID;
	}
	
	@Data
	@Parameters(separators = "=")
	public static class WeightsUsersCommand {

		@Parameter(names = "guid", required = true)
		private String guid;
		
		@Parameter(names = "firstname", required = true)
		private String firstname;
		
		@Parameter(names = "lastname", required = true)
		private String lastname;
		
		@Parameter(names = "years")
		private List<Integer> years = new ArrayList<>();
	}
}
