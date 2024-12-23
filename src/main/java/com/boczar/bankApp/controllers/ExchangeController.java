package com.boczar.bankApp.controllers;

import com.boczar.bankApp.model.AccountRequest;
import com.boczar.bankApp.model.RateRequest;
import com.boczar.bankApp.repository.AccountRepository;
import com.boczar.bankApp.util.AccountService;
import com.boczar.bankApp.util.RateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExchangeController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RateService rateService;
    @Autowired
    AccountService accountService;

    @GetMapping(value = "/getAccount/{id}",
            produces = "application/json")
    public ResponseEntity<String> getAccount(@PathVariable Long id) throws JsonProcessingException {
        return accountService.getAccountById(id);
    }

    @PostMapping(value = "/createAccount",
            produces = "application/json")
    public ResponseEntity<String> createAccount(@RequestBody AccountRequest accountRequest) {
        return accountService.createAccount(accountRequest);
    }

    @PostMapping(value = "/exchangeFromPln",
            produces = "application/json")
    public ResponseEntity<String> exchangeFromPln(@RequestBody RateRequest rateRequest) throws JsonProcessingException {
        return rateService.changePlnToUsd(rateRequest.getId(), rateRequest.getAmountForExchange());
    }

    @PostMapping(value = "/exchangeFromUsd",
            produces = "application/json")
    public ResponseEntity<String> exchangeFromUsd(@RequestBody RateRequest rateRequest) throws JsonProcessingException {
        return rateService.changeUsdToPln(rateRequest.getId(), rateRequest.getAmountForExchange());
    }
}
