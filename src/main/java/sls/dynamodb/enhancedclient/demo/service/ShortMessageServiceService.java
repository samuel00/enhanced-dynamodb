package sls.dynamodb.enhancedclient.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import sls.dynamodb.enhancedclient.demo.data.entity.SMSData;
import sls.dynamodb.enhancedclient.demo.data.repositories.DynamoDbItem;
import sls.dynamodb.enhancedclient.demo.dto.SMSDTO;

public interface ShortMessageServiceService {

    void saveItem(SMSDTO smsdto) throws JsonProcessingException;

    SMSData getItem(String id) throws JsonProcessingException;

    void updateItem(SMSDTO smsdto) throws JsonProcessingException;
}
