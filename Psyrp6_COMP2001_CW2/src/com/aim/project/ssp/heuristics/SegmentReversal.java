package com.aim.project.ssp.heuristics;

import java.util.Random;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;

/**
 * Segment Reversal Heuristic (2-opt style)
 *
 * Selects two indices and reverses the segment between them.
 *
 * @author You :)
 * @since 19/04/2025
 */
public class SegmentReversal extends HeuristicOperators implements HeuristicInterface {

    private final Random random;
    private ObjectiveFunctionInterface f;

    public SegmentReversal(Random random) {

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

        // Determine number of segment reversals to perform based on intensity
        int reversals;
        if (intensityOfMutation < 0.2)
            reversals = 1;
        else if (intensityOfMutation < 0.4)
            reversals = 2;
        else if (intensityOfMutation < 0.6)
            reversals = 3;
        else if (intensityOfMutation < 0.8)
            reversals = 4;
        else
            reversals = 5;

        for (int r = 0; r < reversals; r++) {
            int i = random.nextInt(length - 2); // pick index in range [0, length - 3]
            int j = i + 1 + random.nextInt(length - i - 1); // pick index in range [i+1, length-1]

            // Reverse the segment [i, j]
            while (i < j) {
                int temp = tour[i];
                tour[i] = tour[j];
                tour[j] = temp;
                i++;
                j--;
            }
        }

        double newObj = f.getObjectiveFunctionValue(solution.getSolutionRepresentation());
        solution.setObjectiveFunctionValue((int)Math.round(newObj));
        return newObj;
    }

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
