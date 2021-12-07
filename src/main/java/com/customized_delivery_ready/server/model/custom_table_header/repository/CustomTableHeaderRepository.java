package com.customized_delivery_ready.server.model.custom_table_header.repository;

import java.util.Optional;
import java.util.UUID;

import com.customized_delivery_ready.server.model.custom_table_header.entity.CustomTableHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomTableHeaderRepository extends JpaRepository<CustomTableHeaderEntity, Integer>{
    Optional<CustomTableHeaderEntity> findById(UUID id);
}
