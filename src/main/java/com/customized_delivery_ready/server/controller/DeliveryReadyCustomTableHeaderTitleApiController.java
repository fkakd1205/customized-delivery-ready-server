package com.customized_delivery_ready.server.controller;

import com.customized_delivery_ready.server.model.custom_table_header_title.dto.CustomTableHeaderTitleGetDto;
import com.customized_delivery_ready.server.model.custom_table_header_title.entity.CustomTableHeaderTitleEntity;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.customize_delivery_ready.DeliveryReadyCustomTableHeaderTitleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery-ready/customize/header-title")
public class DeliveryReadyCustomTableHeaderTitleApiController {
    
    @Autowired
    private DeliveryReadyCustomTableHeaderTitleService deliveryReadyCustomHeaderTitleService;

    @GetMapping("/list")
    public ResponseEntity<?> getCustomTableHeaderTitle() {
        Message message = new Message();

        message.setData(deliveryReadyCustomHeaderTitleService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/one")
    public ResponseEntity<?> createCustomTableHeaderTitle(@RequestBody CustomTableHeaderTitleGetDto dto) {
        Message message = new Message();

        deliveryReadyCustomHeaderTitleService.createOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
