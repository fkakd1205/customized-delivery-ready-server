package com.customized_delivery_ready.server.model.custom_table_header.dto;

import java.util.UUID;

import com.customized_delivery_ready.server.model.custom_table_header.entity.CustomTableHeaderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomTableHeaderGetDto {
    private Integer cid;
    private UUID id;
    private String title;
    private String customColName;
    private UUID refFormId;
    private UUID customTableHeaderTitleId;

    public static CustomTableHeaderGetDto toDto(CustomTableHeaderEntity entity) {
        CustomTableHeaderGetDto dto = CustomTableHeaderGetDto.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .customColName(entity.getCustomColName())
            .refFormId(entity.getRefFormId())
            .customTableHeaderTitleId(entity.getCustomTableHeaderTitleId())
            .build();

        return dto;
    }
}
