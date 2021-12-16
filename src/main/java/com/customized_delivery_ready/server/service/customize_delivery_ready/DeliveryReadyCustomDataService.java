package com.customized_delivery_ready.server.service.customize_delivery_ready;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.customized_delivery_ready.server.model.custom_data.dto.CustomDeliveryReadyItemGetDto;
import com.customized_delivery_ready.server.model.custom_data.dto.CustomizedDeliveryReadyItemResDto;
import com.customized_delivery_ready.server.model.custom_data.entity.CustomDeliveryReadyItemEntity;
import com.customized_delivery_ready.server.model.custom_data.repository.CustomDeliveryReadyRepository;
import com.customized_delivery_ready.server.model.custom_table_header.dto.CustomTableHeaderGetDto;
import com.customized_delivery_ready.server.model.custom_table_header.entity.CustomTableHeaderEntity;
import com.customized_delivery_ready.server.model.ref_form.dto.RefFormGetDto;
import com.customized_delivery_ready.server.model.ref_form.entity.RefFormEntity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DeliveryReadyCustomDataService {

    final int NAVER_DELIVERY_READY_COL_SIZE = 67;

    @Autowired
    private CustomDeliveryReadyRepository customDeliveryReadyRepository;

    @Autowired
    private DeliveryReadyCustomTableHeaderService customTableHeaderService;

    @Autowired
    private RefFormService refFormService;
   
    public List<CustomDeliveryReadyItemGetDto> uploadDeliveryReadyExcelFile(MultipartFile file) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<CustomDeliveryReadyItemGetDto> dtos = this.getDeliveryReadyExcelForm(sheet);
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일(네이버 배송준비 엑셀)의 데이터들을 Dto로 변환한다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyNaverItemDto::
     */
    private List<CustomDeliveryReadyItemGetDto> getDeliveryReadyExcelForm(Sheet worksheet) {
        List<CustomDeliveryReadyItemGetDto> dtos = new ArrayList<>();
        List<RefFormEntity> refEntities = refFormService.searchList();
        List<RefFormGetDto> refDtos = refEntities.stream().map(r -> RefFormGetDto.toDto(r)).collect(Collectors.toList());

        for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            JSONObject customDataJson = new JSONObject();
            JSONArray customDataJsonArr = new JSONArray();
            JSONObject customDetailJson = new JSONObject();

            for(int j = 0; j < NAVER_DELIVERY_READY_COL_SIZE; j++){
                customDataJson = new JSONObject();
                customDataJson.put("cid", j);
                customDataJson.put("id", UUID.randomUUID());

                String cellString = "";
                Cell cell = row.getCell(j);

                if(cell != null) {
                    if (row.getCell(j).getCellType() == CellType.STRING) {
                        cellString = cell.getStringCellValue();
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        if (DateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            cellString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(date);
                        } else {
                            cellString = String.valueOf((int)cell.getNumericCellValue());
                        }
                    }
                    customDataJson.put("origin_col_data", cellString);
                }
                customDataJson.put("ref_form_id", refDtos.get(j).getId());
                customDataJsonArr.add(customDataJson);
            }

            customDetailJson.put("details", customDataJsonArr);
            
            CustomDeliveryReadyItemGetDto dto = CustomDeliveryReadyItemGetDto.builder()
                .id(UUID.randomUUID())
                .deliveryReadyCustomItem(customDetailJson)
                .orderNumber(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                .prodOrderNumber(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "")
                .prodName(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                .optionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                .receiver(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                .destination(row.getCell(42) != null ? row.getCell(42).getStringCellValue() : "")
                .build();

            dtos.add(dto);
        }
        return dtos;
    }

    public List<CustomDeliveryReadyItemGetDto> storeDeliveryReadyExcelFile(List<CustomDeliveryReadyItemGetDto> dtos) {
        List<CustomDeliveryReadyItemEntity> entities = dtos.stream().map(dto -> CustomDeliveryReadyItemEntity.toEntity(dto)).collect(Collectors.toList());
        customDeliveryReadyRepository.createItem(entities);
        return dtos;
    }

    public List<CustomizedDeliveryReadyItemResDto> changeToCustomizedDeliveryReadyExcelFile(List<CustomizedDeliveryReadyItemResDto> dtos, Map<String, Object> query) {
        List<CustomizedDeliveryReadyItemResDto> changedDtos = new ArrayList<>();
        List<CustomTableHeaderEntity> headerEntities = customTableHeaderService.searchList(query);
        List<CustomTableHeaderGetDto> headerDtos = headerEntities.stream().map(r -> CustomTableHeaderGetDto.toDto(r)).collect(Collectors.toList());

        for(CustomizedDeliveryReadyItemResDto dto : dtos) {
            JSONArray details = this.objectToJsonArray(dto.getCustomizedDeliveryReadyItem().get("details"));
            JSONArray customizedArr = new JSONArray();

            // header를 돌면서 ref_form_id가 존재하지 않으면 jsonObject에 null값 추가.
            // ref_form_id 가 존재하면 details : [{}, {}, {}, ... {}] 67개 돌면서
            // ref_form_id 와 {}의 ref_form_id가 동일하지 않으면 다음 {}검사, 동일하면 origin_col_data jsonObject추가하고 break.
            for (CustomTableHeaderGetDto headerDto : headerDtos) {
                JSONObject customizedJson = new JSONObject();
                if (headerDto.getRefFormId() != null) {
                    for(int i = 0; i < NAVER_DELIVERY_READY_COL_SIZE; i++) {
                        JSONObject detail = this.objectToJsonObject(details.get(i));
                        String originColData = detail.get("origin_col_data") != null ? detail.get("origin_col_data").toString() : null;
                        UUID refFormId = detail.get("ref_form_id") != null ? UUID.fromString(detail.get("ref_form_id").toString()) : null;

                        if(headerDto.getRefFormId().equals(refFormId)) {
                            customizedJson.put("id", UUID.randomUUID());
                            customizedJson.put("custom_col_data", originColData);
                            customizedJson.put("ref_form_id", refFormId);
                            customizedArr.add(customizedJson);
                            break;
                        }
                    }
                } else {
                    customizedJson.put("id", UUID.randomUUID());
                    customizedJson.put("custom_col_data", null);
                    customizedJson.put("ref_form_id", null);
                    customizedArr.add(customizedJson);
                }
            }
                
            JSONObject detailObj = new JSONObject();
            detailObj.put("details", customizedArr);

            CustomizedDeliveryReadyItemResDto resDto = CustomizedDeliveryReadyItemResDto.builder()
                .id(UUID.randomUUID())
                .customizedDeliveryReadyItem(detailObj)
                .build();

            changedDtos.add(resDto);
        }

        return changedDtos;
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

    public List<CustomizedDeliveryReadyItemResDto> searchAllCustomDeliveryReadyItem(Map<String, Object> query) {
        UUID titleId = UUID.fromString(query.get("titleId").toString());
        List<String> resultList = customDeliveryReadyRepository.searchListDeliveryReadyCustomItem(titleId);
        List<CustomizedDeliveryReadyItemResDto> resDtos = new ArrayList<>();
        
        try{
            JSONParser parser = new JSONParser();
            for(String result : resultList) {
                Object obj = parser.parse(result);
                JSONObject jsonObj = (JSONObject) obj;
                
                CustomizedDeliveryReadyItemResDto resDto = CustomizedDeliveryReadyItemResDto.builder()
                    .id(UUID.randomUUID())
                    .customizedDeliveryReadyItem(jsonObj)
                    .build();
    
                resDtos.add(resDto);
            }
        } catch(ParseException e) {
        }

        List<CustomizedDeliveryReadyItemResDto> changeDtos =  this.changeToCustomizedDeliveryReadyExcelFile(resDtos, query);

        return changeDtos;
    }
}
