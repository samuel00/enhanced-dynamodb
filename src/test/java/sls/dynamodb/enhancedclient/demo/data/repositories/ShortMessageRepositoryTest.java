package sls.dynamodb.enhancedclient.demo.data.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import sls.dynamodb.enhancedclient.demo.data.entity.HistoricoData;
import sls.dynamodb.enhancedclient.demo.data.entity.SMSData;
import sls.dynamodb.enhancedclient.demo.data.entity.ShortMessageServiceData;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static sls.dynamodb.enhancedclient.demo.data.repositories.DynamoDbTestUtil.createTableRequest;


public class ShortMessageRepositoryTest extends LocalDynamoDbSyncTestBase{

    static final String CPF = "93277776204";
    static final String SIGLA = "FZ3";
    static final String STATUS = "EM_PROCESSAMENTO";

    static final String TABLE_NAME = "DynamoDbBeanlessRepositoryTestTable";

    //ShortMessageRepository repository = new ShortMessageRepository(getDynamoDbClient(), getConcreteTableName(TABLE_NAME));
    ShortMessageRepository repository = new ShortMessageRepository(getDynamoDbClient());


    @BeforeEach
    public void setup() throws Exception {
        CreateTableRequest request = createTableRequest(getConcreteTableName(TABLE_NAME));
        getDynamoDbClient().createTable(request);
    }

    @AfterEach
    public void deleteTable() {
        getDynamoDbClient().deleteTable(DeleteTableRequest.builder()
                .tableName(getConcreteTableName(TABLE_NAME))
                .build());

    }

    @Test
    public void givenItemWithMessage_whenRunFindAll_thenItemIsFound() throws JsonProcessingException {

        ShortMessageServiceData data = new ShortMessageServiceData();
        data.setCpf(CPF);
        data.setSigla(SIGLA);
        data.setUuid(UUID.randomUUID().toString());
        data.setDataInsercao(Instant.now());
        repository.insertItem(data);

        Map<String, AttributeValue> map = repository.getShortMessageSericeRaw(CPF);
        System.out.println(map);
    }

    @Test
    public void givenItemWithMessage_whenRunFindAllWithQueryCondicional_thenItemIsFound() {

        SMSData data = new SMSData();
        data.setIdCliente(CPF);
        data.setSigla(SIGLA);
        data.setUuid(UUID.randomUUID().toString());
        data.setDataInsercao("A");
        repository.insertItem(data);


        SMSData smsData = repository.getOrder(CPF, "A");
        PageIterable<SMSData> map = repository.getShortMessageSericeRaw2(CPF);

        System.out.println("###############################################################################");
        System.out.println(map.items().stream().findAny().get().getIdCliente());
    }

    @Test
    public void givenItemWithMessage_whenRunUpdateList_thenItemIsFound() throws JsonProcessingException {

        SMSData smsData = new SMSData();
        String uuid = UUID.randomUUID().toString();
        smsData.setIdCliente(CPF);
        smsData.setSigla(SIGLA);
        smsData.setUuid(uuid);
        smsData.setDataInsercao(String.valueOf(LocalDateTime.now().minus(5, ChronoUnit.DAYS)));

        repository.insertItemWithEmptyList(smsData);
        AttributeValue attributeValue = AttributeValue.builder().s("EM Processamento").build();
        AttributeValue firstValue = AttributeValue.builder().s("14/07/2021").build();
        AttributeValue attributeValueList = AttributeValue.builder().l(Arrays.asList(attributeValue, firstValue)).build();
        repository.insertValueAtIndex(uuid, 0, attributeValueList);
        AttributeValue attributeValue2 = AttributeValue.builder().s("ENVIADO AO EK7").build();
        AttributeValue firstValue2 = AttributeValue.builder().s("14/07/2021").build();
        AttributeValue attributeValueList2 = AttributeValue.builder().l(Arrays.asList(attributeValue2, firstValue2)).build();
        repository.appendValuesToList(uuid, attributeValueList2);
        Map<String, AttributeValue> map = repository.getItemWithList(uuid);
        System.out.println(map);




    }

    private List<HistoricoData> createHistoric() {
        return Arrays.asList(HistoricoData.builder().status("OK").dataInsercao(String.valueOf(LocalDateTime.now().minus(5, ChronoUnit.DAYS))).build());
    }

}