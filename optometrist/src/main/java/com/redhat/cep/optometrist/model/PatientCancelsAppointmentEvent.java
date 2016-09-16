package com.redhat.cep.optometrist.model;

public class PatientCancelsAppointmentEvent extends AbstractExternalEvent {

	
	public PatientCancelsAppointmentEvent(String patientName, String optometristName, int slotNumber)
	{
		super(patientName, optometristName, slotNumber);
	}

}
