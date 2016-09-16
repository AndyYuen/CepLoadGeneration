package com.redhat.cep.util;


// This is a wrapper class for use in a subclass of AbstractLoadGenerator to pass to
// the rules engine for scheduling events as globals
// It only exposes 2 methods
public class Scheduler {

	private AbstractLoadGenerator loadGenerator;
	
	public Scheduler(AbstractLoadGenerator loadGenerator) {
		this.loadGenerator = loadGenerator;
	}
	
	public void scheduleNextServiceCompletion(BaseEvent ev)  {
		loadGenerator.scheduleNextServiceCompletion(ev);
	}
	
	public void scheduleNextArrival(BaseEvent ev) {
		loadGenerator.scheduleNextArrival(ev);
	}

}
