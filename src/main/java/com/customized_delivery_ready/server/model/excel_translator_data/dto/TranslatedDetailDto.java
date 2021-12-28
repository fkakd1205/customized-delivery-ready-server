package com.customized_delivery_ready.server.model.excel_translator_data.dto;

import java.util.UUID;

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
public class TranslatedDetailDto {
    private UUID id;
    private String headerName;
    private String originColData;
    private Integer targetCellNumber;
}
