package sls.dynamodb.enhancedclient.demo.data.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Repository;
import sls.dynamodb.enhancedclient.demo.data.entity.HistoricoData;
import sls.dynamodb.enhancedclient.demo.data.entity.SMSData;
import sls.dynamodb.enhancedclient.demo.data.entity.ShortMessageServiceData;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Repository
public class ShortMessageRepository {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public ShortMessageRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = "Mensageria";
        this.dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    public static Map<String, AttributeValue> keyAsMap(Key key) {
        return new HashMap<String, AttributeValue>() {{
            put("PK", key.partitionKeyValue());
            put("SK", key.sortKeyValue().get());
        }};
    }

    public void insertItem(DynamoDbItem item) throws JsonProcessingException {
        Map<String, AttributeValue> attributes = item.asDynamoDbJson();
        PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(attributes).build();
        dynamoDbClient.putItem(request);
    }

    public void insertItem(final SMSData item){
        DynamoDbTable<SMSData> mensageriaTable = dynamoDbEnhancedClient.table(tableName,  TableSchema.fromBean(SMSData.class));
        mensageriaTable.putItem(item);
    }

    private DynamoDbTable<ShortMessageServiceData> getTable() {
        DynamoDbTable<ShortMessageServiceData> table =  dynamoDbEnhancedClient.table(tableName,  TableSchema.fromBean(ShortMessageServiceData.class));
        return table;
    }

    public static AttributeValue buildPartitionKey(String id) {
        return AttributeValue.builder().s("CLIENTE#" + id).build();
    }

    public Map<String, AttributeValue> getShortMessageSericeRaw(String id) {
        Key key = ShortMessageServiceData.buildKeyForId(id);
        Map<String, AttributeValue> keyMap = keyAsMap(key);
        GetItemRequest request = GetItemRequest.builder().tableName(tableName).key(keyMap).build();
        GetItemResponse response = dynamoDbClient.getItem(request);
        return response.item();
    }

    public SMSData getOrder(final String customerID, final String orderID) {
        DynamoDbTable<SMSData> orderTable = dynamoDbEnhancedClient.table(tableName,  TableSchema.fromBean(SMSData.class));
        // Construct the key with partition and sort key
        Key key = Key.builder().partitionValue(customerID)
                .sortValue(orderID)
                .build();

        SMSData order = orderTable.getItem(key);

        return order;
    }

    public void insertItemWithEmptyList(DynamoDbItem item) throws JsonProcessingException {
        Map<String, AttributeValue> attributes = item.asDynamoDbJson();
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(attributes)
                .build();
        try {
            PutItemResponse response = dynamoDbClient.putItem(putItemRequest);
            //log.info("response: {}", response);
            System.out.println(response);

        } catch (ConditionalCheckFailedException e) {
            //log.error("Condition check failed, item didn't exist", e);
            System.out.println(e);
        }
    }

    public void insertValueAtIndex(String id, int i, AttributeValue value) {
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(new HashMap<String, AttributeValue>() {{ put("PK", AttributeValue.builder().s(id).build());}})
                .updateExpression(String.format("SET #list[%s] = :value", i))
                .conditionExpression("attribute_exists(PK)")
                .expressionAttributeNames(new HashMap<String, String>() {{ put("#list", "List");}})
                .expressionAttributeValues(new HashMap<String, AttributeValue>() {{ put(":value", value);}})
                .build();
        try {
            UpdateItemResponse response = dynamoDbClient.updateItem(updateItemRequest);
            //log.info("response: {}", response);
            System.out.println(response);

        } catch (ConditionalCheckFailedException e) {
            //log.error("Condition check failed, item didn't exist", e);
            System.out.println(e);
        }
    }

    public void appendValuesToList(String id, AttributeValue... values) {
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(new HashMap<String, AttributeValue>() {{ put("PK", AttributeValue.builder().s(id).build());}})
                .updateExpression("SET #list = list_append(#list, :values)")
                .conditionExpression("attribute_exists(PK)")
                .expressionAttributeNames(new HashMap<String, String>() {{ put("#list", "List");}})
                .expressionAttributeValues(new HashMap<String, AttributeValue>() {{ put(":values", AttributeValue.builder().l(values).build());}})
                .build();
        try {
            UpdateItemResponse response = dynamoDbClient.updateItem(updateItemRequest);
            //log.info("response: {}", response);
            System.out.println(response);

        } catch (ConditionalCheckFailedException e) {
            //log.error("Condition check failed, item didn't exist", e);
            System.out.println(e);
        }
    }


    public SMSData getSMSData(final String id) {
        DynamoDbTable<SMSData> orderTable = dynamoDbEnhancedClient.table(tableName,  TableSchema.fromBean(SMSData.class));
        // Construct the key with partition and sort key
        Key key = Key.builder().partitionValue(id)
                .build();

        SMSData order = orderTable.getItem(key);

        return order;
    }

    public void appendValuesToList2(String id, AttributeValue value) {
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(new HashMap<String, AttributeValue>() {{ put("PK", AttributeValue.builder().s(id).build());}})
                .updateExpression("SET historico = list_append(historico, :values)")
                .conditionExpression("attribute_exists(PK)")
                .expressionAttributeValues(Collections.singletonMap(":values", value))
                .build();
        try {
            UpdateItemResponse response = dynamoDbClient.updateItem(updateItemRequest);
            //log.info("response: {}", response);
            System.out.println(response);

        } catch (ConditionalCheckFailedException e) {
            //log.error("Condition check failed, item didn't exist", e);
            System.out.println(e);
        }
    }

    public void insertItemSMSData(DynamoDbItem item) throws JsonProcessingException {
        Map<String, AttributeValue> attributes = item.asDynamoDbJson();
        PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(attributes).build();
        dynamoDbClient.putItem(request);
    }

    public Map<String, AttributeValue> getItemWithList(String id) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(new HashMap<String, AttributeValue>() {{ put("PK", AttributeValue.builder().s(id).build());}})
                .build();
        return dynamoDbClient.getItem(request).item();
    }

    public PageIterable<SMSData> getShortMessageSericeRaw2(String id) {
        Key key = Key.builder().partitionValue(id).sortValue("A").build();
        Key toKey = Key.builder().partitionValue(id).sortValue(String.valueOf(LocalDateTime.now())).build();
        QueryConditional sortValueInBetween = QueryConditional.sortLessThan(toKey);
        DynamoDbTable<SMSData> mensageriaTable = dynamoDbEnhancedClient.table(tableName,  TableSchema.fromBean(SMSData.class));
        PageIterable<SMSData> results   = mensageriaTable.query(r -> r.queryConditional(sortValueInBetween));
        //PageIterable<SMSData> results = mensageriaTable.query(sortValueInBetween).items().iterator();
        return  results;

        /*DynamoDbTable<SMSData> mensageriaTable = dynamoDbEnhancedClient.table(tableName,  TableSchema.fromBean(SMSData.class));

        AttributeValue attributeValue = AttributeValue.builder()
                .n(String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime()))
                .build();

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":value", attributeValue);

        Expression expression = Expression.builder()
                .expression("dataInsercao < :value")
                .expressionValues(expressionValues)
                .build();

        // Create a QueryConditional object that is used in
        // the query operation
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(id)
                        .build());

        // Get items in the Customer table and write out the ID value
        PageIterable<SMSData> results =  mensageriaTable.query(r -> r.queryConditional(queryConditional)
                                .filterExpression(expression));*/
    }

}
