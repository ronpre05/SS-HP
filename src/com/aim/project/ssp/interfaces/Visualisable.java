package com.aim.project.ssp.interfaces;

import com.aim.project.ssp.instance.Location;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public interface Visualisable {

	/**
	 * 
	 * @return The PWP route in visited location order.
	 */
	public Location[] getRouteOrderedByLocations();
	
	/**
	 * 
	 * @return The problem instance that is currently loaded.
	 */
	public SSPInstanceInterface getLoadedInstance();
}
