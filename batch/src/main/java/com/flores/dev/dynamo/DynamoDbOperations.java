package com.flores.dev.dynamo;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbResponseMetadata;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

@Slf4j
public class DynamoDbOperations {
	
	public static void main(String args[]) throws Exception {

		//parse entrypoint arguments before passing to the specific operation
		DynamoDbOperationsCommand command = new DynamoDbOperationsCommand();
		JCommander.newBuilder()
		.addCommand(command)
		.build()
		.parse(args);

		DynamoOperations operations = DynamoDbOperationFactory.getOperationHandler(
				command.getOperation(),
				command.isUseLocalConnection());

		CreateTableResponse createResponse = operations.createTable();
		TableDescription description = createResponse.tableDescription();
		log.info("Table description: {}", description.tableStatus());

		PutItemResponse putResponse = operations.putItem(args);
		DynamoDbResponseMetadata responseMetadata = putResponse.responseMetadata();
		log.info("Response metadata: {}", responseMetadata.toString());
	}
	
	@Data
	@Parameters(separators = "=")
	public static class DynamoDbOperationsCommand {
		
		@Parameter(names = "operation", required = true)
		private SupportedOperations operation;
		
		@Parameter(names = "useLocalConnection", required = false,
				description = "Use local DB connection")
		private boolean useLocalConnection;
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
