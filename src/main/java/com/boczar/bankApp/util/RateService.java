package com.boczar.bankApp.util;

import com.boczar.bankApp.model.Account;
import com.boczar.bankApp.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class RateService {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    public double getUsdRate() {
        return (Double) ((List<Map<String, Object>>) new RestTemplate()
                .getForObject("https://api.nbp.pl/api/exchangerates/rates/a/usd/", Map.class)
                .get("rates")).get(0).get("mid");
    }

    public ResponseEntity<String> changePlnToUsd(Long id, Double amountInPln) throws JsonProcessingException {

        double balancePln = accountRepository.getReferenceById(id).getBalancePln();

        if (amountInPln > balancePln){
            return ResponseEntity.status(403).body("{\"Error\": \"Not enough money for that operation\"}");
        }
        return null;
    }
}
