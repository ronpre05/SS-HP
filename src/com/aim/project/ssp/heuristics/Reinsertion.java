package com.aim.project.ssp.heuristics;

import java.util.Random;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;

/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	private final Random random;
	private ObjectiveFunctionInterface f;

	public Reinsertion(Random random) {

		super(random);
		this.random = random;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		this.f = f;
	}


	@Override
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		int[] tour = solution.getSolutionRepresentation().getSolutionRepresentation();
		int length = tour.length;

		int reinsertions;
		if (intensityOfMutation < 0.2)
			reinsertions = 1;
		else if (intensityOfMutation < 0.4)
			reinsertions = 2;
		else if (intensityOfMutation < 0.6)
			reinsertions = 3;
		else if (intensityOfMutation < 0.8)
			reinsertions = 4;
		else
			reinsertions = 5;

		for (int i = 0; i < reinsertions; i++) {
			int removeIndex = random.nextInt(length); // any sightseeing location
			int value = tour[removeIndex];

			// Shift left
			System.arraycopy(tour, removeIndex + 1, tour, removeIndex, length - removeIndex - 1);

			// Select new position
			int insertIndex = random.nextInt(length - 1); // new spot, avoid putting after the end
			if (insertIndex >= removeIndex) {
				// Adjust for removed slot
				insertIndex = Math.min(insertIndex, length - 2);
			}

			// Shift right
			System.arraycopy(tour, insertIndex, tour, insertIndex + 1, length - insertIndex - 1);
			tour[insertIndex] = value;
		}

		double newObj = f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
		solution.setObjectiveFunctionValue((int) Math.round(newObj));
		return newObj;
	}

	/*
	 * TODO update the methods below to return the correct boolean value.
	 */

	@Override
	public boolean isCrossover() {

		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}


	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}

}
