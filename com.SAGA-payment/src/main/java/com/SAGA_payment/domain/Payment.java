package com.SAGA_payment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Payment {
    //TODO : UUID -> Entity
    private UUID paymentId;
    private String userId;
    private Integer payAmount;
    private String payStatus;
}
