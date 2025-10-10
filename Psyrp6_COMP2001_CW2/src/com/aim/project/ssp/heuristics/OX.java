package com.aim.project.ssp.heuristics;

import java.util.Random;

import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.XOHeuristicInterface;

/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class OX implements XOHeuristicInterface {
	
	private final Random random;
	
	private ObjectiveFunctionInterface f;

	public OX(Random random) {
		
		this.random = random;
	}

	@Override
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		throw new UnsupportedOperationException("OX requires two parents and a child.");
	}

	@Override
	public double apply(SSPSolutionInterface p1, SSPSolutionInterface p2,
						SSPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

		// Early return if parents are the same object or identical
		if (p1 == p2 || areIdentical(p1, p2)) {
			System.out.println("OX skipped: Parents are identical or too similar.");
			return p1.getObjectiveFunctionValue();
		}

		// Log parent fitnesses
		double fitness1 = p1.getObjectiveFunctionValue();
		double fitness2 = p2.getObjectiveFunctionValue();

		System.out.println("OX Parents:");
		System.out.println(" - f(p1) = " + fitness1);
		System.out.println(" - f(p2) = " + fitness2);

		// OX Crossover
		int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation();
		int length = parent1.length;

		int cut1 = random.nextInt(length - 2);
		int cut2 = cut1 + 1 + random.nextInt(length - cut1 - 1);

		int[] child = new int[length];
		for (int i = 0; i < length; i++) child[i] = -1;

		// Copy segment from p1
		for (int i = cut1; i <= cut2; i++) {
			child[i] = parent1[i];
		}

		// Fill remaining positions from p2
		int currentPos = (cut2 + 1) % length;
		for (int i = 0; i < length; i++) {
			int gene = parent2[(cut2 + 1 + i) % length];
			if (!contains(child, gene)) {
				child[currentPos] = gene;
				currentPos = (currentPos + 1) % length;
			}
		}

		c.getSolutionRepresentation().setSolutionRepresentation(child);
		int objValue = f.getObjectiveFunctionValue(c.getSolutionRepresentation());
		c.setObjectiveFunctionValue(objValue);

		// Log child fitness
		System.out.println(" - f(child) = " + objValue);
		System.out.println("--------------------------------");

		return objValue;
	}

	private boolean contains(int[] array, int val) {
		for (int v : array) {
			if (v == val) return true;
		}
		return false;
	}

	private boolean areIdentical(SSPSolutionInterface p1, SSPSolutionInterface p2) {
		int[] rep1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] rep2 = p2.getSolutionRepresentation().getSolutionRepresentation();
		if (rep1.length != rep2.length) return false;
		for (int i = 0; i < rep1.length; i++) {
			if (rep1[i] != rep2[i]) return false;
		}
		return true;
	}


	/*
	 * TODO update the methods below to return the correct boolean value.
	 */

	@Override
	public boolean isCrossover() {

		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.f = f;
	}
}
