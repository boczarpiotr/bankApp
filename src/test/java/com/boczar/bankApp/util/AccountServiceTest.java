package com.boczar.bankApp.util;

import com.boczar.bankApp.model.AccountRequest;
import com.boczar.bankApp.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    RateService rateService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testCreatingOfAccount() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Piotr");
        accountRequest.setSurname("Kowalski");
        accountRequest.setInitialBalancePln(100);

        ResponseEntity<String> account = accountService.createAccount(accountRequest);

        Assertions.assertEquals("{\"Message\": \"Account has been created for id: 1\"}", account.getBody());
    }

    @Test
    void testCreatingOfAccountWithNoAmount() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Piotr");
        accountRequest.setSurname("Kowalski");
        accountRequest.setInitialBalancePln(0);

        ResponseEntity<String> account = accountService.createAccount(accountRequest);

        Assertions.assertEquals(account.getBody(), "{\"Error\": \"Amount has to be greater than 0\"}");
    }

    @Test
    void testExchangeFromPlnToUsd() throws JsonProcessingException {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName("Piotr");
        accountRequest.setSurname("Kowalski");
        accountRequest.setInitialBalancePln(100);

        accountService.createAccount(accountRequest);

        ResponseEntity<String> stringResponseEntity = rateService.changePlnToUsd(1L, 50.00);

        double balancePln = objectMapper.readTree(stringResponseEntity.getBody()).get("balancePln").asDouble();

        Assertions.assertEquals(50.00, balancePln);
    }
}
