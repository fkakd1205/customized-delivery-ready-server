package com.customized_delivery_ready.server.model.custom_data.dto;

import java.util.UUID;

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
public class DeliveryReadyCustomItemGetDto {
    private Integer cid;
    private UUID id;
    private String customColData;
    private UUID refFormId;
}
