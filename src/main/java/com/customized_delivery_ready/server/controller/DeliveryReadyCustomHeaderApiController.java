package com.customized_delivery_ready.server.controller;

import java.util.Map;

import com.customized_delivery_ready.server.model.custom_table_header.dto.CustomTableHeaderGetDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.customize_delivery_ready.CustomTableHeaderService;

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
public class DeliveryReadyCustomHeaderApiController {
    
    @Autowired
    private CustomTableHeaderService customTableHeaderService;

    @PostMapping("/one")
    public ResponseEntity<?> createCustomTableHeader(@RequestBody CustomTableHeaderGetDto dto) {
        Message message = new Message();

        message.setData(customTableHeaderService.createOne(dto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/one")
    public ResponseEntity<?> getCustomTableHeader(@RequestParam Map<String, Object> query) {
        Message message = new Message();

        message.setData(customTableHeaderService.searchOne(query));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllCustomTableHeader() {
        Message message = new Message();

        message.setData(customTableHeaderService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
