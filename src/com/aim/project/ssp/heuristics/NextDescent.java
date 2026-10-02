package com.aim.project.ssp.heuristics;


import java.util.Random;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;


/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {

	private final Random random;
	private ObjectiveFunctionInterface f;

	public NextDescent(Random random) {
	
		super(random);
		this.random = random;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		this.f = f;
	}


	@Override
	public double apply(SSPSolutionInterface solution, double dos, double iom) {

		int[] tour = solution.getSolutionRepresentation().getSolutionRepresentation();
		int length = tour.length;
		int currentValue = solution.getObjectiveFunctionValue();

		// Determine number of iterations based on depthOfSearch
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

		for (int iter = 0; iter < iterations; iter++) {

			boolean improvementFound = false;

			for (int i = 0; i < length - 1; i++) {

				// Clone current solution
				int[] newTour = tour.clone();

				// Swap adjacent elements
				int temp = newTour[i];
				newTour[i] = newTour[i + 1];
				newTour[i + 1] = temp;

				// Wrap in representation and evaluate
				SolutionRepresentationInterface rep = solution.getSolutionRepresentation().clone();
				rep.setSolutionRepresentation(newTour);

				int newValue = f.getObjectiveFunctionValue(rep);

				if (newValue < currentValue) {
					// Accept new tour
					tour[i] = newTour[i];
					tour[i + 1] = newTour[i + 1];
					solution.setObjectiveFunctionValue(newValue);
					improvementFound = true;
					currentValue = newValue;
					break; // first improving move
				}
			}

			if (!improvementFound)
				break; // stop if no improvement found in this iteration
		}

		return solution.getObjectiveFunctionValue();
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
