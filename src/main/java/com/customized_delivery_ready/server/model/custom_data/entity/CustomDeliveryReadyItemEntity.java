package com.customized_delivery_ready.server.model.custom_data.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.customized_delivery_ready.server.model.custom_data.dto.CustomDeliveryReadyItemGetDto;

import org.hibernate.annotations.Type;
import org.json.simple.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "custom_delivery_ready_item")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomDeliveryReadyItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    // @Convert(converter = DeliveryReadyCustomItemConverterDto.class)
    @Column(name = "delivery_ready_custom_item")
    private JSONObject deliveryReadyCustomItem;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "prod_order_number")
    private String prodOrderNumber;

    @Column(name = "prod_name")
    private String prodName;

    @Column(name = "option_info")
    private String optionInfo;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "destination")
    private String destination;

    public static CustomDeliveryReadyItemEntity toEntity(CustomDeliveryReadyItemGetDto dto) {
        CustomDeliveryReadyItemEntity entity = CustomDeliveryReadyItemEntity.builder()
            .id(dto.getId())
            .deliveryReadyCustomItem(dto.getDeliveryReadyCustomItem())
            .orderNumber(dto.getOrderNumber())
            .prodOrderNumber(dto.getProdOrderNumber())
            .prodName(dto.getProdName())
            .optionInfo(dto.getOptionInfo())
            .receiver(dto.getReceiver())
            .destination(dto.getDestination())
            .build();

        return entity;
    }
}
