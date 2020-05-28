package com.g4.fauxexchange.model;

import java.util.LinkedList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Exponential Moving Average. 
 */ 
@Document
public class EMAResult { 

    //This is a unique ID for Database Storing
    @Id
    public String emaId;
    //Identifiers
    public String code;
    public String name;
    //Used to store the history of the EMA result
    //Algorithm will run, and store the result for that day.
    public LinkedList<Price> result;

    public EMAResult() {}

    //Used to create an the EMAResult of a cryptocurrency
    //Results are stored in the result.
    public EMAResult(String code, String name) {
        this.code = code;
        this.name = name;
        this.result = new LinkedList<Price>();
        this.result.add(new Price(0.0));
    }

    @Override
	public String toString() {
		return String.format("EMA[id=%s, code='%s', name='%s'] | %s", 
        emaId, code, name, result.peekLast());
	}

    // Getters & Setters
    public String getEMAId() {
        return this.emaId;
    }

    public void setEMAId(String emaId) {
        this.emaId = emaId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Add the most recent result in the EMA, given a result.
    public void update(Price result) {
        if(!this.result.isEmpty()) {
            result.setChange(result.getValue() - this.result.peekLast().getValue());
            this.result.add(result);
        }  
    }

    // Return the most recent price
    public double getRecentPrice() {
        return this.result.peekLast().getValue();
    }

    // Return the most recent change
    public double getRecentChange() {
        return this.result.peekLast().getChange();
    }
}