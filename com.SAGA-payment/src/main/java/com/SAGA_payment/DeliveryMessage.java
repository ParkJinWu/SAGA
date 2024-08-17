package com.SAGA_payment;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMessage {
    // UUID는 겹칠 가능성이 낮기 때문에 final로 선언X
    private UUID orderID;
    private UUID payment;

    //요청자 ID
    private String userId;

    //상품의 ID
    private Integer productId;

    //상품 구매 수량
    private Integer productQuantity;

    //지불 금액
    private Integer payAmount;

    //에러 사유
    private String errorType;
}
