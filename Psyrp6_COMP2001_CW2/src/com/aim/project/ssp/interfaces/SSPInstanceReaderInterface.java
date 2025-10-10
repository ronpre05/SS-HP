package com.aim.project.ssp.interfaces;

import java.nio.file.Path;
import java.util.Random;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public interface SSPInstanceReaderInterface {

	/**
	 * 
	 * @param path The path to the instance file.
	 * @param random The random number generator to use.
	 *
	 * @return A new instance of the SSP problem as specified by the instance file.
	 */
	SSPInstanceInterface readSSPInstance(Path path, Random random);
}
