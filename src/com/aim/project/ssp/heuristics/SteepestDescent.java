package com.aim.project.ssp.heuristics;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

import java.util.Random;

public class SteepestDescent extends HeuristicOperators implements HeuristicInterface {

    private final Random random;
    private ObjectiveFunctionInterface f;

    public SteepestDescent(Random random) {
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
            int bestImprovement = 0;
            int bestI = -1;

            for (int i = 0; i < length - 1; i++) {
                int[] newTour = tour.clone();

                // Perform adjacent swap
                int temp = newTour[i];
                newTour[i] = newTour[i + 1];
                newTour[i + 1] = temp;

                // Wrap and evaluate
                SolutionRepresentationInterface rep = solution.getSolutionRepresentation().clone();
                rep.setSolutionRepresentation(newTour);

                int newValue = f.getObjectiveFunctionValue(rep);

                int improvement = currentValue - newValue;
                if (improvement > bestImprovement) {
                    bestImprovement = improvement;
                    bestI = i;
                }
            }

            if (bestI == -1) break; // no improvement found

            // Apply the best move found
            int temp = tour[bestI];
            tour[bestI] = tour[bestI + 1];
            tour[bestI + 1] = temp;

            currentValue -= bestImprovement;
            solution.setObjectiveFunctionValue(currentValue);
        }

        return solution.getObjectiveFunctionValue();
    }

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

