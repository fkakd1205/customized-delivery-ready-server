package com.customized_delivery_ready.server.service.customize_delivery_ready;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.customized_delivery_ready.server.model.custom_data.dto.CustomDeliveryReadyItemGetDto;
import com.customized_delivery_ready.server.model.custom_data.entity.CustomDeliveryReadyItemEntity;
import com.customized_delivery_ready.server.model.custom_data.repository.CustomDeliveryReadyRepository;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CustomDeliveryReadyDataService {

    final int NAVER_DELIVERY_READY_COL_SIZE = 67;
    final String[] dateFormatCellNum = {"6", "14", "27", "28", "29", "30", "56", "60"};

    @Autowired
    private CustomDeliveryReadyRepository customDeliveryReadyRepository;
   
    public List<CustomDeliveryReadyItemGetDto> uploadDeliveryReadyExcelFile(MultipartFile file) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        // TODO : 타입체크 메서드 구현해야됨.
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

        for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            JSONObject customDataJson = new JSONObject();
            JSONArray customDataJsonArr = new JSONArray();
            JSONObject customDetailJson = new JSONObject();
            
            for(int j = 0; j < NAVER_DELIVERY_READY_COL_SIZE; j++){
                customDataJson = new JSONObject();
                customDataJson.put("cid", j);
                customDataJson.put("id", UUID.randomUUID());

                if(row.getCell(j) != null && row.getCell(j).getCellType() == CellType.STRING) {
                    customDataJson.put("origin_col_data", row.getCell(j) != null ? row.getCell(j).getStringCellValue() : "");
                }
                else if(row.getCell(j) != null && row.getCell(j).getCellType() == CellType.NUMERIC) {
                    if(Arrays.asList(dateFormatCellNum).contains(Integer.toString(j))){
                        customDataJson.put("origin_col_data", row.getCell(j) != null ? row.getCell(j).getDateCellValue() : "");
                    }else{
                        customDataJson.put("origin_col_data", row.getCell(j) != null ? row.getCell(j).getNumericCellValue() : "");
                    }
                }
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
}
