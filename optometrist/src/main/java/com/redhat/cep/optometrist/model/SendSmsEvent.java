package com.redhat.cep.optometrist.model;

import org.joda.time.LocalTime;

public class SendSmsEvent extends AbstractInternalEvent {

	protected final int delay;
	protected final int slotNumberInService;

	public SendSmsEvent(String optometristName, int slotNumberInService, int delay, LocalTime currentTime)
	{
		super(optometristName, currentTime);
		this.delay = delay;
		this.slotNumberInService = slotNumberInService;
	}

	public int getDelay() {
		return delay;
	}

	public int getSlotNumberInService() {
		return slotNumberInService;
	}
}
