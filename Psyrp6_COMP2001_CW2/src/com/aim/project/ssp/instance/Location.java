package com.aim.project.ssp.instance;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * Keeps a record of the x and y coordinates of a Location
 *
 * @param x The x-coordinate.
 * @param y The y-coordinate.
 */
public record Location (int x, int y) {


	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public int getCeilingEuclideanDistance(Location other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		return (int)Math.ceil(Math.sqrt(dx * dx + dy * dy));
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

}
