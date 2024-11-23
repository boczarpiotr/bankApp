package com.boczar.bankApp.util;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class RateService {

    public double getUsdRate() {
        return (Double) ((List<Map<String, Object>>) new RestTemplate()
                .getForObject("https://api.nbp.pl/api/exchangerates/rates/a/usd/", Map.class)
                .get("rates")).get(0).get("mid");
    }
}
