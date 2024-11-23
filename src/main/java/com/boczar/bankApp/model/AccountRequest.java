package com.boczar.bankApp.model;

import lombok.Data;

@Data
public class AccountRequest {
    private String name;
    private String surname;
    private double initialBalancePln;
}
