package com.customized_delivery_ready.server.controller;

import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.excel_translator.ExcelTranslatorHeaderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/excel-translator")
public class ExcelTranslatorHeaderApiController {
    
    @Autowired
    private ExcelTranslatorHeaderService excelTranslatorHeaderService;

    @PostMapping("/one")
    public ResponseEntity<?> createExcelTranslatorHeader(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        excelTranslatorHeaderService.createOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    // @GetMapping("/list")
    // public ResponseEntity<?> searchExcelTranslatorHeader() {
    //     Message message = new Message();

    //     message.setData(excelTranslatorHeaderService.searchList());
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }

    // @GetMapping("/list/test")
    // public ResponseEntity<?> searchExcelTranslatorHeaderTest() {
    //     Message message = new Message();

    //     message.setData(excelTranslatorHeaderService.searchListTest());
    //     message.setStatus(HttpStatus.OK);
    //     message.setMessage("success");

    //     return new ResponseEntity<>(message, message.getStatus());
    // }
}
