package com.customized_delivery_ready.server.service.customize_delivery_ready;

import java.util.List;

import com.customized_delivery_ready.server.model.custom_table_header.dto.CustomTableHeaderGetDto;
import com.customized_delivery_ready.server.model.custom_table_header.entity.CustomTableHeaderEntity;
import com.customized_delivery_ready.server.model.custom_table_header.repository.CustomTableHeaderRepository;
import com.customized_delivery_ready.server.model.custom_table_header_title.dto.CustomTableHeaderTitleGetDto;
import com.customized_delivery_ready.server.model.custom_table_header_title.entity.CustomTableHeaderTitleEntity;
import com.customized_delivery_ready.server.model.custom_table_header_title.repository.CustomTableHeaderTitleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReadyCustomTableHeaderTitleService {
    
    @Autowired
    private CustomTableHeaderTitleRepository customTableHeaderTitleRepository;

    public List<CustomTableHeaderTitleEntity> searchList() {
        return customTableHeaderTitleRepository.findAll();
    }

    public void createOne(CustomTableHeaderTitleGetDto dto) {
        CustomTableHeaderTitleEntity entity = CustomTableHeaderTitleEntity.toEntity(dto);
        customTableHeaderTitleRepository.save(entity);
    }
}
