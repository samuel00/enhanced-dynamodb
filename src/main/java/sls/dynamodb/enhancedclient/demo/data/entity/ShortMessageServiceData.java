package sls.dynamodb.enhancedclient.demo.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sls.dynamodb.enhancedclient.demo.data.repositories.DynamoDbItem;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDbBean
public class ShortMessageServiceData implements DynamoDbItem {

    private String cpf;

    private String uuid;

    private Instant dataInsercao;

    private String sigla;

    public static AttributeValue buildPartitionKey(String id) {
        return AttributeValue.builder().s("CLIENTE#" + id).build();
    }

    public static AttributeValue buildSortKey() {
        return AttributeValue.builder().s(Instant.now().minus(5, ChronoUnit.DAYS).toString()).build();
    }

    public static Key buildKeyForId(String id) {
        return Key.builder().partitionValue(buildPartitionKey(id)).sortValue(buildSortKey()).build();
    }

    public static ShortMessageServiceData fromMap(Map<String, AttributeValue> item) {
        return ShortMessageServiceData.builder()
                .cpf("93277776204")
                .dataInsercao(Instant.now())
                .sigla("FZ3")
                .build();
    }

    @Override
    public Map<String, AttributeValue> asDynamoDbJson() {
        Map<String, AttributeValue> map = new HashMap<>();
        map.put("PK", buildPartitionKey(this.cpf));
        map.put("SK", buildSortKey());
        map.put("Type", AttributeValue.builder().s("ShortMessageServiceItem").build());
        map.put("Id", AttributeValue.builder().s(this.cpf).build());
        map.put("Sigla", AttributeValue.builder().s(this.sigla).build());
        map.put("DataInsercao", AttributeValue.builder().s(this.dataInsercao.toString()).build());
        return map;
    }


    @DynamoDbPartitionKey
    @DynamoDbAttribute("ClienteID")
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("Data")
    public Instant getDataInsercao() {
        return dataInsercao;
    }

    public void setDataInsercao(Instant dataInsercao) {
        this.dataInsercao = dataInsercao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
