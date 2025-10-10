package com.aim.project.ssp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import com.aim.project.ssp.SightseeingProblemDomain;
import com.aim.project.ssp.instance.InitialisationMode;

import java.util.Arrays;

public class LearningHH extends HyperHeuristic {

    private static final double ALPHA = 0.1;   // learning rate
    private static final double DECAY = 0.95;  // decay factor
    private static final double EPSILON = 0.1; // exploration rate
    private static final double[] PARAM_VALUES = {0.1, 0.3, 0.5, 0.7, 0.9};

    private double[] heuristicScores;
    private double[][] dosScores;
    private double[][] iomScores;
    private int[] applicableHeuristics;
    private int numHeuristics;

    public LearningHH(long seed) {
        super(seed);
    }

    @Override
    protected void solve(ProblemDomain problem) {
        int currentIndex = 0, candidateIndex = 1;

        if (problem instanceof SightseeingProblemDomain) {
            ((SightseeingProblemDomain) problem).setInitialisationMode(InitialisationMode.CONSTRUCTIVE);
        }
        problem.initialiseSolution(currentIndex);
        double currentFitness = problem.getFunctionValue(currentIndex);

        // Get heuristics by types
        int[] mutations = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.MUTATION);
        int[] localSearches = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH);
        int[] crossovers = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);

        // Combine all applicable heuristics
        numHeuristics = mutations.length + localSearches.length + crossovers.length;
        applicableHeuristics = new int[numHeuristics];
        System.arraycopy(mutations, 0, applicableHeuristics, 0, mutations.length);
        System.arraycopy(localSearches, 0, applicableHeuristics, mutations.length, localSearches.length);
        System.arraycopy(crossovers, 0, applicableHeuristics, mutations.length + localSearches.length, crossovers.length);

        heuristicScores = new double[numHeuristics];
        Arrays.fill(heuristicScores, 1.0);

        dosScores = new double[numHeuristics][PARAM_VALUES.length];
        iomScores = new double[numHeuristics][PARAM_VALUES.length];
        for (int i = 0; i < numHeuristics; i++) {
            Arrays.fill(dosScores[i], 1.0);
            Arrays.fill(iomScores[i], 1.0);
        }

        while (!hasTimeExpired()) {
            int selected = selectHeuristic();
            int heuristic = applicableHeuristics[selected];

           // System.out.println("Using heuristic " + heuristic + " (" + heuristicName(heuristic) + ") [index: " + selected + "]");

            double candidateFitness = currentFitness;
            int applications = isLocalSearch(heuristic, problem) ? 5 : 1;

            int dosIndex = -1, iomIndex = -1;
            double dos = usesDOS(heuristic, problem) ? selectParamValue(dosScores[selected]) : 0.0;
            if (usesDOS(heuristic, problem)) dosIndex = getParamIndex(dos);
            double iom = usesIOM(heuristic, problem) ? selectParamValue(iomScores[selected]) : 0.0;
            if (usesIOM(heuristic, problem)) iomIndex = getParamIndex(iom);

            for (int i = 0; i < applications; i++) {
                if (problem instanceof SightseeingProblemDomain ssp) {
                    candidateFitness = ssp.applyHeuristic(heuristic, currentIndex, candidateIndex, dos, iom);
                } else {
                    candidateFitness = problem.applyHeuristic(heuristic, currentIndex, candidateIndex);
                }

                if (candidateFitness < currentFitness) {
                    problem.copySolution(candidateIndex, currentIndex);
                    currentFitness = candidateFitness;
                } else {
                    break;
                }
            }

            // Update heuristic and parameter scores
            double delta = currentFitness - candidateFitness;
            if (candidateFitness <= currentFitness) {
                heuristicScores[selected] += ALPHA * (1.0 + delta);
                if (dosIndex >= 0) dosScores[selected][dosIndex] += ALPHA;
                if (iomIndex >= 0) iomScores[selected][iomIndex] += ALPHA;
            } else {
                heuristicScores[selected] *= DECAY;
                if (dosIndex >= 0) dosScores[selected][dosIndex] *= DECAY;
                if (iomIndex >= 0) iomScores[selected][iomIndex] *= DECAY;
            }
        }
    }

    private int selectHeuristic() {
        if (rng.nextDouble() < EPSILON) {
            return rng.nextInt(numHeuristics);
        }

        double total = 0;
        double[] expScores = new double[numHeuristics];
        for (int i = 0; i < numHeuristics; i++) {
            expScores[i] = Math.exp(heuristicScores[i]);
            total += expScores[i];
        }

        double r = rng.nextDouble() * total;
        double cumulative = 0;
        for (int i = 0; i < numHeuristics; i++) {
            cumulative += expScores[i];
            if (r <= cumulative) return i;
        }

        return rng.nextInt(numHeuristics);
    }

    private double selectParamValue(double[] scores) {
        double[] expScores = new double[scores.length];
        double total = 0;
        for (int i = 0; i < scores.length; i++) {
            expScores[i] = Math.exp(scores[i]);
            total += expScores[i];
        }

        double r = rng.nextDouble() * total;
        double cumulative = 0;
        for (int i = 0; i < scores.length; i++) {
            cumulative += expScores[i];
            if (r <= cumulative) return PARAM_VALUES[i];
        }

        return PARAM_VALUES[rng.nextInt(PARAM_VALUES.length)];
    }

    private int getParamIndex(double val) {
        for (int i = 0; i < PARAM_VALUES.length; i++) {
            if (Math.abs(PARAM_VALUES[i] - val) < 1e-6)
                return i;
        }
        return 0;
    }

    private boolean usesDOS(int heuristic, ProblemDomain problem) {
        return Arrays.stream(problem.getHeuristicsThatUseDepthOfSearch()).anyMatch(h -> h == heuristic);
    }

    private boolean usesIOM(int heuristic, ProblemDomain problem) {
        return Arrays.stream(problem.getHeuristicsThatUseIntensityOfMutation()).anyMatch(h -> h == heuristic);
    }

    private boolean isLocalSearch(int heuristicId, ProblemDomain problem) {
        return problem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH) != null &&
                Arrays.stream(problem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH)).anyMatch(h -> h == heuristicId);
    }

    private String heuristicName(int id) {
        return switch (id) {
            case 0 -> "AdjacentSwap";
            case 1 -> "DavissHillClimbing";
            case 2 -> "Reinsertion";
            case 3 -> "NextDescent";
            case 4 -> "OrderCrossover";
            case 5 -> "SegmentReversal";
            case 6 -> "SteepestDescent";
            default -> "Unknown";
        };
    }

    @Override
    public String toString() {
        return "LearningSelectionHH (Adaptive Parameters + Heuristic Selection)";
    }
}
