package sls.dynamodb.enhancedclient.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sls.dynamodb.enhancedclient.demo.data.entity.SMSData;
import sls.dynamodb.enhancedclient.demo.dto.SMSDTO;
import sls.dynamodb.enhancedclient.demo.service.ShortMessageServiceService;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping("/short-message-services")
public class ShortMessageServiceController {

    ShortMessageServiceService shortMessageServiceService;

    @Autowired
    public ShortMessageServiceController(ShortMessageServiceService shortMessageServiceService) {
        this.shortMessageServiceService = shortMessageServiceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SMSData> getItem(@PathVariable String id) throws JsonProcessingException {
        return new ResponseEntity<>(this.shortMessageServiceService.getItem(id), HttpStatus.OK);
    }


    @PostMapping
    public void saveItem(@RequestBody SMSDTO smsdto) throws JsonProcessingException {
        this.shortMessageServiceService.saveItem(smsdto);
    }

    @PutMapping
    public void pdateItem(@RequestBody SMSDTO smsdto) throws JsonProcessingException {
        this.shortMessageServiceService.updateItem(smsdto);
    }
}
