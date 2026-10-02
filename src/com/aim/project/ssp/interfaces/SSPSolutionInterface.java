package com.aim.project.ssp.interfaces;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public interface SSPSolutionInterface extends Cloneable {
	
	/**
	 * 
	 * @return The objective value of the solution.
	 */
	public int getObjectiveFunctionValue();
	
	/**
	 * 
	 * Updates the objective value of the solution.
	 * @param objectiveFunctionValue The new objective function value.
	 */
	public void setObjectiveFunctionValue(int objectiveFunctionValue);
	
	/**
	 * 
	 * @return The solution's representation.
	 */
	public SolutionRepresentationInterface getSolutionRepresentation();
	
	/**
	 * 
	 * @return The total number of locations in this solution (includes the HOTEL and AIRPORT).
	 */
	public int getNumberOfLocations();

	/**
	 * 
	 * @return A deep clone of the solution.
	 */
	public SSPSolutionInterface clone();

}
