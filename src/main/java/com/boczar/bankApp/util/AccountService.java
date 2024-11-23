package com.boczar.bankApp.util;

import com.boczar.bankApp.model.Account;
import com.boczar.bankApp.model.AccountRequest;
import com.boczar.bankApp.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    Account account;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ObjectMapper objectMapper;

    public String createAccount(AccountRequest accountRequest) {
        account.setName(accountRequest.getName());
        account.setSurname(accountRequest.getSurname());
        account.setBalancePln(accountRequest.getInitialBalancePln());

        accountRepository.save(account);
        return "Account has been created for id: " + account.getId();
    }

    public String getAccountById(Long id) {
        return accountRepository.findById(id)
                .map(account -> {
                    try {
                        return objectMapper.writeValueAsString(account);
                    } catch (JsonProcessingException e) {
                        return "Error processing the account data.";
                    }
                })
                .orElse("Account not found for ID: " + id);
    }
}
