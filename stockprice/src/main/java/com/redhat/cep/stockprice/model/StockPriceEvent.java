package com.redhat.cep.stockprice.model;

import com.redhat.cep.util.BaseEvent;

public class StockPriceEvent extends BaseEvent {

    private String symbol;
    private String companyName;
    private double price;


    public StockPriceEvent(String symbol, String companyName, double price) {
    	super();
    	this.symbol = symbol;
    	this.companyName = companyName;
    	this.price = price;

    }


	public String getSymbol() {
		return symbol;
	}


	public String getCompanyName() {
		return companyName;
	}


	public double getPrice() {
		return price;
	}

	public String toString() {
		return super.toString() + String.format(" - symbol=%s, companyName=%s, price=%8.2f", symbol, companyName, price);
	}

}
