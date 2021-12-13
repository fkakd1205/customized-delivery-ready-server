package com.customized_delivery_ready.server.model.custom_data.dto;

import java.util.UUID;

import org.json.simple.JSONObject;

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
public class CustomizedDeliveryReadyItemResDto {
    private UUID id;
    private JSONObject customizedDeliveryReadyItem;
}
