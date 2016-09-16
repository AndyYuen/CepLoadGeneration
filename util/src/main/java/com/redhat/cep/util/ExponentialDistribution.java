package com.redhat.cep.util;

import org.apache.commons.math3.random.JDKRandomGenerator;


public class ExponentialDistribution extends org.apache.commons.math3.distribution.ExponentialDistribution implements Distribution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ExponentialDistribution(int seed, double mean) {
		super(new JDKRandomGenerator(), mean);
		reseedRandomGenerator(seed);
	}
	
	public static void main(String[] args) {
		ExponentialDistribution distr = new ExponentialDistribution(11111, 10.0);
		for (int i = 0; i < 25; i++) {
			System.out.println(distr.sample());
		}
		System.out.println("Mean: " + distr.getNumericalMean());
	}

	
}
