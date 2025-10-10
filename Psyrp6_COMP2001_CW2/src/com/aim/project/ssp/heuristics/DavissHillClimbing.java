package com.aim.project.ssp.heuristics;

import java.util.Random;
import java.util.stream.IntStream;

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
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {

	private final Random random;
	private ObjectiveFunctionInterface f;

	public DavissHillClimbing(Random random) {
	
		super(random);
		this.random = random;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		this.f = f;
	}

	@Override
	public double apply(SSPSolutionInterface solution, double dos, double iom) {

		// Determine number of internal iterations based on dos
		int iterations;
		if (dos < 0.2)
			iterations = 1;
		else if (dos < 0.4)
			iterations = 2;
		else if (dos < 0.6)
			iterations = 3;
		else if (dos < 0.8)
			iterations = 4;
		else
			iterations = 5;

		int[] tour = solution.getSolutionRepresentation().getSolutionRepresentation();
		int n = tour.length;

		int currentValue = f.getObjectiveFunctionValue(solution.getSolutionRepresentation());

		for (int iter = 0; iter < iterations; iter++) {

			// Pick a random index for adjacent swap
			int i = random.nextInt(n - 1); // index ∈ [0, n - 2]

			// Swap i and i + 1
			swap(tour, i, i + 1);

			int newValue = f.getObjectiveFunctionValue(solution.getSolutionRepresentation());

			// Accept the new solution if it's better
			if (newValue < currentValue) {
				currentValue = newValue;
			} else {
				// Revert swap
				swap(tour, i, i + 1);
			}
		}

		solution.setObjectiveFunctionValue(currentValue);
		return currentValue;
	}

	private void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
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

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return true;
	}
}
