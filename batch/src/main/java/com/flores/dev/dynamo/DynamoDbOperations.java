package com.flores.dev.dynamo;

import java.net.URI;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

@Slf4j
public class DynamoDbOperations {

	private static final String LOCAL_DB_ENDPOINT = "http://localhost:8000";
	
	public static void main(String args[]) throws Exception {

		log.info("Initializing DynamoDb client");
		DynamoDbClient client = DynamoDbClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(Region.US_EAST_1)
				.endpointOverride(new URI(LOCAL_DB_ENDPOINT))
				.build();
		
		DynamoOperations operations = new WeightsUsersOperations(client);
		
		CreateTableResponse response = operations.createTable();
		TableDescription description = response.tableDescription();
		log.info("Table description: {}", description.tableStatus());
		
		operations.putItem(args);
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
//	
//	public static void getSingleItem(DynamoDbClient client, String tableName, String userGuid, String entryDate) {
//		Map<String, AttributeValue> item = new HashMap<>();
//
//		AttributeValue guid = AttributeValue.builder()
//				.s(userGuid)
//				.build();
//		
//		item.put(ATTRIBUTE_GUID, guid);
//		AttributeValue entryDateAttr = AttributeValue.builder()
//				.s(entryDate)
//				.build();
//
//		item.put(ATTRIBUTE_ENTRY_DATE, entryDateAttr);
//		
//		GetItemRequest getItemRequest = GetItemRequest.builder()
//				.returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
//				.tableName(tableName)
//				.key(item)
//				.build();
//		
//		GetItemResponse getItemResponse = client.getItem(getItemRequest);
//		ConsumedCapacity consumedCapacity = getItemResponse.consumedCapacity();
//		log.info("GetItem complete.  Comsumed capacity: {}", consumedCapacity);	
//		
//		Map<String, AttributeValue> responseItem = getItemResponse.item();
//		log.info("Returned {}", responseItem);
//	}
//	

//	
//	public static Map<String, AttributeValue> getItemMap(String userGuid, String entryDate, String value) {
//		Map<String, AttributeValue> item = new HashMap<>();
//
//		AttributeValue guid = AttributeValue.builder()
//				.s(userGuid)
//				.build();
//		
//		item.put(ATTRIBUTE_GUID, guid);
//		AttributeValue entryDateAttr = AttributeValue.builder()
//				.s(entryDate)
//				.build();
//
//		item.put(ATTRIBUTE_ENTRY_DATE, entryDateAttr);
//		AttributeValue valueAttr = AttributeValue.builder()
//				.n(value)
//				.build();
//
//		item.put(ATTRIBUTE_VALUE, valueAttr);
//		return item;
//	}
}
