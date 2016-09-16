package com.redhat.cep.optometrist.model;

public class PatientArrivalEvent extends AbstractExternalEvent {

	public PatientArrivalEvent(String patientName, String optometristName, int slotNumber)
	{
		super(patientName, optometristName, slotNumber);
	}

}
