package com.aim.project.ssp;


import com.aim.project.ssp.heuristics.*;
import com.aim.project.ssp.instance.InitialisationMode;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.instance.SSPInstance;
import com.aim.project.ssp.instance.reader.*;
import com.aim.project.ssp.interfaces.*;

import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.solution.SSPSolution;
import com.aim.project.ssp.solution.SolutionRepresentation;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 * Ensure that you reference https://people.cs.nott.ac.uk/pszwj1/chesc2011/javadoc/index.html?help-doc.html
 * when implementing each method to be HyFlex API compliant.
 */
public class SightseeingProblemDomain extends ProblemDomain implements Visualisable, InLabPracticalExamInterface {

	private SSPInstanceInterface loadedInstance;
	private SSPSolutionInterface[] aoSolutions;
	private int iMemorySize;
	private int iBestSolutionIndex = -1;
	private InitialisationMode initMode = InitialisationMode.RANDOM;
	private final int MUTATION_ADJACENT_SWAP = 0;
	private final int LOCAL_SEARCH_DAVIS_HILL_CLIMBING = 1;
	private final int MUTATION_REINSERTION = 2;
	private final int LOCAL_SEARCH_NEXT_DESCENT = 3;
	private final int CROSSOVER_ORDER = 4;
	private final int MUTATION_SEGMENT_REVERSAL = 5;
	private final int LOCAL_SEARCH_STEEPEST_DESCENT = 6;





	private AdjacentSwap adjacentSwap;
	private DavissHillClimbing davissHillClimbing;
	private Reinsertion reinsertion;
	private NextDescent nextDescent;
	private OX orderCrossover;
	private SegmentReversal segmentReversal;
	private SteepestDescent steepestDescent;






	public SightseeingProblemDomain(long seed) {

		super(seed);

		// TODO - set default memory size and create the array of low-level heuristics
		// ...
		setMemorySize(20);
		this.adjacentSwap = new AdjacentSwap(rng);
		this.davissHillClimbing = new DavissHillClimbing(rng);
		this.reinsertion = new Reinsertion(rng);
		this.nextDescent = new NextDescent(rng);
		this.orderCrossover = new OX(rng);
		this.segmentReversal = new SegmentReversal(rng);
		this.steepestDescent = new SteepestDescent(rng);




	}

	public SSPSolutionInterface getSolution(int index) {
		return aoSolutions[index];
	}

