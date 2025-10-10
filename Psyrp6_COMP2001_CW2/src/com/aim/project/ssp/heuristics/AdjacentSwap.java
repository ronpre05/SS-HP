package com.aim.project.ssp.heuristics;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;

import java.util.Random;


/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private Random random;
	private ObjectiveFunctionInterface f;

	public AdjacentSwap(Random random) {

		super(random);
		this.random = random;

	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		this.f = f;
	}

	@Override
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		//System.out.println("Applying AdjacentSwap heuristic.");
		int[] tour = solution.getSolutionRepresentation().getSolutionRepresentation();
		int length = tour.length;

		// Map intensityOfMutation to number of swaps
		int numSwaps;
		if (intensityOfMutation < 0.2)
			numSwaps = 1;
		else if (intensityOfMutation < 0.4)
			numSwaps = 2;
		else if (intensityOfMutation < 0.6)
			numSwaps = 4;
		else if (intensityOfMutation < 0.8)
			numSwaps = 8;
		else if (intensityOfMutation < 1.0)
			numSwaps = 16;
		else
			numSwaps = 32;

		// Don't swap hotel (index 0) or airport (index length - 1)
		for (int i = 0; i < numSwaps; i++) {
			int index = random.nextInt(length - 1); // ensures index ∈ [0, length - 2]

			// Swap index and index+1
			int temp = tour[index];
			tour[index] = tour[index + 1];
			tour[index + 1] = temp;
		}

		double newObj = f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
		solution.setObjectiveFunctionValue((int)Math.round(newObj));
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
