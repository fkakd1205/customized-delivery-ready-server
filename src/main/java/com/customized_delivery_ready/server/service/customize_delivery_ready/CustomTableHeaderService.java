package com.customized_delivery_ready.server.service.customize_delivery_ready;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.customized_delivery_ready.server.model.custom_table_header.dto.CustomTableHeaderGetDto;
import com.customized_delivery_ready.server.model.custom_table_header.entity.CustomTableHeaderEntity;
import com.customized_delivery_ready.server.model.custom_table_header.repository.CustomTableHeaderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomTableHeaderService {
    
    @Autowired
    private CustomTableHeaderRepository customTableHeaderRepository;

    public CustomTableHeaderEntity createOne(CustomTableHeaderGetDto dto) {
        CustomTableHeaderEntity entity = CustomTableHeaderEntity.toEntity(dto);
        return customTableHeaderRepository.save(entity);
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

    // title 전체 조회용도
    public List<CustomTableHeaderEntity> searchList() {
        return customTableHeaderRepository.findAll();
    }
}
