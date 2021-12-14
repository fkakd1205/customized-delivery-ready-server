package com.customized_delivery_ready.server.service.customize_delivery_ready;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.customized_delivery_ready.server.model.custom_table_header.dto.CustomTableHeaderGetDto;
import com.customized_delivery_ready.server.model.custom_table_header.entity.CustomTableHeaderEntity;
import com.customized_delivery_ready.server.model.custom_table_header.repository.CustomTableHeaderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomTableHeaderService {
    
    @Autowired
    private CustomTableHeaderRepository customTableHeaderRepository;

    public List<CustomTableHeaderEntity> createList(List<CustomTableHeaderGetDto> dtos) {
        List<CustomTableHeaderEntity> entities = new ArrayList<>();

        for(CustomTableHeaderGetDto dto : dtos) {
            CustomTableHeaderEntity entity = CustomTableHeaderEntity.toEntity(dto);
            entities.add(entity);
        }
        return customTableHeaderRepository.saveAll(entities);
    }

    public CustomTableHeaderEntity searchOne(Map<String, Object> query) {
        UUID headerId = UUID.fromString(query.get("headerId").toString());
        Optional<CustomTableHeaderEntity> entityOpt = customTableHeaderRepository.findById(headerId);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    public List<CustomTableHeaderEntity> searchList() {
        return customTableHeaderRepository.findAll();
    }

    public void updateList(List<CustomTableHeaderGetDto> dtos) {
        List<CustomTableHeaderEntity> entities = dtos.stream().map(dto -> CustomTableHeaderEntity.toEntity(dto)).collect(Collectors.toList());
        
        for(CustomTableHeaderEntity entity : entities) {
            customTableHeaderRepository.findById(entity.getId()).ifPresent(item -> {
                item.setTitle(entity.getTitle())
                    .setCustomColName(entity.getCustomColName())
                    .setRefFormId(entity.getRefFormId());

                customTableHeaderRepository.save(item);
            });
        }
    }
}
