package com.redhat.cep.optometrist.model;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

public class Optometrist {

	private final String name;
	private final String clinic;
	private final LocalTime startTime;
	private final int appointmentLength;
	private int slotNumberInService = -1;
	private int lastSlotNumberInService = -1;
	private int delay;

	public Optometrist(String name, String clinic, LocalTime startTime, int appointmentLength) {
		this.name = name;
		this.clinic = clinic;
		this.startTime = startTime;
		this.appointmentLength = appointmentLength;
	}
	
	public String getName() {
		return name;
	}

	public String getClinic() {
		return clinic;
	}

	public LocalTime getStartTime() {
		return startTime;
	}
	
	public int getAppointmentLength() {
		return appointmentLength;
	}


	public int getSlotNumberInService() {
		return slotNumberInService;
	}

	public void setSlotNumberInService(int slotNumberInService) {
		this.slotNumberInService = slotNumberInService;
	}


	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public int compareSlotTimeTo(int slot, LocalTime time)
	{
		return startTime.plusMinutes(slot * appointmentLength).compareTo(time);
	}

	public int minutesAfterScheduledStartTime(int slot, LocalTime time)
	{
		return Minutes.minutesBetween(startTime.plusMinutes(slot * appointmentLength), time).getMinutes();
		
	}
	
	public LocalTime getStartTimeForSlot(int slot)
	{
		return startTime.plusMinutes(slot * appointmentLength);
	}
	
	public int getLastSlotNumberInService() {
		return lastSlotNumberInService;
	}

	public void setLastSlotNumberInService(int lastSlotNumberInService) {
		this.lastSlotNumberInService = lastSlotNumberInService;
	}

}
