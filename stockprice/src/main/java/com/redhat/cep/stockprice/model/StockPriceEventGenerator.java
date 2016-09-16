package com.redhat.cep.stockprice.model;


import com.redhat.cep.util.BaseEvent;
import com.redhat.cep.util.EventGenerator;
import com.redhat.cep.util.NormalDistribution;

public class StockPriceEventGenerator implements EventGenerator {

	private String symbol;
	private String companyName;
	private double price;
	private NormalDistribution distr;
	
	public StockPriceEventGenerator(int seed, String symbol, String companyName, double price, double priceDifference) {
		this.symbol = symbol;
		this.companyName = companyName;
		this.price = price;

		distr = new NormalDistribution(seed, priceDifference, priceDifference);
	}
	
	public BaseEvent generateEvent() {

		return new StockPriceEvent(
				symbol,
				companyName,
				price + distr.sample()
				);
	}

}
