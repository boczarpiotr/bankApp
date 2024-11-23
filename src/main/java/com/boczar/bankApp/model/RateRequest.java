package com.boczar.bankApp.model;

import lombok.Data;

@Data
public class RateRequest {
    private Long id;
    private double amountForExchange;
}