package com.boczar.bankApp.util;

import com.boczar.bankApp.model.Account;
import com.boczar.bankApp.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RateService {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ObjectMapper objectMapper;

    public ResponseEntity<String> changePlnToUsd(Long id, Double amountInPln) throws JsonProcessingException {
        if (amountInPln <= 0){
            return getGreaterThanZeroResponse();
        }
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isEmpty()) {
            return accountService.getNoFoundResponse(id);
        }

        Account referenceById = optionalAccount.get();

        BigDecimal balanceInPln = BigDecimal.valueOf(referenceById.getBalancePln()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountInPlnBigDecimal = BigDecimal.valueOf(amountInPln).setScale(2, RoundingMode.HALF_UP);

        if (amountInPlnBigDecimal.compareTo(balanceInPln) > 0) {
            return noSufficientFunds();
        }

        BigDecimal currentUsdBalance = BigDecimal.valueOf(referenceById.getBalanceUsd()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal usdAmount = BigDecimal.valueOf(getUsdBalance(amountInPln));

        BigDecimal updatedUsdBalance = currentUsdBalance.add(usdAmount).setScale(2, RoundingMode.HALF_UP);
        BigDecimal updatedPlnBalance = balanceInPln.subtract(amountInPlnBigDecimal).setScale(2, RoundingMode.HALF_UP);

        referenceById.setBalanceUsd(updatedUsdBalance.doubleValue());
        referenceById.setBalancePln(updatedPlnBalance.doubleValue());

        accountRepository.save(referenceById);

        return getAccountInJson(id);
    }

    public ResponseEntity<String> changeUsdToPln(Long id, Double amountInUsd) throws JsonProcessingException {
        if (amountInUsd <= 0){
            return getGreaterThanZeroResponse();
        }
        Optional<Account> optionalAccount = accountRepository.findById(id);

        if (optionalAccount.isEmpty()) {
            return accountService.getNoFoundResponse(id);
        }

        Account referenceById = optionalAccount.get();

        BigDecimal balanceInUsd = BigDecimal.valueOf(referenceById.getBalanceUsd()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountInUsdBigDecimal = BigDecimal.valueOf(amountInUsd).setScale(2, RoundingMode.HALF_UP);

        if (amountInUsdBigDecimal.compareTo(balanceInUsd) > 0) {
            return noSufficientFunds();
        }

        BigDecimal currentPlnBalance = BigDecimal.valueOf(referenceById.getBalancePln()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal plnAmount = BigDecimal.valueOf(getPlnBalance(amountInUsd));

        BigDecimal updatedPlnBalance = currentPlnBalance.add(plnAmount).setScale(2, RoundingMode.HALF_UP);
        BigDecimal updatedUsdBalance = balanceInUsd.subtract(amountInUsdBigDecimal).setScale(2, RoundingMode.HALF_UP);

        referenceById.setBalancePln(updatedPlnBalance.doubleValue());
        referenceById.setBalanceUsd(updatedUsdBalance.doubleValue());

        accountRepository.save(referenceById);

        return getAccountInJson(id);
    }


    public ResponseEntity<String> noSufficientFunds() {
        return ResponseEntity.status(403).body("{\"Error\": \"You do not have sufficient funds for that operation\"}");
    }

    public double getUsdBalance(Double amountInPln) {
        return BigDecimal.valueOf(amountInPln)
                .divide(BigDecimal.valueOf(getUsdRate()), 2, RoundingMode.HALF_UP).doubleValue();
    }

    public double getPlnBalance(Double amountInUsd) {
        BigDecimal amount = BigDecimal.valueOf(amountInUsd);
        BigDecimal usdRate = BigDecimal.valueOf(getUsdRate());
        return amount.multiply(usdRate).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public double getUsdRate() {
        return (Double) ((List<Map<String, Object>>) new RestTemplate().getForObject("https://api.nbp.pl/api/exchangerates/rates/a/usd/", Map.class).get("rates")).get(0).get("mid");
    }

    public ResponseEntity<String> getAccountInJson(Long id) throws JsonProcessingException {
        Optional<Account> accountAfterChange = accountRepository.findById(id);
        return ResponseEntity.status(200).body(objectMapper.writeValueAsString(accountAfterChange.get()));
    }
    public ResponseEntity<String> getGreaterThanZeroResponse(){
        return ResponseEntity.status(403).body("{\"Error\": \"Amount has to be greater than 0\"}");
    }
}
