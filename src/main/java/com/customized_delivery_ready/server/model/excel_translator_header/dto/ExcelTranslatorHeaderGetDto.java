package com.customized_delivery_ready.server.model.excel_translator_header.dto;

import java.util.UUID;

import com.customized_delivery_ready.server.model.excel_translator_header.entity.ExcelTranslatorHeaderEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.TypeDef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExcelTranslatorHeaderGetDto {
    private Integer cid;
    private UUID id;
    private String uploadHeaderTitle;
    private String downloadHeaderTitle;
    private ExcelTranslatorHeaderDetailDto uploadHeaderDetail = new ExcelTranslatorHeaderDetailDto();
    private ExcelTranslatorHeaderDetailDto downloadHeaderDetail = new ExcelTranslatorHeaderDetailDto();
    private Integer rowStartNumber;

    public static ExcelTranslatorHeaderGetDto toEntity(ExcelTranslatorHeaderEntity entity) {
        ExcelTranslatorHeaderGetDto dto = ExcelTranslatorHeaderGetDto.builder()
            .id(entity.getId())
            .uploadHeaderTitle(entity.getUploadHeaderTitle())
            .downloadHeaderTitle(entity.getDownloadHeaderTitle())
            .uploadHeaderDetail(entity.getUploadHeaderDetail())
            .downloadHeaderDetail(entity.getDownloadHeaderDetail())
            .rowStartNumber(entity.getRowStartNumber())
            .build();

        return dto;
    }
}
