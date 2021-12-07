package com.customized_delivery_ready.server.controller;

import java.util.List;

import com.customized_delivery_ready.server.model.custom_data.dto.CustomDeliveryReadyItemGetDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.customize_delivery_ready.CustomDeliveryReadyDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/delivery-ready/customize")
public class DeliveryReadyCustomDataApiController {
    
    @Autowired
    private CustomDeliveryReadyDataService customDeliveryReadyDataService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        message.setData(customDeliveryReadyDataService.uploadDeliveryReadyExcelFile(file));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // @PostMapping("/store")
    // public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
    //     Message message = new Message();

    //     message.setData(customDeliveryReadyDataService.storeDeliveryReadyExcelFile(file));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    @PostMapping("/store")
    public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestBody List<CustomDeliveryReadyItemGetDto> dtos) {
        Message message = new Message();

        message.setData(customDeliveryReadyDataService.storeDeliveryReadyExcelFile(dtos));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
