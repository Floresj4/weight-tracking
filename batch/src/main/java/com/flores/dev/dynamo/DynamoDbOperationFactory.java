package com.flores.dev.dynamo;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Slf4j
public final class DynamoDbOperationFactory {

	private static final String LOCAL_DB_ENDPOINT = "http://localhost:8000";
	
	public static DynamoDbClient getDynamoDbClient(boolean useLocalEndpoint) throws URISyntaxException {
		log.info("Initializing DynamoDb client");
		return DynamoDbClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(Region.US_EAST_1)
				.endpointOverride(new URI(LOCAL_DB_ENDPOINT))
				.build();
	}
	
	public static DynamoOperations getOperationHandler(SupportedOperations operation, boolean useLocalEndpoint) throws Exception {
		DynamoOperations operations = null;
		DynamoDbClient client = null;
		
		switch(operation) {
			case ENTRIES:
				log.info("Initializing Entry operations");
				client = getDynamoDbClient(useLocalEndpoint);
				operations = new WeightEntryOperations(client);
				break;

			case USERS:
				log.info("Initializing User operations");
				client = getDynamoDbClient(useLocalEndpoint);
				operations = new WeightsUsersOperations(client);
				break;
				
			default:
				log.error("Unsupported operation {}", operation);
				throw new UnsupportedOperationException(String
						.valueOf(operation));
		}
		
		return operations;
	}
}
