package com.aim.project.ssp.instance;


import java.util.ArrayList;
import java.util.Random;

import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.solution.SSPSolution;
import com.aim.project.ssp.solution.SolutionRepresentation;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public class SSPInstance implements SSPInstanceInterface {
	
	private final Location[] aoLocations;
	
	private final Location oHotelLocation;
	
	private final Location oAirportLocation;
	
	private final int iNumberOfLocations;
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface f = null;



	public SSPInstance(int iNumberOfLocations, Location[] aoLocations, Location oHotelLocation, Location oAirportLocation, Random random) {
		
		this.iNumberOfLocations = iNumberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oHotelLocation = oHotelLocation;
		this.oAirportLocation = oAirportLocation;


	}

	@Override
	public SSPSolution createSolution(InitialisationMode mode) {
		int[] tour = new int[iNumberOfLocations];
		for (int i = 0; i < iNumberOfLocations; i++) {
			tour[i] = i;
		}

		if (mode == InitialisationMode.RANDOM) {
			// Shuffle POI indices randomly
			for (int i = tour.length - 1; i > 0; i--) {
				int j = oRandom.nextInt(i + 1);
				int temp = tour[i];
				tour[i] = tour[j];
				tour[j] = temp;
			}
		} else if (mode == InitialisationMode.CONSTRUCTIVE) {

			tour = nearestNeighbourTour();
		}

		SolutionRepresentation rep = new SolutionRepresentation(tour);
		double objective = evaluateTour(rep);
		return new SSPSolution(rep, (int)Math.round(objective));
	}


	private int[] nearestNeighbourTour() {
		boolean[] visited = new boolean[iNumberOfLocations];
		int[] tour = new int[iNumberOfLocations];

		// Start from the POI closest to the hotel
		int currentIndex = findNearest(oHotelLocation, aoLocations, visited);
		tour[0] = currentIndex;
		visited[currentIndex] = true;

		for (int i = 1; i < iNumberOfLocations; i++) {
			currentIndex = findNearest(aoLocations[currentIndex], aoLocations, visited);
			tour[i] = currentIndex;
			visited[currentIndex] = true;
		}

		return tour;
	}

	private int findNearest(Location from, Location[] candidates, boolean[] visited) {
		double minDist = Double.MAX_VALUE;
		int nearest = -1;
		for (int i = 0; i < candidates.length; i++) {
			if (!visited[i]) {
				double dist = from.getCeilingEuclideanDistance(candidates[i]);
				if (dist < minDist) {
					minDist = dist;
					nearest = i;
				}
			}
		}
		return nearest;
	}

	private double evaluateTour(SolutionRepresentation rep) {
		double total = 0.0;
		Location current = oHotelLocation;

		for (int i : rep.getSolutionRepresentation()) {
			Location next = aoLocations[i];
			total += current.getCeilingEuclideanDistance(next);
			current = next;
		}

		total += current.getCeilingEuclideanDistance(oAirportLocation);
		return total;
	}



	@Override
	public ObjectiveFunctionInterface getSSPObjectiveFunction() {
		return f;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		this.f = f;
	}


	@Override
	public int getNumberOfLocations() {
		return iNumberOfLocations;
	}

	@Override
	public Location getSightseeingLocation(int iLocationId) {
		if (iLocationId >= 0 && iLocationId < aoLocations.length) {
			return aoLocations[iLocationId];
		}
		return null;
	}

	@Override
	public Location getHotelLocation() {
		return oHotelLocation;
	}


	@Override
	public Location getAirportLocation() {
		return oAirportLocation;
	}

	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(SSPSolutionInterface oSolution) {
		ArrayList<Location> list = new ArrayList<>();
		list.add(oHotelLocation);

		int[] tour = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		for (int i : tour) {
			list.add(aoLocations[i]);
		}

		list.add(oAirportLocation);
		return list;
	}
}
