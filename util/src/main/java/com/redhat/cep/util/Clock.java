package com.redhat.cep.util;

import org.joda.time.LocalTime;

public class Clock {
	private LocalTime time;

	public Clock()
	{
		
	}
	
	public Clock(LocalTime time)
	{
		setTime(time);
	}
	
	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}


}
