package com.aim.project.ssp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public class SR_IE_HH extends HyperHeuristic {
	
	public SR_IE_HH(long lSeed) {
		
		super(lSeed);
	}

	@Override
	protected void solve(ProblemDomain oProblem) {

		int currentIndex = 0, candidateIndex = 1;

		oProblem.initialiseSolution(currentIndex);
		double current = oProblem.getFunctionValue(currentIndex);

		while(!hasTimeExpired() ) {

			// apply a random heuristic
			int h = rng.nextInt(oProblem.getNumberOfHeuristics());
			double candidate = oProblem.applyHeuristic(h, currentIndex, candidateIndex);

			// IE acceptance
			if(candidate <= current) {

				// switch indices (faster by avoiding an unnessecary clone)
				oProblem.copySolution(candidateIndex, currentIndex);

				// update cost of current solution
				current = candidate;
			}
		}
	}

	@Override
	public String toString() {

		return "SR_IE_HH";
	}
}
