package com.g4.fauxexchange.service.impl;

import java.util.List;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.apache.commons.io.IOUtils;
import org.json.*;

import com.g4.fauxexchange.model.Currency;
import com.g4.fauxexchange.model.Price;
import com.g4.fauxexchange.service.CurrencyService;
import com.g4.fauxexchange.dao.CurrencyRepository;


@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    /* Create Currency
    Based on a given currency we can create a new currency to save to the database */
    @Override
    public void createCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    /* Update Currency
    This function pulls data from a API and sets the currencies price to the data.
    All other data is calculated within this function. Eg, timestamp and change  */
    @Override
    @Scheduled(fixedRate=60000, initialDelay = 60000)
    public void updateCurrency() {
        System.out.println("- Updating Currencies -");
        for(Currency currency : currencyRepository.findAll()) {
            JSONObject json = null;
            try {
                System.setProperty("http.agent", "curl/7.51.0");
                json = new JSONObject(
                    IOUtils.toString(
                        new URL("https://api.cryptonator.com/api/ticker/"+ currency.getCode() + "-AUD")));
            } catch(Exception e) {
                e.printStackTrace();
            }
            try{
                if(json != null) {
                    double priceAPI = json.getJSONObject("ticker").getDouble("price");
                    Price price = new Price(priceAPI);
                    currency.update(price);
                    currencyRepository.save(currency);
                }
            } catch(JSONException je) {
                je.printStackTrace();
            }

            System.out.println("Updated: " + currency);
        }
    }

    /* Delete Currency
    Based on a given currency remove it fomr the database */
    @Override
    public void deleteCurrency(Currency currency) {
        currencyRepository.delete(currency);
    }

    /* Get All currencies */
    @Override
    public List<Currency> getCurrencies() {
        return currencyRepository.findAllWithRecentPrices();
    }

    /* Get specific currency */
    @Override
    public Currency getCurrency(String code) {
        return currencyRepository.findByCode(code);
    }

}
