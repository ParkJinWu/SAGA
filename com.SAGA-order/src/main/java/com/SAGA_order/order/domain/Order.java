package com.SAGA_order.order.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
@ToString
//TODO : @Entity로 DB저장, Status -> Enum
public class Order {
    private UUID orderId;
    private String userId;
    private String orderStatus;
    private String errorType;

    public void cancleOrder(String receiveErrorType){
        orderStatus = "CANCELED";
        errorType = receiveErrorType;
    }
}
