package com.aim.project.ssp.interfaces;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public interface InLabPracticalExamInterface {

    /**
     * Should print the best solution found in the form:
     * (h_x,h_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (a_x,a_y)
     * where:
     *  `h` is the hotel
     *  `a` is the airport
     *  `l_xi` is the x-coordinate of the location in the i^th index in the solution.
     *  `l_yi` is the y-coordinate of the location in the i^th index in the solution.
     *
     * For example:
     *  (0,0) - (1,1) - (2,2) - (3,3) - (4,4)
     */
    void printBestSolutionFound();

    /**
     * Prints the objective value of the best solution found.
     */
    void printObjectiveValueOfTheSolutionFound();

    /**
     * Should print the initial solution:
     * (h_x,h_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (a_x,a_y)
     * where:
     *  `h` is the hotel
     *  `a` is the airport
     *  `l_xi` is the x-coordinate of the location in the i^th index in the solution.
     *  `l_yi` is the y-coordinate of the location in the i^th index in the solution.
     *
     * For example:
     *  (0,0) - (2,2) - (1,1) - (3,3) - (4,4)
     */
    void printInitialSolution();

    /**
     * Prints the objective value of the (first) initial solution generated.
     */
    void printObjectiveValueOfTheInitialSolution();
}
