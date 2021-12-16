package com.customized_delivery_ready.server.model.custom_table_header_title.dto;

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
public class CustomTableHeaderTitleGetDto {
    private UUID id;
    private String title;
}
