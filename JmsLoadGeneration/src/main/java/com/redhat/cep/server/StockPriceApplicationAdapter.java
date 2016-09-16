package com.redhat.cep.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.redhat.cep.stockprice.application.StockPriceCepApplication;
import com.redhat.cep.util.BaseEvent;
import com.redhat.cep.util.ObservableEvent;

@Service(value = "processEvent")
public class StockPriceApplicationAdapter {

	private ObservableEvent observableEvent;
	private StockPriceCepApplication app;
	
	public StockPriceApplicationAdapter() {

		// KSESSION_NAME is the ksession name in the kmodule.xml in the META-INF directory
		// ENTRY_POINT is the event entry point name

		app = new StockPriceCepApplication(StockPriceCepApplication.KSESSION_NAME, StockPriceCepApplication.ENTRY_POINT);
		app.setSystemTimeMode();

		app.setGlobal("tickers", new HashSet(Arrays.asList(new String[] { "BHP", "CBA" })));
		List<String> list = new ArrayList<String>();
		app.setGlobal("list", list);
		app.setGlobal("clock", app.getWallClock());
		
		// change CSV file delimiter (default is comma)
		//simulator.changeDelimiter('\t');
		
		observableEvent = new ObservableEvent();
		observableEvent.addObserver(app);

	}
	
	public void processEvent(BaseEvent event) {
		observableEvent.setValue(event);

	}
}
