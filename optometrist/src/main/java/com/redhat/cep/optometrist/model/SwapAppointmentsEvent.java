package com.redhat.cep.optometrist.model;

public class SwapAppointmentsEvent extends AbstractExternalEvent {

	protected final String otherPatientName;
	protected final int otherSlotNumber;
	
	public SwapAppointmentsEvent(String patientName, String optometristName, int slotNumber,
			String otherPatientName, int otherSlotNumber)
	{
		super(patientName, optometristName, slotNumber);
		this.otherPatientName = otherPatientName;
		this.otherSlotNumber = otherSlotNumber;
	}


	public String getOtherPatientName() {
		return otherPatientName;
	}

	public int getOtherSlotNumber() {
		return otherSlotNumber;
	}

}
