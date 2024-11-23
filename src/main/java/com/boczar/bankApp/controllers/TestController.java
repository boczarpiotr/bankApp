package com.boczar.bankApp.controllers;

import com.boczar.bankApp.model.AccountRequest;
import com.boczar.bankApp.repository.AccountRepository;
import com.boczar.bankApp.util.AccountService;
import com.boczar.bankApp.util.RateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RateService rateService;
    @Autowired
    AccountService accountService;


    @GetMapping(value = "/getAccount/{id}",
    produces = "application/json")
    public String getAccount(@PathVariable Long id) throws JsonProcessingException {
        return accountService.getAccountById(id);
    }

    @PostMapping("/createAccount")
    public String createAccount(@RequestBody AccountRequest accountRequest) {

        return accountService.createAccount(accountRequest);
    }

    @GetMapping("/getUsdRate")
    public String getUsdRate() {
        double usdRate = rateService.getUsdRate();

        return "USD rate is : " + usdRate;
    }
}
