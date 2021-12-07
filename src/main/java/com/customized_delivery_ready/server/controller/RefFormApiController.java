package com.customized_delivery_ready.server.controller;

import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.customize_delivery_ready.RefFormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/delivery-ready/ref-form")
public class RefFormApiController {
    
    @Autowired
    private RefFormService refFormService;

    @GetMapping("/list")
    public ResponseEntity<?> searchList() {
        Message message = new Message();

        message.setData(refFormService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
