package com.redhat.cep.optometrist.model;

import org.joda.time.LocalTime;

public class ValidSmsPeriodEvent {

	private final LocalTime currentTime;
	
	public LocalTime getCurrentTime() {
		return currentTime;
	}

	public ValidSmsPeriodEvent(LocalTime currentTime) {
		this.currentTime = currentTime;
	}
}
