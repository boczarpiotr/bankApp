package com.boczar.bankApp.util;

import com.boczar.bankApp.model.Account;
import com.boczar.bankApp.model.AccountRequest;
import com.boczar.bankApp.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ObjectMapper objectMapper;

    public ResponseEntity<String> createAccount(AccountRequest accountRequest) {
        Account account = new Account();
        account.setName(accountRequest.getName());
        account.setSurname(accountRequest.getSurname());
        account.setBalancePln(accountRequest.getInitialBalancePln());

        accountRepository.save(account);
        return ResponseEntity.status(201).body("{\"Message\": \"Account has been created for id: " + account.getId() + "\"}");
    }

    public ResponseEntity<String> getAccountById(Long id) throws JsonProcessingException {
        Optional<Account> referenceById = accountRepository.findById(id);

        if (referenceById.isPresent()) {
            String s = objectMapper.writeValueAsString(referenceById.get());
            return ResponseEntity.status(200).body(s);
        } else {
            return ResponseEntity.status(404).body("{\"Error\": \"No data for id: " + id + "\"}");
        }
    }
}
