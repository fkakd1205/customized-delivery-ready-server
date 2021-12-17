package com.customized_delivery_ready.server.model.customized_excel.dto;

import java.util.UUID;

import org.json.simple.JSONObject;

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
public class CustomizedExcelHeaderDto {
    private Integer cid;
    private UUID id;
    private String uploadHeaderTitle;
    private String downloadHeaderTitle;
    private JSONObject uploadHeaderDetail;
    private JSONObject downloadHeaderDetail;
    private Integer startRowNumber;
}
