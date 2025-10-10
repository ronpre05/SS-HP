package com.aim.project.ssp.interfaces;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public interface ObjectiveFunctionInterface {

	/**
	 * 
	 * @param solutionRepresentation The representation of the current solution
	 * @return The objective function of the current solution represented by <code>solutionRepresentation</code>
	 */
	public int getObjectiveFunctionValue(SolutionRepresentationInterface solutionRepresentation);
	
	/**
	 * 
	 * @param iLocationA ID of the starting sightseeing location.
	 * @param iLocationB ID of the destination sightseeing location.
	 * @return The distance between locations <code>iLocationA</code> and <code>iLocationB</code>.
	 */
	public int getCost(int iLocationA, int iLocationB);
	
	/**
	 * 
	 * @param iLocation ID of the sightseeing location.
	 * @return The cost of going from the HOTEL to the location with ID iLocation.
	 */
	public int getCostBetweenHotelAnd(int iLocation);

	/**
	 * 
	 * @param iLocation ID of the sightseeing location.
	 * @return The cost of going from the location with ID iLocation to the AIRPORT.
	 */
	public int getCostBetweenAirportAnd(int iLocation);
}
