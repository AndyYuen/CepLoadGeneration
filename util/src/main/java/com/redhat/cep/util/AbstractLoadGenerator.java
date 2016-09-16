package com.redhat.cep.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;



public abstract class AbstractLoadGenerator {
	static Logger logger = Logger.getLogger(AbstractLoadGenerator.class);
	
	public static final String TYPE = "type";
	public static final String CLASS_NAME = "className";
	public static final String ARRIVAL_DISTR_REF = "arrivalDistrRef";
	public static final String SERVICE_DISTR_REF = "serviceDistrRef";
	public static final String TIME = "time";
	public static final char T_EVENT = 'E';
	public static final char T_DISTR = 'D';
	public static final char T_GENERATOR = 'G';
	public static final char T_TERMINATION = 'T';
	
	private static final int CAPACITY = 20;
	
	protected LabeledCSVParser parser;
	protected Clock wallClock = new Clock();


	protected PriorityQueue<BaseEvent> scheduler;
	protected Map<String, Distribution> map;
	protected LocalTime terminationTime;

	protected LocalTime lastInsertionTime;
	protected ObjectCreator creator;
	protected char delimiter = ',';


	// setup the load generator
	// reader contains the FileReader to read the fact/event csv file
	// creator is the object instantiator
	public AbstractLoadGenerator(FileReader reader, ObjectCreator creator) throws IOException
	{
		// set up event csv file as input
		parser = new LabeledCSVParser(new CSVParser(reader));
		parser.changeDelimiter(delimiter);
		
		// setup creator
		this.creator = creator;
		
		map = new HashMap<String, Distribution>();
		
		// create scheduler
		scheduler = new PriorityQueue<BaseEvent>(CAPACITY, new Comparator<BaseEvent>() {
			public int compare(BaseEvent e1, BaseEvent e2) {
				return e1.getTime().compareTo(e2.getTime());
			}
		});
		
	}
	

	// start the load generation
	public void run() throws IOException
	{

		boolean error;
		
		// loop to insert facts/events according to the scheduled time
		while (parser.getLine() != null) {
			
			error = false;
			BaseEvent event = null;

			try {
				Object object = null;
				Validators.checkNotNull(parser, TYPE);
				// insert facts/events into Drools
				switch (parser.getValueByLabel(TYPE).charAt(0)) {
					
					case T_EVENT:
						// schedule event
						Validators.checkNotNull(parser, TIME, CLASS_NAME);
						event = (BaseEvent) creator.newInstance(parser);
						event.setArrivalDistributionRef(parser.getValueByLabel(ARRIVAL_DISTR_REF));
						event.setServiceDistributionRef(parser.getValueByLabel(SERVICE_DISTR_REF));
						event.setTime(new LocalTime(parser.getValueByLabel(TIME)));
						event.setArrival(true);
						scheduler.add(event);
						logger.debug(wallClock.getTime() + ": Inserting event: " + parser.getValueByLabel(CLASS_NAME));

						break;
						
					case T_DISTR:
						// create distribution object instance and put in map
						Validators.checkNotNull(parser, CLASS_NAME, ARRIVAL_DISTR_REF);
						object = creator.newInstance(parser);
						map.put(parser.getValueByLabel(ARRIVAL_DISTR_REF), (Distribution) object);
						logger.debug(wallClock.getTime() + ": Creating a distribution object: " + parser.getValueByLabel(CLASS_NAME));
						break;
						
					case T_TERMINATION:
						// set load generation termination time
						Validators.checkNotNull(parser, TIME);
						terminationTime = new LocalTime(parser.getValueByLabel(TIME));
						logger.debug(wallClock.getTime() + ": Setting the termination time: " + parser.getValueByLabel(TIME));
						break;
						
					case T_GENERATOR:
						// create distribution object instance and put in map
						Validators.checkNotNull(parser, TIME, CLASS_NAME);
						object = creator.newInstance(parser);
						event = ((EventGenerator) object).generateEvent();
						event.setTime(new LocalTime(parser.getValueByLabel(TIME)));
						event.setArrivalDistributionRef(parser.getValueByLabel(ARRIVAL_DISTR_REF));
						event.setServiceDistributionRef(parser.getValueByLabel(SERVICE_DISTR_REF));
						event.setEventGenerator((EventGenerator) object);
						event.setArrival(true);
						scheduler.add(event);
						logger.debug(wallClock.getTime() + ": Creating a Generator object: " + parser.getValueByLabel(CLASS_NAME));
						break;
						
					default:
						break;
							
				}
			} catch (Exception e) {
				logger.error("Skipped line at " + (parser.lastLineNumber() + 1) + " due to exception: " + e.getMessage());
				error = true;
			}
			if (error) continue;
			
			processScheduledEvents();
			
		}
		

		parser.close();
		
		while ((terminationTime != null) && (scheduler.size() > 0) && (wallClock.getTime().compareTo(terminationTime) < 0)) {
			processScheduledEvents();
		}


	}
	

