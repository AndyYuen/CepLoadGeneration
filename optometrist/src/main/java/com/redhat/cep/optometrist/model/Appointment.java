package com.redhat.cep.optometrist.model;

import org.joda.time.LocalTime;

public class Appointment {

	private int slotNumber;
	private LocalTime scheduledStartTime;
	private String patientName;
	private String optometristName;
	private LocalTime arrivalTime;
	private LocalTime startServiceTime;
	private LocalTime endServiceTime;

	public Appointment(String patientName, String optometristName, int slotNumber, LocalTime scheduledStartTime)
	{
		this.optometristName = optometristName;
		this.patientName = patientName;	
		this.slotNumber = slotNumber;
		this.scheduledStartTime = scheduledStartTime;
	}
	
	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public LocalTime getStartServiceTime() {
		return startServiceTime;
	}

	public void setStartServiceTime(LocalTime startServiceTime) {
		this.startServiceTime = startServiceTime;
	}

	public LocalTime getEndServiceTime() {
		return endServiceTime;
	}

	public void setEndServiceTime(LocalTime endServiceTime) {
		this.endServiceTime = endServiceTime;
	}


	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public LocalTime getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(LocalTime scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}

	public String getOptometristName() {
		return optometristName;
	}

	public void setOptometristName(String optometristName) {
		this.optometristName = optometristName;
	}
	
	public int compareScheduledStartTimeTo(LocalTime time) {
		return scheduledStartTime.compareTo(time);
	}
}

