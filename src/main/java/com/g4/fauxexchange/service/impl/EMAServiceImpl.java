package com.g4.fauxexchange.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.apache.commons.io.IOUtils;
import org.json.*;

import com.g4.fauxexchange.model.Currency;
import com.g4.fauxexchange.model.Price;
import com.g4.fauxexchange.model.EMAResult;
import com.g4.fauxexchange.service.EMAService;
import com.g4.fauxexchange.dao.CurrencyRepository;
import com.g4.fauxexchange.dao.EMARepository;

@Service
public class EMAServiceImpl implements EMAService {

    @Autowired
    private EMARepository emaRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public void createEMAResult(EMAResult emaResult) {
        emaRepository.save(emaResult);
    }

    // Algorithm should work here
    @Override
    @Scheduled(fixedRate=60000, initialDelay = 70000)
    public void runEMAAlgorithm() {
        System.out.println("- Running EMA on all Currencies -");
        for(EMAResult currEMA : emaRepository.findAll()) {
            System.out.printf("Current EMA - %s\n", currEMA.getCode());
            Currency currency = currencyRepository.findByCode(currEMA.getCode());
            LinkedList<Price> cPrices = currency.getPrice();
            // prices
        }
    }

    // Don't work on this function
    @Override
    public void deleteEMAResult(EMAResult emaResult) {
        emaRepository.delete(emaResult);
    } 

    // Return Recent EMA Results
    @Override
    public List<EMAResult> getEMAs() {
        return emaRepository.findAllWithRecentPrices();
    }

    // Return History of a specific EMA
    @Override
    public EMAResult getEMAResult(String code) {
        return emaRepository.findByCode(code);
    }

}