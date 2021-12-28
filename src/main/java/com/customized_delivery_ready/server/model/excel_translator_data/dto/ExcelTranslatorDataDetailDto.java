package com.customized_delivery_ready.server.model.excel_translator_data.dto;

import java.util.List;


import org.hibernate.annotations.Type;

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
public class ExcelTranslatorDataDetailDto {
    @Type(type = "jsonb")
    List<TranslatedDetailDto> details;
}
