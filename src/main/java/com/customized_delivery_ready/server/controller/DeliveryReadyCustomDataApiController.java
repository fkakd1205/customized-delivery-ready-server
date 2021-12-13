package com.customized_delivery_ready.server.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.customized_delivery_ready.server.model.custom_data.dto.CustomDeliveryReadyItemGetDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.model.ref_form.dto.RefFormGetDto;
import com.customized_delivery_ready.server.model.ref_form.entity.RefFormEntity;
import com.customized_delivery_ready.server.service.customize_delivery_ready.CustomDeliveryReadyDataService;
import com.customized_delivery_ready.server.service.customize_delivery_ready.RefFormService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    private CustomDeliveryReadyDataService customDeliveryReadyDataService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDeliveryReadyExcelFile(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        message.setData(customDeliveryReadyDataService.uploadDeliveryReadyExcelFile(file));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/store")
    public ResponseEntity<?> storeDeliveryReadyExcelFile(@RequestBody List<CustomDeliveryReadyItemGetDto> dtos) {
        Message message = new Message();

        message.setData(customDeliveryReadyDataService.storeDeliveryReadyExcelFile(dtos));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/customized")
    public ResponseEntity<?> changeToCustomizeDeliveryReadyExcelFile(@RequestBody List<CustomDeliveryReadyItemGetDto> dtos) {
        Message message = new Message();

        message.setData(customDeliveryReadyDataService.changeToCustomizedDeliveryReadyExcelFile(dtos));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/searchList")
    public ResponseEntity<?> searchAllCustomDeliveryReadyItem() {
        Message message = new Message();

        message.setData(customDeliveryReadyDataService.searchAllCustomDeliveryReadyItem());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }
}
