package com.redhat.cep.optometrist.model;

import org.joda.time.LocalTime;

import com.redhat.cep.util.BaseEvent;

public abstract class AbstractInternalEvent extends BaseEvent {
	protected final String optometristName;
	protected final LocalTime currentTime;;

	public AbstractInternalEvent(String optometristName, LocalTime currentTime)
	{
		super();
		this.optometristName = optometristName;
		this.currentTime = currentTime;
	}

	public String getOptometristName() {
		return optometristName;
	}
	
	public LocalTime getCurrentTime() {
		return currentTime;
	}

}
