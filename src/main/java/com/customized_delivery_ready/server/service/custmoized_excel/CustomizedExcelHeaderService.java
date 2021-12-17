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
        // List<CustomizedExcelDataDto> excelDtos = new ArrayList<>();
        JSONArray uploadHeaderDetail = this.objectToJsonArray(headerDto.getUploadHeaderDetail().get("details"));
        JSONArray downloadHeaderDetail = this.objectToJsonArray(headerDto.getDownloadHeaderDetail().get("details"));

        // 데이터 시작 위치
        Row row = worksheet.getRow(headerDto.getStartRowNumber()-1);


        // uploadHeaderDetail에 따라 업로드 되는 엑셀의 데이터를 가공
        JSONObject customDataJson = new JSONObject();
        JSONArray customDataJsonArr = new JSONArray();
        JSONObject customDetailJson = new JSONObject();
        JSONArray customDetailJsonArr = new JSONArray();

        // 업로드 엑셀의 항목 개수만큼 for문을 돈다
        for(int colNum = 0; colNum < row.getPhysicalNumberOfCells(); colNum++) {
            customDetailJson = new JSONObject();
            for(int i = 0; i < uploadHeaderDetail.size(); i++) {
                JSONObject detail = this.objectToJsonObject(uploadHeaderDetail.get(i));

                String cellString = "";
                Cell cell = row.getCell(colNum);
    
                if(colNum == (int)detail.get("cellNumber")) {
                    customDataJsonArr = new JSONArray();
                    // 세로개수
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

        // downloadHeaderDetail에 따라 다운로드 되는 엑셀의 데이터를 가공
        JSONObject resultDataJson = new JSONObject();
        JSONArray resultDataJsonArr = new JSONArray();
        JSONObject resultDetailJson = new JSONObject();

        for(int i = 0; i < downloadHeaderDetail.size(); i++) {
            JSONObject detail = this.objectToJsonObject(downloadHeaderDetail.get(i));
            resultDetailJson = new JSONObject();

            for(int j = 0; j < customDetailJsonArr.size(); j++) {
                JSONObject customizedData = this.objectToJsonObject(customDetailJsonArr.get(j));
                JSONArray customizedDetailsArr = this.objectToJsonArray(customizedData.get("customizedDetails"));

                // 데이터 row 개수만큼 fixedValue 설정
                if ((int) detail.get("targetCellNumber") == -1) {
                    JSONArray fixedDataJsonArr = new JSONArray();
                    JSONObject fixedDataJson = new JSONObject();

                    for(int k = headerDto.getStartRowNumber()-1; k < worksheet.getPhysicalNumberOfRows(); k++) {
                        fixedDataJson.put("id", UUID.randomUUID().toString());
                        fixedDataJson.put("targetCellNumber", -1);
                        fixedDataJson.put("originColData", detail.get("fixedValue"));
                        fixedDataJson.put("headerName", detail.get("headerName"));
                        fixedDataJsonArr.add(fixedDataJson);
                        resultDetailJson.put("customizedColData", fixedDataJsonArr);
                    }
                    resultDataJsonArr.add(resultDetailJson);
                    break;
                }else{
                    for (int k = 0; k < customizedDetailsArr.size(); k++) {
                        resultDetailJson = new JSONObject();
                        JSONObject customizedDetailsData = this.objectToJsonObject(customizedDetailsArr.get(k));
    
                        // download 커스터마이징 항목의 targetCellNumber 순서로 데이터 정렬 
                        if ((int)detail.get("targetCellNumber") == (int)customizedDetailsData.get("targetCellNumber")) {
                            resultDetailJson.put("customizedColData", customizedDetailsArr);
                            resultDataJsonArr.add(resultDetailJson);
                            break;
                        }
                    }
                }
            }
            resultDataJson.put("details", resultDataJsonArr);

            CustomizedExcelDataDto excelDto = CustomizedExcelDataDto.builder()
                .cid(i)
                .id(UUID.randomUUID())
                .customizedData(resultDataJson)
                .build();

            // excelDtos.add(excelDto);
        }
        resultDataJson.put("details", resultDataJsonArr);

        CustomizedExcelDataDto excelDto = CustomizedExcelDataDto.builder()
            .cid(1)
            .id(UUID.randomUUID())
            .customizedData(resultDataJson)
            .build();
        // return excelDtos;
        return excelDto;
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