	public SSPSolutionInterface getBestSolution() {
		return aoSolutions[iBestSolutionIndex];
	}


	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {


		copySolution(currentIndex, candidateIndex);

		switch (hIndex) {
			case MUTATION_ADJACENT_SWAP:
				System.out.println("AdjacentSwap CALLED");
				adjacentSwap.apply(getSolution(candidateIndex), 0.0, 0.8);
				break;
			case LOCAL_SEARCH_DAVIS_HILL_CLIMBING:
				System.out.println("DavisHillClimbing CALLED");
				davissHillClimbing.apply(getSolution(candidateIndex), 0.0, 0.0);
				break;
			case MUTATION_REINSERTION:
				System.out.println("Reinsertion CALLED");
				reinsertion.apply(getSolution(candidateIndex), 0.0, 0.2);
				break;
			case LOCAL_SEARCH_NEXT_DESCENT:
				System.out.println("NextDescent CALLED");
				nextDescent.apply(getSolution(candidateIndex), 0.0, 0.0); // example dos = 0.5
				break;
			case MUTATION_SEGMENT_REVERSAL:
				System.out.println("SegmentReversal CALLED");
				segmentReversal.apply(getSolution(candidateIndex), 0.0, 0.0);
				break;
			case LOCAL_SEARCH_STEEPEST_DESCENT:
				System.out.println("SteepestDescent CALLED");
				steepestDescent.apply(getSolution(candidateIndex), 0.0, 0.0);
				break;
			default:
				break;
		}

		return getFunctionValue(candidateIndex);
	}

	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex, double dos, double iom) {
		copySolution(currentIndex, candidateIndex);

		switch (hIndex) {
			case MUTATION_ADJACENT_SWAP:
				adjacentSwap.apply(getSolution(candidateIndex), dos, iom);
				break;
			case LOCAL_SEARCH_DAVIS_HILL_CLIMBING:
				davissHillClimbing.apply(getSolution(candidateIndex), dos, iom);
				break;
			case MUTATION_REINSERTION:
				reinsertion.apply(getSolution(candidateIndex), dos, iom);
				break;
			case LOCAL_SEARCH_NEXT_DESCENT:
				nextDescent.apply(getSolution(candidateIndex), dos, iom);
				break;
			case MUTATION_SEGMENT_REVERSAL:
				segmentReversal.apply(getSolution(candidateIndex), dos, iom);
				break;
			case LOCAL_SEARCH_STEEPEST_DESCENT:
				steepestDescent.apply(getSolution(candidateIndex), dos, iom);
				break;
			default:
				break;
		}

		return getFunctionValue(candidateIndex);
	}





	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		switch (hIndex) {
			case CROSSOVER_ORDER:
				System.out.println("Order Crossover CALLED");
				return orderCrossover.apply(
						getSolution(parent1Index),
						getSolution(parent2Index),
						getSolution(candidateIndex),
						0.0, 0.0
				);
			default:
				// fallback: just clone parent1 if the heuristic doesn't exist
				copySolution(parent1Index, candidateIndex);
				return getFunctionValue(candidateIndex);
		}
	}



	@Override
	public String bestSolutionToString() {
		if (iBestSolutionIndex == -1) return "No best solution found.";
		return solutionToString(iBestSolutionIndex);
	}

	@Override
	public boolean compareSolutions(int iIndex1, int iIndex2) {
		return getFunctionValue(iIndex1) < getFunctionValue(iIndex2);
	}


	@Override
	public void copySolution(int fromIndex, int toIndex) {

		// TODO - BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
		aoSolutions[toIndex] = (SSPSolutionInterface) aoSolutions[fromIndex].clone();

		if (iBestSolutionIndex == fromIndex) {
			iBestSolutionIndex = toIndex;
		}
	}

	@Override
	public double getBestSolutionValue() {
		return aoSolutions[iBestSolutionIndex].getObjectiveFunctionValue();
	}

	@Override
	public double getFunctionValue(int index) {
		return aoSolutions[index].getObjectiveFunctionValue();
	}

	// TODO
	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
        return switch (type) {
            case MUTATION -> new int[]{
                    MUTATION_ADJACENT_SWAP,
                    MUTATION_REINSERTION,
                    MUTATION_SEGMENT_REVERSAL
            };
			case LOCAL_SEARCH -> new int[]{
					LOCAL_SEARCH_DAVIS_HILL_CLIMBING,
					LOCAL_SEARCH_NEXT_DESCENT,
					LOCAL_SEARCH_STEEPEST_DESCENT
			};
			case CROSSOVER -> new int[]{CROSSOVER_ORDER};
            default -> new int[0];
        };
	}



	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		return new int[] {
				LOCAL_SEARCH_NEXT_DESCENT,
				LOCAL_SEARCH_DAVIS_HILL_CLIMBING,
				LOCAL_SEARCH_STEEPEST_DESCENT
		};
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		// No heuristics implemented yet
		return new int[] {
				MUTATION_ADJACENT_SWAP,
				MUTATION_REINSERTION,
				MUTATION_SEGMENT_REVERSAL
		};

	}


	@Override
	public int getNumberOfHeuristics() {
		return 7;
	}


	@Override
	public int getNumberOfInstances() {
		return 7; // The number of files in the instance list
	}


	@Override
	public void initialiseSolution(int index) {
		SSPSolutionInterface solution = loadedInstance.createSolution(initMode);
		aoSolutions[index] = solution;

		double value = solution.getObjectiveFunctionValue();

		if (iBestSolutionIndex == -1 || value < aoSolutions[iBestSolutionIndex].getObjectiveFunctionValue()) {
			iBestSolutionIndex = index;
		}
	}


	@Override
	public void loadInstance(int instanceId) {

		System.out.println(" loadInstance CALLED");

		// Step 1: Map instanceId to filename
		String[] instanceFiles = {
				"square.ssp", "libraries-15.ssp", "carparks-40.ssp",
				"tramstops-85.ssp", "grid.ssp", "clustered.ssp", "chatgpt-instance-100.ssp"
		};

		// Validate instanceId
		if (instanceId < 0 || instanceId >= instanceFiles.length) {
			throw new IllegalArgumentException("Invalid instance ID: " + instanceId);
		}

		String filename = instanceFiles[instanceId];

		// Step 2: Read the problem instance from file
		Path path = Paths.get("instances/ssp/" + filename); // Adjust path if needed
		Random random = new Random();

		SSPInstanceReader reader = new SSPInstanceReader();
		SSPInstance instance = (SSPInstance) reader.readSSPInstance(path, random);

		// Step 3: Set the objective function into the instance
		SSPObjectiveFunction objFunc = new SSPObjectiveFunction(instance);
		instance.setObjectiveFunction(objFunc);

		// Step 4: Store the loaded instance and objective function
		this.loadedInstance = instance;

		// Step 5: Inject the objective function into all heuristics
		this.adjacentSwap.setObjectiveFunction(objFunc);
		this.davissHillClimbing.setObjectiveFunction(objFunc);
		this.reinsertion.setObjectiveFunction(objFunc);
		this.nextDescent.setObjectiveFunction(objFunc);
		this.orderCrossover.setObjectiveFunction(objFunc);
		this.segmentReversal.setObjectiveFunction(objFunc);
		this.steepestDescent.setObjectiveFunction(objFunc);

		// Step 6: Initialize memory and solutions
		this.aoSolutions = new SSPSolutionInterface[this.iMemorySize];
		int numberOfLocations = this.loadedInstance.getNumberOfLocations();

		for (int i = 0; i < this.iMemorySize; i++) {

			// Generate a random permutation
			int[] route = IntStream.range(0, numberOfLocations).toArray();
			for (int j = numberOfLocations - 1; j > 0; j--) {
				int k = rng.nextInt(j + 1);
				int temp = route[j];
				route[j] = route[k];
				route[k] = temp;
			}

			// Wrap in a representation
			SolutionRepresentationInterface rep = new SolutionRepresentation(route);

			// Evaluate
			int value = objFunc.getObjectiveFunctionValue(rep);

			// Wrap in solution
			this.aoSolutions[i] = new SSPSolution(rep, value);
		}


		// Optional: reset best solution index
		this.iBestSolutionIndex = -1;

		System.out.println("Instance loaded: " + filename);
	}


	@Override
	public void setMemorySize(int size) {
		this.iMemorySize = size;
		aoSolutions = new SSPSolutionInterface[size];
	}

	@Override
	public String solutionToString(int index) {
		SSPSolutionInterface solution = aoSolutions[index];
		if (solution == null) return "null";

		SSPInstance instance = (SSPInstance) loadedInstance;

		StringBuilder sb = new StringBuilder();

		// Start at hotel
		Location hotel = instance.getHotelLocation();
		sb.append("(").append(hotel.getX()).append(",").append(hotel.getY()).append(") - ");

		// POIs in the tour
		int[] route = solution.getSolutionRepresentation().getSolutionRepresentation();
		for (int i : route) {
			Location loc = instance.getSightseeingLocation(i);
			sb.append("(").append(loc.getX()).append(",").append(loc.getY()).append(") - ");
		}

		// End at airport
		Location airport = instance.getAirportLocation();
		sb.append("(").append(airport.getX()).append(",").append(airport.getY()).append(")");

		return sb.toString();
	}


	@Override
	public String toString() {
		return "[Psyrp6 SSP Domain]";
	}

	@Override
	public SSPInstanceInterface getLoadedInstance() {
		return loadedInstance;
	}


	@Override
	public Location[] getRouteOrderedByLocations() {
		if (iBestSolutionIndex == -1) return new Location[0];

		SSPSolutionInterface best = aoSolutions[iBestSolutionIndex];
		int[] tour = best.getSolutionRepresentation().getSolutionRepresentation();

		SSPInstance instance = (SSPInstance) loadedInstance;

		Location[] route = new Location[tour.length + 2];
		route[0] = instance.getHotelLocation();

		for (int i = 0; i < tour.length; i++) {
			route[i + 1] = instance.getSightseeingLocation(tour[i]);
		}

		route[route.length - 1] = instance.getAirportLocation();
		return route;
	}



	@Override
	public void printBestSolutionFound() {
		if (iBestSolutionIndex == -1) {
			System.out.println("No best solution found.");
		} else {
			System.out.println("Best Solution: " + bestSolutionToString());
		}
	}


	@Override
	public void printObjectiveValueOfTheSolutionFound() {
		if (iBestSolutionIndex == -1) {
			System.out.println("No best solution found.");
		} else {
			System.out.println("Objective Value: " + getFunctionValue(iBestSolutionIndex));
		}
	}

	@Override
	public void printInitialSolution() {
		if (aoSolutions[0] != null) {
			System.out.println("Initial Solution: " + solutionToString(0));
		} else {
			System.out.println("Initial solution not generated.");
		}
	}

	@Override
	public void printObjectiveValueOfTheInitialSolution() {
		if (aoSolutions[0] != null) {
			System.out.println("Initial Objective Value: " + getFunctionValue(0));
		} else {
			System.out.println("Initial solution not generated.");
		}
	}

	public void setInitialisationMode(InitialisationMode mode) {
		this.initMode = mode;
	}
}
	/**
	 * Should print the best solution found in the form:
	 * (h_x,h_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (a_x,a_y)
	 * where:
	 * `h` is the hotel
	 * `a` is the airport
	 * `l_xi` i

	 **/