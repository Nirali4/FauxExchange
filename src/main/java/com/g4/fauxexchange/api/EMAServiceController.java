package com.g4.fauxexchange.api;

import com.g4.fauxexchange.service.EMAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EMAServiceController {
    
    @Autowired
    private EMAService emaService;

    @RequestMapping(value = "/api/ema", method = RequestMethod.GET)
    public ResponseEntity<Object> getEMAs() {
        return new ResponseEntity<Object>(emaService.getEMAs(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/ema/{code}", method = RequestMethod.GET)
    public ResponseEntity<Object> getEMAByCode(@PathVariable("code") String code) {
        return new ResponseEntity<Object>(emaService.getEMAResult(code), HttpStatus.OK);
    }

}