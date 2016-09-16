package com.redhat.cep.optometrist.model;

public class PatientServiceCompletedEvent extends AbstractExternalEvent  {
	
	public PatientServiceCompletedEvent(String patientName, String optometristName, int slotNumber)
	{
		super(patientName, optometristName, slotNumber);
	}

}
