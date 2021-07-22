package sls.dynamodb.enhancedclient.demo.data.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

public interface DynamoDbItem {
    Map<String, AttributeValue> asDynamoDbJson() throws JsonProcessingException;
}
