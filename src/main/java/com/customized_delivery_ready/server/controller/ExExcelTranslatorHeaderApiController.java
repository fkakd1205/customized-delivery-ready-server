package com.customized_delivery_ready.server.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.customized_delivery_ready.server.model.excel_translator_data.dto.ExcelTranslatorDataGetDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.service.ex_excel_translator.ExExcelTranslatorHeaderService;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ex-excel-translator")
public class ExExcelTranslatorHeaderApiController {
    
    @Autowired
    private ExExcelTranslatorHeaderService excelTranslatorHeaderService;

    @PostMapping("/one")
    public ResponseEntity<?> createExcelTranslatorHeader(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        excelTranslatorHeaderService.createOne(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @GetMapping("/list")
    public ResponseEntity<?> searchExcelTranslatorHeader() {
        Message message = new Message();

        message.setData(excelTranslatorHeaderService.searchList());
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/one")
    public ResponseEntity<?> updateExcelTranslatorHeader(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        message.setData(excelTranslatorHeaderService.updateOne(dto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file, @RequestPart ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        message.setData(excelTranslatorHeaderService.uploadExcelFile(file, dto));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/download")
    public void downloadExcelFile(HttpServletResponse response, @RequestBody List<ExcelTranslatorDataGetDto> dtos) {

        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);

        // headerName 설정
        for(int i = 0; i < dtos.size(); i++) {
            // header는 첫번째 row에 기입
            row = sheet.getRow(0);
            cell = row.createCell(i);
            cell.setCellValue(dtos.get(i).getCustomizedData().getDetails().get(0).getHeaderName());

            // 엑셀 데이터 설정
            for(int j = 0; j < dtos.get(i).getCustomizedData().getDetails().size(); j++) {
                // 엑셀 데이터는 header의 다음 row부터 기입
                row = sheet.getRow(j+1);
                if(row == null) {
                    row = sheet.createRow(j+1);
                }
                cell = row.createCell(i);
                cell.setCellValue(dtos.get(i).getCustomizedData().getDetails().get(j).getOriginColData());
            }
        }

        for(int i = 0; i < dtos.size(); i++){
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

    @PostMapping("/upload/test")
    public ResponseEntity<?> uploadExcelFileTest(@RequestParam("file") MultipartFile file) {
        Message message = new Message();

        message.setData(excelTranslatorHeaderService.uploadExcelFileTest(file));
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

}
