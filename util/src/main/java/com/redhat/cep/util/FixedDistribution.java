package com.redhat.cep.util;

public class FixedDistribution implements Distribution {

	private double fixedValue;
	
	public FixedDistribution(double fixedValue) {
		this.fixedValue = fixedValue;
	}
	public double sample() {

		return fixedValue;
	}
	
	public double getFixedValue() {
		return fixedValue;
	}

}