	public void changeDelimiter(char delimiter) {
		this.delimiter = delimiter;
		parser.changeDelimiter(delimiter);
	}
	
	
	public char getDelimiter() {
		return delimiter;
	}
	
	public Clock getWallClock() {
		return wallClock;
	}
	
	private void processScheduledEvents() {
		if (scheduler.size() == 0) return;
		BaseEvent ev = scheduler.peek();
		if ((wallClock.getTime() != null) && (wallClock.getTime().compareTo(ev.getTime()) > 0)) return;
		ev = scheduler.poll();
		
		lastInsertionTime = wallClock.getTime();
		wallClock.setTime(ev.getTime());
		
		long ms = 0;
		// not advancing the Drools clock for the first fact insertion
		if (lastInsertionTime != null) {
			LocalTime time = new LocalTime(lastInsertionTime);
			ms = time.plus(Period.fieldDifference(lastInsertionTime, wallClock.getTime())).toDateTimeToday().getMillis() -
					lastInsertionTime.toDateTimeToday().getMillis();
		}
		if (ms > 0) {
			logger.debug(wallClock.getTime() + ": Advancing clock by: " + ms + " milliseconds");

		}

		if (ev.isArrival()) {
			scheduleNextArrival(ev);
		}

		processEvent(ev, ms);
		
	}
	
	public void scheduleNextArrival(BaseEvent ev) {
		BaseEvent newEv = null;
		if (ev.getEventGenerator() == null) {
			try {
				newEv = (BaseEvent) ev.clone();
			} catch (CloneNotSupportedException e) {
				logger.error("Failed to clone ev - " + ev);
				return;
			}
		}
		else {
			newEv = ev.getEventGenerator().generateEvent();
			newEv.setTime(ev.getTime());
			newEv.setArrival(ev.isArrival());
			newEv.setArrivalDistributionRef(ev.getArrivalDistributionRef());
			newEv.setServiceDistributionRef(ev.getServiceDistributionRef());
			newEv.setEventGenerator(ev.getEventGenerator());
		}
		scheduleNext(newEv, ev.getArrivalDistributionRef());
	}
	
	public void scheduleNextServiceCompletion(BaseEvent ev) {
		scheduleNext(ev, ev.getServiceDistributionRef());
	}
	
	
	private void scheduleNext(BaseEvent ev, String ref) {

		// schedule next event

		Distribution distr = null;
		if (ref != null) {

			if (ev != null) {
				distr = map.get(ref);
				if (distr != null ) {
					int ms = (int) distr.sample();
					LocalTime time = ev.getTime();
					ev.setTime(time.plusMillis((int) ms));
					scheduler.add(ev);
					logger.debug("\tScheduling event - " + ev.getClass().getName() + ": " + ev + ", " + ms + " millisec");
					logger.debug("\tscheduled at - " + time);

				}
			} else {
					logger.error("Failed to clone event - " + ev);
			}
		}
		else {
			logger.error("Error: Distribution Reference: " + ref + " not defined - " + ev + " (ref: " + ref + ")" );
			logger.error("Error event - " + ev);
		}
	}
	
	abstract public void processEvent(BaseEvent event, long millisSinceLastEvent);


}
