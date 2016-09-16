package com.redhat.cep.util;


import java.io.Serializable;

import org.joda.time.LocalTime;

// This is to be used as a base class for all CEP application events
// AbstractLoadGenerator initialises and uses these fields
// user applications normally do not set/modify these fields
public class BaseEvent implements Cloneable, Serializable {

	/**
	 * All base class fields do not require serialization for using message queues
	 */
	private static final long serialVersionUID = -4330560182544608374L;
	transient private LocalTime time;
	transient private String arrivalDistributionRef;
	transient private String serviceDistributionRef;
	transient private EventGenerator eventGenerator;
	transient private boolean arrival;
	
	

	public BaseEvent(LocalTime time, String arrivalDistributionRef, String serviceDistributionRef) {
		this.time = time;
		this.arrivalDistributionRef = arrivalDistributionRef;
		this.serviceDistributionRef = serviceDistributionRef;
		arrival = false;
		eventGenerator = null;

	}
	
	public BaseEvent() {
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public LocalTime getTime() {
		return time;
	}
	
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public String getArrivalDistributionRef() {
		return arrivalDistributionRef;
	}
	
	public void setArrivalDistributionRef(String arrivalDistributionRef) {
		this.arrivalDistributionRef = arrivalDistributionRef;
	}
	
	public String getServiceDistributionRef() {
		return serviceDistributionRef;
	}
	
	public void setServiceDistributionRef(String serviceDistributionRef) {
		this.serviceDistributionRef = serviceDistributionRef;
	}
	
	public String toString() {
		return time.toString() + " : " + arrivalDistributionRef + " : " + serviceDistributionRef + " : " + arrival;
	}
	
	public EventGenerator getEventGenerator() {
		return eventGenerator;
	}

	public void setEventGenerator(EventGenerator eventGenerator) {
		this.eventGenerator = eventGenerator;
	}

	public boolean isArrival() {
		return arrival;
	}

	public void setArrival(boolean arrival) {
		this.arrival = arrival;
	}

	
}
