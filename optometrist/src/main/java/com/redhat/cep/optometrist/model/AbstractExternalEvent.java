package com.redhat.cep.optometrist.model;

import com.redhat.cep.util.BaseEvent;

public abstract class AbstractExternalEvent extends BaseEvent {
	protected final String patientName;
	protected final String optometristName;
	protected final int slotNumber;
	
	public AbstractExternalEvent(String patientName, String optometristName, int slotNumber) 
	{
		super();
		this.patientName = patientName;
		this.optometristName = optometristName;
		this.slotNumber = slotNumber;
	}
	
	public String getPatientName() {
		return patientName;
	}

	public String getOptometristName() {
		return optometristName;
	}

	public int getSlotNumber() {
		return slotNumber;
	}


}
