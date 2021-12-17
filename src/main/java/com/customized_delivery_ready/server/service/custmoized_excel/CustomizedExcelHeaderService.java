package com.customized_delivery_ready.server.service.custmoized_excel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.customized_delivery_ready.server.model.customized_excel.dto.CustomizedExcelDataDto;
import com.customized_delivery_ready.server.model.customized_excel.dto.CustomizedExcelHeaderDto;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CustomizedExcelHeaderService {
    
    public CustomizedExcelDataDto uploadExcelFile(MultipartFile file, CustomizedExcelHeaderDto dto) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        CustomizedExcelDataDto excelDto = this.getCustomizedExcelForm(sheet, dto);
        return excelDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일(네이버 배송준비 엑셀)의 데이터들을 Dto로 변환한다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyNaverItemDto::
     */
    private CustomizedExcelDataDto getCustomizedExcelForm(Sheet worksheet, CustomizedExcelHeaderDto headerDto) {
        JSONArray uploadHeaderDetail = this.objectToJsonArray(headerDto.getUploadHeaderDetail().get("details"));
        JSONArray downloadHeaderDetail = this.objectToJsonArray(headerDto.getDownloadHeaderDetail().get("details"));

        Row row = worksheet.getRow(headerDto.getStartRowNumber()-1);

        JSONObject customDataJson = new JSONObject();
        JSONArray customDataJsonArr = new JSONArray();
        JSONObject customDetailJson = new JSONObject();
        JSONArray customDetailJsonArr = new JSONArray();

        for(int colNum = 0; colNum < row.getPhysicalNumberOfCells(); colNum++) {
            customDetailJson = new JSONObject();
            for(int i = 0; i < uploadHeaderDetail.size(); i++) {
                JSONObject detail = this.objectToJsonObject(uploadHeaderDetail.get(i));

                String cellString = "";
                Cell cell = row.getCell(colNum);
    
                if(colNum == (int)detail.get("cellNumber")) {
                    customDataJsonArr = new JSONArray();
                    for(int rowNum = headerDto.getStartRowNumber()-1; rowNum < worksheet.getPhysicalNumberOfRows(); rowNum++){
                        row = worksheet.getRow(rowNum);
                        cell = row.getCell(colNum);

                        switch(detail.get("dataType").toString()) {
                            case "STRING" :
                                cellString = cell.getStringCellValue();
                                break;
                            case "DATE" :
                                Date date = cell.getDateCellValue();
                                cellString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
                                break;
                            case "NUMERIC" :
                                cellString = String.valueOf((int)cell.getNumericCellValue());
                                break;
                        }
                        customDataJson = new JSONObject();
                        customDataJson.put("id", UUID.randomUUID().toString());
                        customDataJson.put("originColData", cellString);
                        customDataJson.put("headerName", detail.get("headerName"));
                        customDataJson.put("targetCellNumber", (int)detail.get("cellNumber"));
                        customDataJsonArr.add(customDataJson);
                    }
                    break;
                }
            }
            customDetailJson.put("customizedDetails", customDataJsonArr);
            customDetailJsonArr.add(customDetailJson);
        }

        JSONObject resultDataJson = new JSONObject();
        JSONArray resultDataJsonArr = new JSONArray();
        JSONObject resultDetailJson = new JSONObject();

        for(int i = 0; i < downloadHeaderDetail.size(); i++) {
            JSONObject detail = this.objectToJsonObject(downloadHeaderDetail.get(i));
            resultDetailJson = new JSONObject();

            for(int k = 0; k < customDetailJsonArr.size(); k++) {
                JSONObject customizedData = this.objectToJsonObject(customDetailJsonArr.get(k));
                JSONArray customizedDetailsArr = this.objectToJsonArray(customizedData.get("customizedDetails"));

                // fixedValue설정
                if ((int) detail.get("targetCellNumber") == -1) {
                    JSONArray fixedDataJsonArr = new JSONArray();
                    JSONObject fixedDataJson = new JSONObject();

                    fixedDataJson.put("id", UUID.randomUUID().toString());
                    fixedDataJson.put("targetCellNumber", -1);
                    fixedDataJson.put("originColData", detail.get("fixedValue"));
                    fixedDataJson.put("headerName", detail.get("headerName"));
                    fixedDataJsonArr.add(fixedDataJson);
                    resultDetailJson.put("customizedColData", fixedDataJsonArr);
                    resultDataJsonArr.add(resultDetailJson);
                    break;
                }

                for (int j = 0; j < customizedDetailsArr.size(); j++) {
                    resultDetailJson = new JSONObject();
                    JSONObject customizedDetailsData = this.objectToJsonObject(customizedDetailsArr.get(j));

                    if ((int) customizedDetailsData.get("targetCellNumber") == (int) detail.get("targetCellNumber")) {
                        resultDetailJson.put("customizedColData", customizedDetailsArr);
                        resultDataJsonArr.add(resultDetailJson);
                        break;
                    }
                }
            }
        }
        resultDataJson.put("details", resultDataJsonArr);

        CustomizedExcelDataDto excelDto = CustomizedExcelDataDto.builder()
            .cid(1)
            .id(UUID.randomUUID())
            .customizedData(resultDataJson)
            .build();
        return excelDto;



            // for(int colNum = 0; colNum < row.getPhysicalNumberOfCells(); colNum++){
            //     for(int j = 0; j < uploadHeaderDetail.size(); j++) {
            //         JSONObject detail = this.objectToJsonObject(uploadHeaderDetail.get(j));
    
            //         String cellString = "";
            //         Cell cell = row.getCell(colNum);
    
            //         if(rowNum == (int)detail.get("cellNumber")) {
            //             switch(detail.get("dataType").toString()) {
            //                 case "STRING" : 
            //                     System.out.println(detail.get("headerName"));
            //                     cellString = cell.getStringCellValue();
            //                     break;
            //                 case "DATE" :
            //                     Date date = cell.getDateCellValue();
            //                     cellString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
            //                     break;
            //                 case "NUMERIC" :
            //                     cellString = String.valueOf((int)cell.getNumericCellValue());
            //                     break;
            //             }

            //             customDataJson = new JSONObject();
            //             customDataJson.put("cid", j);
            //             customDataJson.put("id", UUID.randomUUID());
            //             customDataJson.put("originColData", cellString);
            //             customDataJson.put("targetCellNumber", (int)detail.get("cellNumber"));
    
            //             customDataJsonArr.add(customDataJson);
            //             break;
            //         }
            //     }
            // }


        // for (int rowNum = headerDto.getStartRowNumber()-1; rowNum < worksheet.getPhysicalNumberOfRows(); rowNum++) {
        //     Row row = worksheet.getRow(rowNum);

        //     JSONObject customDataJson = new JSONObject();
        //     JSONArray customDataJsonArr = new JSONArray();
        //     JSONObject customDetailJson = new JSONObject();

        //     for(int colNum = 0; colNum < row.getPhysicalNumberOfCells(); colNum++){
        //         for(int j = 0; j < uploadHeaderDetail.size(); j++) {
        //             JSONObject detail = this.objectToJsonObject(uploadHeaderDetail.get(j));
    
        //             String cellString = "";
        //             Cell cell = row.getCell(colNum);
    
        //             if(rowNum == (int)detail.get("cellNumber")) {
        //                 switch(detail.get("dataType").toString()) {
        //                     case "STRING" : 
        //                         System.out.println(detail.get("headerName"));
        //                         cellString = cell.getStringCellValue();
        //                         break;
        //                     case "DATE" :
        //                         Date date = cell.getDateCellValue();
        //                         cellString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
        //                         break;
        //                     case "NUMERIC" :
        //                         cellString = String.valueOf((int)cell.getNumericCellValue());
        //                         break;
        //                 }

        //                 customDataJson = new JSONObject();
        //                 customDataJson.put("cid", j);
        //                 customDataJson.put("id", UUID.randomUUID());
        //                 customDataJson.put("originColData", cellString);
        //                 customDataJson.put("targetCellNumber", (int)detail.get("cellNumber"));
    
        //                 customDataJsonArr.add(customDataJson);
        //                 break;
        //             }
        //         }
        //     }
        //     System.out.println(customDataJsonArr);

            // JSONObject customDataJson = new JSONObject();
            // JSONArray customDataJsonArr = new JSONArray();
            // JSONObject customDetailJson = new JSONObject();

            // for(int j = 0; j < NAVER_DELIVERY_READY_COL_SIZE; j++){
            //     customDataJson = new JSONObject();
            //     customDataJson.put("cid", j);
            //     customDataJson.put("id", UUID.randomUUID());

            //     String cellString = "";
            //     Cell cell = row.getCell(j);

            //     if(cell != null) {
            //         if (row.getCell(j).getCellType() == CellType.STRING) {
            //             cellString = cell.getStringCellValue();
            //         } else if (cell.getCellType() == CellType.NUMERIC) {
            //             if (DateUtil.isCellDateFormatted(cell)) {
            //                 Date date = cell.getDateCellValue();
            //                 cellString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
            //             } else {
            //                 cellString = String.valueOf((int)cell.getNumericCellValue());
            //             }
            //         }
            //         customDataJson.put("origin_col_data", cellString);
            //     }
            //     customDataJson.put("ref_form_id", refDtos.get(j).getId());
            //     customDataJsonArr.add(customDataJson);
            // }

            // customDetailJson.put("details", customDataJsonArr);
            
            // CustomizedExcelDataDto dto = CustomizedExcelDataDto.builder()
            //     .id(UUID.randomUUID())
            //     .deliveryReadyCustomItem(customDetailJson)
            //     .orderNumber(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
            //     .prodOrderNumber(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "")
            //     .prodName(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
            //     .optionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
            //     .receiver(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
            //     .destination(row.getCell(42) != null ? row.getCell(42).getStringCellValue() : "")
            //     .build();

            // dtos.add(dto);
        // }
    }

    public JSONArray objectToJsonArray(Object object){
        JSONArray jsonArray = new JSONArray();
        if (object instanceof Map){
            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll((Map)object);
            jsonArray.add(jsonObject);
        }
        else if (object instanceof List){
            jsonArray.addAll((List)object);
        }
        return jsonArray;
    }

    public JSONObject objectToJsonObject(Object object){
        JSONObject jsonObject = new JSONObject();
        if (object instanceof Map){
            jsonObject.putAll((Map)object);
        }
        return jsonObject;
    }
}
