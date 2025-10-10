package com.aim.project.ssp.runners;

import com.aim.project.ssp.hyperheuristics.LearningHH;

import AbstractClasses.HyperHeuristic;

public class LearningSelectionVisualRunner extends HH_Runner_Visual {

    @Override
    protected HyperHeuristic getHyperHeuristic(long seed) {
        return new LearningHH(seed);
    }

    public static void main(String[] args) {
        HH_Runner_Visual runner = new LearningSelectionVisualRunner();
        runner.run();
    }
}
