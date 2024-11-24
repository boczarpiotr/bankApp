package com.boczar.bankApp.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExchangeControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Test
    void testOf1000Id() throws Exception {
        mockMvc.perform(get("/getAccount/10000"))
                .andExpect(content().string(containsString("{\"Error\": \"No data for id: 10000\"}")));
    }

    @Test
    void creationAccountTest() throws Exception {
        mockMvc.perform(post("/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Piotr\",\"surname\":\"Kowalski\",\"initialBalancePln\":100}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("{\"Message\": \"Account has been created for id: 1\"}")));

    }

    @Test
    void notFoundExchangeTest() throws Exception {
        mockMvc.perform(post("/exchangeFromPln")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1000,\"amountForExchange\":50}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void exchangeTest() throws Exception {
        mockMvc.perform(post("/createAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Piotr\",\"surname\":\"Kowalski\",\"initialBalancePln\":100}"));

        mockMvc.perform(post("/exchangeFromPln")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"amountForExchange\":50}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"id\":1,\"name\":\"Piotr\",\"surname\":\"Kowalski\",\"balancePln\":50.0")));
    }
}