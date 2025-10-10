package com.aim.project.ssp.heuristics;

import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;

import java.util.Random;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 * TODO implement any common functionality here so that your heuristics can reuse them.
 *	E.g.  you may want to implement the swapping of two sight-seeing locations here.
 *
 */
public class HeuristicOperators {

	protected ObjectiveFunctionInterface m_oObjectiveFunction;

	protected Random m_oRandom;

	public HeuristicOperators(Random oRandom) {

		m_oRandom = oRandom;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		m_oObjectiveFunction = f;
	}

}
