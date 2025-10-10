package com.aim.project.ssp.solution;

import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 */
public class SSPSolution implements SSPSolutionInterface {

	private SolutionRepresentationInterface oRepresentation;
	
	private int iObjectiveFunctionValue;
	
	public SSPSolution(SolutionRepresentationInterface oRepresentation, int iObjectiveFunctionValue) {
		
		this.oRepresentation = oRepresentation;
		this.iObjectiveFunctionValue = iObjectiveFunctionValue;
	}

	@Override
	public int getObjectiveFunctionValue() {

		return iObjectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(int objectiveFunctionValue) {
		
		this.iObjectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {
		
		return this.oRepresentation;
	}

	@Override
	public SSPSolutionInterface clone() {
		SolutionRepresentationInterface clonedRep = this.oRepresentation.clone();
		return new SSPSolution(clonedRep, this.iObjectiveFunctionValue);
	}


	@Override
	public int getNumberOfLocations() {
		return oRepresentation.getNumberOfLocations();
	}

}
