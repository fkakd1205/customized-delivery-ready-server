package com.customized_delivery_ready.server.service.excel_translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Convert;

import com.customized_delivery_ready.server.model.excel_translator_header.dto.DetailDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorHeaderDetailDto;
import com.customized_delivery_ready.server.model.excel_translator_header.dto.ExcelTranslatorHeaderGetDto;
import com.customized_delivery_ready.server.model.excel_translator_header.entity.ExcelTranslatorHeaderEntity;
import com.customized_delivery_ready.server.model.excel_translator_header.repository.ExcelTranslatorHeaderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelTranslatorHeaderService {

    @Autowired
    private ExcelTranslatorHeaderRepository excelTranslatorHeaderRepository;

    // @Autowired
    // private ExcelTranslatorHeaderTestRepository excelTranslatorHeaderTestRepository;

    public ExcelTranslatorHeaderGetDto createOne(ExcelTranslatorHeaderGetDto dto) {
        ExcelTranslatorHeaderEntity entity = ExcelTranslatorHeaderEntity.toEntity(dto);
        excelTranslatorHeaderRepository.save(entity);
        return dto;
    }
    
    // public ExcelTranslatorHeaderEntity createOne(ExcelTranslatorHeaderGetDto dto) {
    //     ExcelTranslatorHeaderEntity entity = ExcelTranslatorHeaderEntity.toEntity(dto);
    //     excelTranslatorHeaderRepository.createOne(entity);
    //     return entity;
    // }

    // public List<ExcelTranslatorHeaderEntity> searchList() {
    //     List<Object[]> objs = excelTranslatorHeaderRepository.searchList();

    //     List<ExcelTranslatorHeaderEntity> entities = new ArrayList<>();
    //     for (Object[] row : objs) {
    //         try {
    //             JSONParser parser = new JSONParser();
    //             Object obj = parser.parse(row[4].toString());
    //             Object obj2 = parser.parse(row[5].toString());
    //             JSONObject jsonObj = (JSONObject)obj;
    //             JSONObject jsonObj2 = (JSONObject)obj2;

    //             ExcelTranslatorHeaderEntity entity = ExcelTranslatorHeaderEntity.builder()
    //                     .id(UUID.fromString(row[1].toString()))
    //                     .uploadHeaderTitle(row[2].toString())
    //                     .downloadHeaderTitle(row[3].toString())
    //                     .uploadHeaderDetail(jsonObj)
    //                     .downloadHeaderDetail(jsonObj2)
    //                     .rowStartNumber((int)row[6])
    //                     .build();

    //             entities.add(entity);
    //         } catch (ParseException e) {
    //         }
    //     }

    //     return entities;
    // }

    // public JSONObject objectToJsonObject(Object object){
    //     JSONObject jsonObject = new JSONObject();
    //     if (object instanceof Map){
    //         jsonObject.putAll((Map)object);
    //     }
    //     return jsonObject;
    // }

    // public JSONArray objectToJsonArray(Object object){
    //     JSONArray jsonArray = new JSONArray();
    //     if (object instanceof Map){
    //         JSONObject jsonObject = new JSONObject();
    //         jsonObject.putAll((Map)object);
    //         jsonArray.add(jsonObject);
    //     }
    //     else if (object instanceof List){
    //         jsonArray.addAll((List)object);
    //     }
    //     return jsonArray;
    // }

    // public List<ExcelTranslatorHeaderEntity> searchListTest() {
    //     List<ExcelTranslatorHeaderEntity> entities = excelTranslatorHeaderRepository.findAll();
    //     System.out.println(entities);
    //     return entities;
    // }
}
