package com.redhat.cep.optometrist.model;

public class PatientServiceStartedEvent extends AbstractExternalEvent {
	
	public PatientServiceStartedEvent(String patientName, String optometristName, int slotNumber)
	{
		super(patientName, optometristName, slotNumber);
	}

}
