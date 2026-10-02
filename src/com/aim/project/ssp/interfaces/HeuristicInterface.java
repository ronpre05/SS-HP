package com.aim.project.ssp.interfaces;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public interface HeuristicInterface {

	/**
	 * Applies this heuristic to the solution <code>oSolution</code>
	 * and updates the objective value of the solution.
	 * @param oSolution The solution to apply the heuristic to.
	 * @param dDepthOfSearch The current depth of search setting.
	 * @param dIntensityOfMutation The current intensity of mutation setting.
	 */
	public double apply(SSPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation);
	
	public boolean isCrossover();
	
	public boolean usesIntensityOfMutation();
	
	public boolean usesDepthOfSearch();
	
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction);
}
