package com.aim.project.ssp.runners;


import java.awt.Color;

import com.aim.project.ssp.SightseeingProblemDomain;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.solution.SSPSolution;
import com.aim.project.ssp.visualiser.SSPView;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 * 
 * Runs a hyper-heuristic using a default configuration then displays the best solution found.
 */
public abstract class HH_Runner_Visual {

	public HH_Runner_Visual() {
		
	}
	
	public void run() {
		
		long seed = 17032025L;
		long timeLimit = 10000L;
		SightseeingProblemDomain problem = new SightseeingProblemDomain(seed);
		problem.loadInstance(6);
		HyperHeuristic hh = getHyperHeuristic(seed);
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(problem);
		hh.run();

		System.out.println("Initial: ");
		problem.printInitialSolution();

		System.out.println("f(s_best) = " + hh.getBestSolutionValue());
		problem.printBestSolutionFound();

		new SSPView(problem.getLoadedInstance(), problem, Color.RED, Color.GREEN);
	}
	
	/** 
	 * Transforms the best solution found, represented as an SSPSolution, into an ordering of Location's
	 * which the visualiser tool uses to draw the tour.
	 */
	protected Location[] transformSolution(SSPSolution solution, SightseeingProblemDomain problem) {
		
		return problem.getRouteOrderedByLocations();
	}
	
	/**
	 * Allows a general visualiser runner by making the HyperHeuristic abstract.
	 * You can sub-class this class to run any hyper-heuristic that you want.
	 */
	protected abstract HyperHeuristic getHyperHeuristic(long seed);
}
