package com.aim.project.ssp;

import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 */
public class SSPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final SSPInstanceInterface oInstance;
	
	public SSPObjectiveFunction(SSPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		int[] tour = oSolution.getSolutionRepresentation();
		double totalCost = 0.0;

		// Start from hotel to first POI
		Location from = oInstance.getHotelLocation();
		for (int i = 0; i < tour.length; i++) {
			Location to = oInstance.getSightseeingLocation(tour[i]);
			totalCost += from.getCeilingEuclideanDistance(to);
			from = to;
		}

		// End from last POI to airport
		totalCost += from.getCeilingEuclideanDistance(oInstance.getAirportLocation());

		return (int) Math.round(totalCost);
	}

	@Override
	public int getCost(int iLocationA, int iLocationB) {
		Location locA = oInstance.getSightseeingLocation(iLocationA);
		Location locB = oInstance.getSightseeingLocation(iLocationB);
		return (locA.getCeilingEuclideanDistance(locB));
	}

	@Override
	public int getCostBetweenHotelAnd(int iLocation) {
		Location hotel = oInstance.getHotelLocation();
		Location poi = oInstance.getSightseeingLocation(iLocation);
		return (hotel.getCeilingEuclideanDistance(poi));
	}


	@Override
	public int getCostBetweenAirportAnd(int iLocation) {
		Location airport = oInstance.getAirportLocation();
		Location poi = oInstance.getSightseeingLocation(iLocation);
		return (airport.getCeilingEuclideanDistance(poi));
	}
}
