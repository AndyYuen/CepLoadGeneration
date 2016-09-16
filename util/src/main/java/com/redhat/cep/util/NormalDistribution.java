package com.redhat.cep.util;

import org.apache.commons.math3.random.JDKRandomGenerator;

public class NormalDistribution extends org.apache.commons.math3.distribution.NormalDistribution implements Distribution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public NormalDistribution(int seed, double mean, double sd) {
		super(new JDKRandomGenerator(), mean, sd);
		reseedRandomGenerator(seed);
	}
	

	public static void main(String[] args) {
		NormalDistribution distr = new NormalDistribution(25677, 10.0, 5.0);
		for (int i = 0; i < 25; i++) {
			System.out.println(distr.sample());
		}
		System.out.println("Mean: " + distr.getNumericalMean() + ", sd: " + distr.getStandardDeviation());
	}

}
