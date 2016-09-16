package com.redhat.cep.util;

import org.apache.commons.math3.random.JDKRandomGenerator;

public class UniformDistribution extends org.apache.commons.math3.distribution.UniformRealDistribution implements Distribution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public UniformDistribution(int seed, double lower, double upper) {
		super(new JDKRandomGenerator(), lower, upper);
		reseedRandomGenerator(seed);
	}
	

	public static void main(String[] args) {
		UniformDistribution distr = new UniformDistribution(25677, 10.0, 20.0);
		for (int i = 0; i < 25; i++) {
			System.out.println(distr.sample());
		}

	}

}
