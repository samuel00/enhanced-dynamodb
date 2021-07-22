package sls.dynamodb.enhancedclient.demo.data.repositories;

import software.amazon.awssdk.services.dynamodb.model.*;

class DynamoDbTestUtil {

    private DynamoDbTestUtil() {
    }

    static CreateTableRequest createTableRequest(String tableName) {
        KeySchemaElement pk = KeySchemaElement.builder().attributeName("PK").keyType(KeyType.HASH).build();
        AttributeDefinition pkDef = AttributeDefinition.builder().attributeName("PK").attributeType(ScalarAttributeType.S).build();
       // KeySchemaElement sk = KeySchemaElement.builder().attributeName("SK").keyType(KeyType.RANGE).build();
        //AttributeDefinition skDef = AttributeDefinition.builder().attributeName("SK").attributeType(ScalarAttributeType.S).build();
        return CreateTableRequest.builder()
            .tableName(tableName)
            .keySchema(pk)
            .attributeDefinitions(pkDef)
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build();
    }

}
