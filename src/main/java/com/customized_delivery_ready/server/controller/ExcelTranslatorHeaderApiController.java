package com.customized_delivery_ready.server.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import com.customized_delivery_ready.server.exception.ExcelFileUploadException;
import com.customized_delivery_ready.server.model.excel_translator_data.dto.ExcelTranslatorDataGetDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.customized_delivery_ready.server.model.message.Message;
import com.customized_delivery_ready.server.model.upload_excel_data.dto.DownloadExcelDataGetDto;
import com.customized_delivery_ready.server.model.upload_excel_data.dto.UploadedDetailDto;
import com.customized_delivery_ready.server.service.excel_translator.ExcelTranslatorHeaderService;

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
@RequestMapping("/api/v1/excel-translator")
public class ExcelTranslatorHeaderApiController {
    
    @Autowired
    private ExcelTranslatorHeaderService excelTranslatorHeaderService;

    @PostMapping("/one")
    public ResponseEntity<?> createExcelTranslatorHeaderTitle(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        excelTranslatorHeaderService.createTitle(dto);
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

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file, @RequestPart ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        try{
            message.setData(excelTranslatorHeaderService.uploadExcelFile(file, dto));
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
        } catch (IllegalArgumentException e) {
            throw new ExcelFileUploadException("설정된 양식과 동일한 엑셀 파일을 업로드해주세요.");
        } catch (NullPointerException e) {
            throw new ExcelFileUploadException("설정된 양식과 동일한 엑셀 파일을 업로드해주세요.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/header/upload/one")
    public ResponseEntity<?> updateUploadHeaderDetailOfExcelTranslator(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        excelTranslatorHeaderService.updateUploadHeaderDetailOfExcelTranslator(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PutMapping("/header/download/one")
    public ResponseEntity<?> updateDownloadHeaderDetailOfExcelTranslator(@RequestBody ExcelTranslatorHeaderGetDto dto) {
        Message message = new Message();

        excelTranslatorHeaderService.updateDownloadHeaderDetailOfExcelTranslator(dto);
        message.setStatus(HttpStatus.OK);
        message.setMessage("success");

        return new ResponseEntity<>(message, message.getStatus());
    }

    @PostMapping("/download")
    public void downloadExcelFile(HttpServletResponse response, @RequestBody List<DownloadExcelDataGetDto> dtos) {

        // 엑셀 생성
        Workbook workbook = new XSSFWorkbook();     // .xlsx
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

        // headerName 설정
        for(int i = 0; i < dtos.size(); i++) {
            for(int j = 0; j < dtos.get(i).getTranslatedData().getDetails().size(); j++) {
                // 엑셀 데이터는 header의 다음 row부터 기입
                row = sheet.getRow(j);
                if(row == null) {
                    row = sheet.createRow(j);
                }
                cell = row.createCell(i);
                
                UploadedDetailDto detailDto = dtos.get(i).getTranslatedData().getDetails().get(j);

                try{
                    if(detailDto.getCellType().equals("String")) {
                        cell.setCellValue(detailDto.getColData().toString());
                    }else if(detailDto.getCellType().equals("Date")) {
                        Date data = format.parse(detailDto.getColData().toString());
                        cell.setCellValue(outputFormat.format(data));
                    }else if(detailDto.getCellType().equals("Double")) {
                        cell.setCellValue((int)detailDto.getColData());
                    }
                } catch(ParseException e) {

                }
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
}
