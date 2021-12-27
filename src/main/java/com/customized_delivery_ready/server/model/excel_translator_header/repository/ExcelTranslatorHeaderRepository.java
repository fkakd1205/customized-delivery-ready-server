package com.customized_delivery_ready.server.model.excel_translator_header.repository;

import com.customized_delivery_ready.server.model.excel_translator_header.entity.ExcelTranslatorHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelTranslatorHeaderRepository extends JpaRepository<ExcelTranslatorHeaderEntity, Integer> {
}
