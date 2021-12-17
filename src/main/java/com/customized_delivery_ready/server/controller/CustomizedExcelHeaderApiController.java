package com.customized_delivery_ready.server.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.customized_delivery_ready.server.model.customized_excel.dto.CustomizedExcelDataDto;
import com.customized_delivery_ready.server.model.customized_excel.dto.CustomizedExcelHeaderDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.custmoized_excel.CustomizedExcelHeaderService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/customized-excel")
public class CustomizedExcelHeaderApiController {
   
    @Autowired
    private CustomizedExcelHeaderService customizedExcelHeaderService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file, @RequestPart CustomizedExcelHeaderDto dto) {
        Message message = new Message();

        message.setData(customizedExcelHeaderService.uploadExcelFile(file, dto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/download")
    public void downloadExcelFile(HttpServletResponse response, @RequestBody CustomizedExcelDataDto dto) {

        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        JSONArray jsonArr = customizedExcelHeaderService.objectToJsonArray(dto.getCustomizedData().get("details"));

        row = sheet.createRow(rowNum++);
        
        // headerName 설정
        for(int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObj = customizedExcelHeaderService.objectToJsonObject(jsonArr.get(i));
            JSONArray jsonArr2 = customizedExcelHeaderService.objectToJsonArray(jsonObj.get("customizedColData"));
            JSONObject jsonObj2 = customizedExcelHeaderService.objectToJsonObject(jsonArr2.get(0));
            cell = row.createCell(i);
            cell.setCellValue(jsonObj2.get("headerName").toString());
        }

        for(int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObj = customizedExcelHeaderService.objectToJsonObject(jsonArr.get(i));
            JSONArray jsonArr2 = customizedExcelHeaderService.objectToJsonArray(jsonObj.get("customizedColData"));
            
            for(int j = 0; j < jsonArr2.size(); j++) {
                if (i == 0) row = sheet.createRow(rowNum++);
                JSONObject jsonObj2 = customizedExcelHeaderService.objectToJsonObject(jsonArr2.get(j));
                row = sheet.getRow(j+1);
                cell = row.createCell(i);
                cell.setCellValue(jsonObj2.get("originColData").toString());
            }
        }

        for(int i = 0; i < jsonArr.size(); i++){
            sheet.autoSizeColumn(i);
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        try{
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }
}
