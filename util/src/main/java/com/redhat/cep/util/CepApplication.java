package com.redhat.cep.util;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.drools.core.time.SessionPseudoClock;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.kie.api.KieServices;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.runtime.rule.FactHandle;



public class CepApplication implements Observer {
	static Logger logger = Logger.getLogger(CepApplication.class);

	static final String SEPARATOR_LINE = "******************************";
	
	protected KieSession ksession;
	protected SessionPseudoClock clock;
	protected EntryPoint entry;
	protected Clock wallClock;
	protected LocalTime lastInsertionTime;
	protected boolean useSystemTime;


	// setup the CEP application
	// ksessionName is the ksession name in the kmodule.xml in the META-INF directory
	// entryPointName is the event entry point name
	public CepApplication(String ksessionName, String entryPointName) 
	{
		
		logger.info(SEPARATOR_LINE + " Initialising CEP Application");

		
		// set up drools env
		ksession = KieServices.Factory.get().getKieClasspathContainer().newKieSession(ksessionName);
    	clock = ksession.getSessionClock();
	    entry = ksession.getEntryPoint(entryPointName);
	    
	    // set agenda listener
    	ksession.addEventListener( new DefaultAgendaEventListener() {
 		   public void afterMatchFired(AfterMatchFiredEvent event) {
 		       super.afterMatchFired( event );
 		   }
    	});
    	
    	wallClock = new Clock();
    	setEventTimeMode();

	}
	
	public void setGlobal(String name, Object object)
	{
		ksession.setGlobal(name, object);
	}

	
	// perform cleanup 
	public void shutdown() {
		ksession.dispose();
	}

	public void update(Observable observable, Object obj) {
		BaseEvent event = ((ObservableEvent) observable).getValue();
		long ms = 0;
		
		lastInsertionTime = wallClock.getTime();
		if (useSystemTime) {
			wallClock.setTime(LocalTime.now());
		}
		else {
			wallClock.setTime(event.getTime());
		}
		if (lastInsertionTime != null) {
			LocalTime time = new LocalTime(lastInsertionTime);
			ms = time.plus(Period.fieldDifference(lastInsertionTime, wallClock.getTime())).toDateTimeToday().getMillis() -
					lastInsertionTime.toDateTimeToday().getMillis();
		}
		clock.advanceTime(ms, TimeUnit.MILLISECONDS);

		entry.insert(event);
		
		ksession.fireAllRules();
	}

	public FactHandle insert(Object fact) {
		return ksession.insert(fact);
	}

	
	public Clock getWallClock() {
		return wallClock;
	}

	public void setSystemTimeMode() {
		useSystemTime = true;
	}

	public void setEventTimeMode() {
		useSystemTime = false;
	}
}
