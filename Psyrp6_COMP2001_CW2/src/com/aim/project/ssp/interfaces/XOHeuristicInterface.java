package com.aim.project.ssp.interfaces;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public interface XOHeuristicInterface extends HeuristicInterface {

	/**
	 * 
	 * @param oParent1            Parent solution 1
	 * @param oParent2            Parent solution 2
	 * @param dDepthOfSearch       current DOS setting
	 * @param dIntensityOfMutation currentIOM setting
	 */
	public double apply(SSPSolutionInterface oParent1, SSPSolutionInterface oParent2, SSPSolutionInterface oChild,
						double dDepthOfSearch, double dIntensityOfMutation);

}
