package com.customized_delivery_ready.server.controller;

import java.util.List;
import java.util.Map;

import com.customized_delivery_ready.server.model.custom_data.dto.CustomDeliveryReadyItemGetDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.customize_delivery_ready.DeliveryReadyCustomDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/delivery-ready/customize/naver")
public class DeliveryReadyCustomDataApiController {
    
    @Autowired
    private DeliveryReadyCustomDataService deliveryReadyCustomDataService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        message.setData(deliveryReadyCustomDataService.uploadDeliveryReadyExcelFile(file));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // @PostMapping("/upload/test")
    // public ResponseEntity<?> uploadDeliveryReadyExcelFileTest(@RequestParam("file") MultipartFile file) {
    //     Message message = new Message();

    //     message.setData(deliveryReadyCustomDataService.uploadDeliveryReadyExcelFileTest(file));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    @PostMapping("/store")
    public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestBody List<CustomDeliveryReadyItemGetDto> dtos) {
        Message message = new Message();

        message.setData(deliveryReadyCustomDataService.storeDeliveryReadyExcelFile(dtos));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // @PostMapping("/customized")
    // public ResponseEntity<?> changeToCustomizeDeliveryReadyExcelFile(@RequestBody List<CustomDeliveryReadyItemGetDto> dtos) {
    //     Message message = new Message();

    //     message.setData(deliveryReadyCustomDataService.changeToCustomizedDeliveryReadyExcelFile(dtos));
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    @GetMapping("/customized")
    public ResponseEntity<?> searchAllCustomDeliveryReadyItem(@RequestParam Map<String, Object> query) {
        Message message = new Message();

        message.setData(deliveryReadyCustomDataService.searchAllCustomDeliveryReadyItem(query));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
