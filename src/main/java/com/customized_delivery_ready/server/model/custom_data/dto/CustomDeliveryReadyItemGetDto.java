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
public class CustomDeliveryReadyItemGetDto {
    private Integer cid;
    private UUID id;
    private JSONObject deliveryReadyCustomItem;
    private String orderNumber;
    private String prodOrderNumber;
    private String prodName;
    private String optionInfo;
    private String receiver;
    private String destination;
}
