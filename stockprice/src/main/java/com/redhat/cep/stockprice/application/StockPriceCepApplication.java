package com.redhat.cep.stockprice.application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.redhat.cep.util.CepApplication;
import com.redhat.cep.util.DirectLoadGenerator;
import com.redhat.cep.util.ObjectCreator;
import com.redhat.cep.util.ObservableEvent;
import com.redhat.cep.util.ReflectionBasedObjectCreator;

public class StockPriceCepApplication extends CepApplication {

	public static final String ENTRY_POINT = "stockprice";
	public static final String FACT_EVENT_PACKAGE = "com.redhat.cep.stockprice.model";
	public static final String KSESSION_NAME = "SimKS";

	// setup the application
	public StockPriceCepApplication(String ksessionName, String entryPointName) 
	{
		super(ksessionName, entryPointName);

	}

	
	// print statistics 
	public void printStats(List<String> list)
	{

	    System.out.println( "List size: " + list.size() );


	    for (String msg : list) {
	    	System.out.println(msg);
	    }

	}
	


	// main program to setup and run the CEP application
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		// args[0] contains the csv file path info

		StockPriceCepApplication simulator = new StockPriceCepApplication(KSESSION_NAME, ENTRY_POINT);

		simulator.setGlobal("tickers", new HashSet(Arrays.asList(new String[] { "BHP", "CBA" })));
		List<String> list = new ArrayList<String>();
		simulator.setGlobal("list", list);
		simulator.setGlobal("clock", simulator.getWallClock());
		
		// change CSV file delimiter (default is comma)
		//simulator.changeDelimiter('\t');
		ObservableEvent observableEvent = new ObservableEvent();
		observableEvent.addObserver(simulator);
		
		// set up fact/event object creator
		ObjectCreator creator = new ReflectionBasedObjectCreator(FACT_EVENT_PACKAGE);
		
		DirectLoadGenerator loadGenerator = new DirectLoadGenerator(new FileReader(args[0]), creator, observableEvent);

		// run application
		loadGenerator.run();
		
		// output statistics for application
		simulator.printStats(list);
		
		// cleanup
		simulator.shutdown();
	}




}
