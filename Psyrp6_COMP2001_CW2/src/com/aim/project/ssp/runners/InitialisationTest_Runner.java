package com.aim.project.ssp.runners;

import com.aim.project.ssp.hyperheuristics.InitialisationTest_HH;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 * Runs a hyper-heuristic which just initialises a solution and displays that initial solution.
 */
public class InitialisationTest_Runner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		return new InitialisationTest_HH(seed);
	}

	public static void main(String[] args) {

		HH_Runner_Visual runner = new InitialisationTest_Runner();
		runner.run();
	}

}
