package com.customized_delivery_ready.server.controller;

import java.util.List;
import java.util.Map;

import com.customized_delivery_ready.server.model.custom_table_header.dto.CustomTableHeaderGetDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.customize_delivery_ready.DeliveryReadyCustomTableHeaderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery-ready/customize/header")
public class DeliveryReadyCustomTableHeaderApiController {
    
    @Autowired
    private DeliveryReadyCustomTableHeaderService deliveryReadyCustomTableHeaderService;

    @GetMapping("/one")
    public ResponseEntity<?> getCustomTableHeader(@RequestParam Map<String, Object> query) {
        Message message = new Message();

        message.setData(deliveryReadyCustomTableHeaderService.searchOne(query));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/selected")
    public ResponseEntity<?> getAllCustomTableHeader(@RequestParam Map<String, Object> query) {
        Message message = new Message();

        message.setData(deliveryReadyCustomTableHeaderService.searchList(query));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/list")
    public ResponseEntity<?> createCustomTableHeader(@RequestBody List<CustomTableHeaderGetDto> dtos) {
        Message message = new Message();

        message.setData(deliveryReadyCustomTableHeaderService.createList(dtos));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/update/list")
    public ResponseEntity<?> updateCustomTableHeader(@RequestBody List<CustomTableHeaderGetDto> dtos) {
        Message message = new Message();

        deliveryReadyCustomTableHeaderService.updateList(dtos);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
