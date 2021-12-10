package com.customized_delivery_ready.server.model.custom_data.dto;

import java.util.Date;
import java.util.UUID;

import org.json.simple.JSONArray;

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
    private JSONArray customizedDeliveryReadyItem;
    // private UUID id;
    // private String prodOrderNumber;
    // private String orderNumber;
    // private String transportTypeOfBuyer;
    // private String transportType;
    // private String deliveryService;
    // private String transportNumber;
    // private Date shippedDate;
    // private String salesChannel;
    // private String buyer;
    // private String buyerId;
    // private String receiver;
    // private String orderStatus;
    // private String orderDetailStatus;
    // private String paymentDevice;      // 결제위치
    // private Date paymentDate;     // 결제일
    // private String prodNumber;
    // private String prodName;
    // private String prodType;        // 상품종류
    // private String optionInfo;
    // private String optionMangementCode;
    // private Integer unit;
    // private String optionPrice;
    // private String prodPrice;
    // private String discountByProduct;
    // private String discountBySeller;
    // private String totalPricePerProduct;
    // private String gift;
    // private Date orderConfirmationDate;
    // private Date shipmentDueDate;
    // private Date shipmentProcessingDate;
    // private Date invoiceOutputDate;       // 송장출력일
    // private String shipmentCostType;    // 배송비 형태
    // private String shipmentCostBundleNumber;        // 배송비 묶음번호
    // private String shipmentTotalCost;       // 배송비 합계
    // private String additionalShipmentCost;      // 제주/도서 추가배송비
    // private String discountOfShipmentCost;      // 배송비 할인액
    // private String sellerProdCode;      // 판매자 상품코드
    // private String sellerInnerCode;
    // private String sellerInnerCode2;
    // private String receiverContact1;
    // private String receiverContact2;
    // private String destination;
    // private String buyerContact;
    // private String zipCode;
    // private String deliveryMessage;
    // private String releaseAt;
    // private String paymentType;     // 결제수단
    // private String feeChargeClassification;       // 수수료 과금구분
    // private String feeChargeType;       // 수수료 결제방식
    // private String naverPayOrderManagementFee;      // 네이버페이 주문관리 수수료
    // private String salesRelatedFee;     // 매출연동 수수료
    // private String salesReleatedFeeClassification;      // 매출연동 수수료 구분
    // 정산 예정액
    // ~ (구매자연락처) 까지 작성해야함.



}
