package com.redhat.cep.optometrist.model;

import org.joda.time.LocalTime;

public class CheckDelayEvent extends AbstractInternalEvent {

	public CheckDelayEvent(String optometristName, LocalTime currentTime)
	{
		super(optometristName, currentTime);
	}

}
