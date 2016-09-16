package com.redhat.cep.optometrist.model;

public class PatientMakesAppointmentEvent extends AbstractExternalEvent {

	protected final String mobileNumber;
	protected final String pushoverKey;

	
	
	public String getPushoverKey() {
		return pushoverKey;
	}

	public PatientMakesAppointmentEvent(String patientName, String optometristName, int slotNumber, String mobileNumber, String pushoverKey)
	{
		super(patientName, optometristName, slotNumber);
		this.mobileNumber = mobileNumber;
		this.pushoverKey = pushoverKey;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}

}
