package sls.dynamodb.enhancedclient.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sls.dynamodb.enhancedclient.demo.data.entity.SMSData;
import sls.dynamodb.enhancedclient.demo.data.repositories.ShortMessageRepository;
import sls.dynamodb.enhancedclient.demo.dto.SMSDTO;
import sls.dynamodb.enhancedclient.demo.service.ShortMessageServiceService;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

@Service
public class ShortMessageServiceServiceImpl implements ShortMessageServiceService {

    ModelMapper mapper;

    ShortMessageRepository shortMessageRepository;

    @Autowired
    public ShortMessageServiceServiceImpl(ShortMessageRepository shortMessageRepository, ModelMapper mapper) {
        this.mapper = mapper;
        this.shortMessageRepository = shortMessageRepository;
    }

    @Override
    public void saveItem(SMSDTO smsdto) throws JsonProcessingException {
        SMSData smsData = mapper.map(smsdto, SMSData.class);
        this.shortMessageRepository.insertItem(smsData);
    }

    @Override
    public SMSData getItem(String id) throws JsonProcessingException {
        return this.shortMessageRepository.getSMSData(id);
    }

    @Override
    public void updateItem(SMSDTO smsdto) throws JsonProcessingException {
        SMSData smsData = mapper.map(smsdto, SMSData.class);
        List<AttributeValue> lista = new ArrayList<>();
        smsData.getHistorico().stream().forEach(historicoData -> {
            Map<String, AttributeValue> doubleBraceMap  = new HashMap<String, AttributeValue>() {{
                put("dataInsercao", AttributeValue.builder().s(historicoData.getDataInsercao()).build());
                put("status", AttributeValue.builder().s(historicoData.getStatus()).build());
                put("mensagem", AttributeValue.builder().s(historicoData.getMensagem()).build());
            }};
            AttributeValue attributeDataMap = AttributeValue.builder().m(doubleBraceMap).build();
            lista.add(attributeDataMap);
        });
       AttributeValue attributeValueList = AttributeValue.builder().l(lista).build();
        this.shortMessageRepository.appendValuesToList2(smsData.getUuid(), attributeValueList);
    }
}
