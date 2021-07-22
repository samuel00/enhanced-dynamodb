package sls.dynamodb.enhancedclient.demo.data.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import sls.dynamodb.enhancedclient.demo.data.repositories.DynamoDbItem;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DynamoDbBean
public class SMSData implements DynamoDbItem {

    private String uuid;

    private String idCliente;

    private String dataInsercao;

    private String sigla;

    private List<HistoricoData> historico;

    @DynamoDbAttribute("historico")
    public List<HistoricoData> getHistorico() {
        return historico;
    }

    public void setHistorico(List<HistoricoData> historico) {
        this.historico = historico;
    }

    @DynamoDbAttribute("id_cliente")
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    //@DynamoDbSortKey
    //@DynamoDbAttribute("DataInsercao")
    @DynamoDbAttribute("data_insercao")
    public String getDataInsercao() {
        return dataInsercao;
    }

    public void setDataInsercao(String dataInsercao) {
        this.dataInsercao = dataInsercao;
    }

    @DynamoDbAttribute("sigla")
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public static AttributeValue buildPartitionKey(String id) {
        return AttributeValue.builder().s("Cliente#" + id).build();
    }

    @Override
    public Map<String, AttributeValue> asDynamoDbJson() throws JsonProcessingException {
        Map<String, AttributeValue> map = new HashMap<>();
        map.put("PK", AttributeValue.builder().s(this.uuid).build());
        map.put("id_notificacao", AttributeValue.builder().s("BeanlessItem").build());
        map.put("data_insercao", AttributeValue.builder().s(this.dataInsercao).build());
        map.put("id_cliente", AttributeValue.builder().s(this.idCliente).build());
        map.put("sigla", AttributeValue.builder().s(this.sigla).build());
        map.put("historico", AttributeValue.builder().s(MAPPER.writeValueAsString(this.historico)).build());
        return map;
    }

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
}
