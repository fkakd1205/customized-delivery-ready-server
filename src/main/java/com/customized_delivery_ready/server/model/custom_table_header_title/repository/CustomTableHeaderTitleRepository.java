package com.customized_delivery_ready.server.model.custom_table_header_title.repository;

import com.customized_delivery_ready.server.model.custom_table_header_title.entity.CustomTableHeaderTitleEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomTableHeaderTitleRepository extends JpaRepository<CustomTableHeaderTitleEntity, Integer>{
    
}
