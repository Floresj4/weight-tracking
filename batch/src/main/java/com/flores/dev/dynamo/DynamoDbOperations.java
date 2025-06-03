package com.flores.dev.dynamo;

import java.net.URI;
import java.net.URISyntaxException;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbResponseMetadata;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

@Slf4j
public class DynamoDbOperations {

	private static final String LOCAL_DB_ENDPOINT = "http://localhost:8000";
	
	public static DynamoDbClient getDynamoDbClient() throws URISyntaxException {
		log.info("Initializing DynamoDb client");
		return DynamoDbClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(Region.US_EAST_1)
				.endpointOverride(new URI(LOCAL_DB_ENDPOINT))
				.build();
	}
	
	public static void main(String args[]) throws Exception {

		DynamoDbClient client = getDynamoDbClient();	
		DynamoOperations operations = new WeightsUsersOperations(client);

		CreateTableResponse createResponse = operations.createTable();
		TableDescription description = createResponse.tableDescription();
		log.info("Table description: {}", description.tableStatus());

		PutItemResponse putResponse = operations.putItem(args);
		DynamoDbResponseMetadata responseMetadata = putResponse.responseMetadata();
		log.info("Response metadata: {}", responseMetadata.toString());
	}

	public enum Operations {
		USER,
		ENTRIES;
	}
	
	@Data
	@Parameters(separators = "=")
	public static class DynamoDbOperationsCommand {
		
		@Parameter(names = "operation", required = true)
		private Operations operation;
	}

//	public static void batchItemRequest(DynamoDbClient client, String tableName, String userGuid) {
//		Map<String, List<WriteRequest>> requestItems = new HashMap<>();
//		
//		int numberOfEntries = RANDOM.nextInt(25);
//		log.info("Attempting to add {} entries", numberOfEntries);
//		
//		for(int i = 0; i < numberOfEntries; i++) {			
//			Map<String, AttributeValue> item = getItemMap(userGuid);
//			
//			List<WriteRequest> putItems = requestItems.getOrDefault(tableName, new ArrayList<>());
//			PutRequest put = PutRequest.builder()
//					.item(item)
//					.build();
//
//			putItems.add(WriteRequest.builder()
//					.putRequest(put)
//					.build());
//			
//			requestItems.put(tableName, putItems);
//		}
//		
//		BatchWriteItemRequest batchItemRequest = BatchWriteItemRequest.builder()
//				.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
//				.requestItems(requestItems)
//				.build();
//		
//		BatchWriteItemResponse batchItemResponse = client.batchWriteItem(batchItemRequest);
//		List<ConsumedCapacity> consumedCapacity = batchItemResponse.consumedCapacity();
//
//		String capacity = consumedCapacity.stream()
//				.map(String::valueOf)
//				.collect(Collectors.joining(
//						System.lineSeparator()));
//		
//		log.info("BatchWriteItemRequest complete.  Consumed capacity: {}", capacity);
//	}
}
