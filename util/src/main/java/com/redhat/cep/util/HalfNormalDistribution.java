package com.redhat.cep.util;

import org.apache.commons.math3.random.JDKRandomGenerator;

public class HalfNormalDistribution extends org.apache.commons.math3.distribution.NormalDistribution implements Distribution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public HalfNormalDistribution(int seed, double mean, double sd) {
		super(new JDKRandomGenerator(), mean, sd);
		reseedRandomGenerator(seed);
	}
	
	// make sure there is no negative value - singled-sided bell curve
	public double sample() {
		return Math.abs(super.sample());
	}
	
	public static void main(String[] args) {
		HalfNormalDistribution distr = new HalfNormalDistribution(25677, 10.0, 5.0);
		for (int i = 0; i < 25; i++) {
			System.out.println(distr.sample());
		}
		System.out.println("Mean: " + distr.getNumericalMean() + ", sd: " + distr.getStandardDeviation());
	}

}
