package com.redhat.cep.optometrist.model;

public class Patient {

	private String name;
	private String mobileNumber;
	private String pushoverKey;
	
	public String getPushoverKey() {
		return pushoverKey;
	}

	public void setPushoverKey(String pushoverKey) {
		this.pushoverKey = pushoverKey;
	}

	public Patient(String name, String mobileNumber, String pushoverKey)
	{
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.pushoverKey = pushoverKey;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
