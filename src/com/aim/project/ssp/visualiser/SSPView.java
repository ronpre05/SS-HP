package com.aim.project.ssp.visualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serial;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aim.project.ssp.SightseeingProblemDomain;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.solution.SSPSolution;

public class SSPView extends JFrame {

	private final int DEFAULT_SIZE = 800;

	private final SSPPanel oPanel;

	private final Color oLocationsColor;

	private final Color oRoutesColor;

	private final JFrame frame;

	public SSPView(SSPInstanceInterface oInstance, SightseeingProblemDomain oProblem, Color oCitiesColor, Color oRoutesColor) {

		this.oLocationsColor = oCitiesColor;
		this.oRoutesColor = oRoutesColor;
		this.oPanel = new SSPPanel(oInstance, oProblem);

		frame = new JFrame();
		frame.setTitle("SSP Solution Visualiser - [not to scale]");
		frame.setSize(DEFAULT_SIZE, DEFAULT_SIZE);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.add(this.oPanel);
		frame.setVisible(true);
	}

	class SSPPanel extends JPanel {

		/**
		 * 
		 */
		@Serial
		private static final long serialVersionUID = 1295525707913147839L;

		private SSPInstanceInterface oInstance;

		private SightseeingProblemDomain oProblem;

		public SSPPanel(SSPInstanceInterface oInstance, SightseeingProblemDomain oProblem) {

			this.oInstance = oInstance;
			this.oProblem = oProblem;
		}

		int map(double d, double min_x, double max_x, long out_min, long out_max) {
			return (int) ((d - min_x) * (out_max - out_min) / (max_x - min_x) + out_min);
		}

		public void updateSolution(SSPSolutionInterface[] current, SSPSolutionInterface[] candidate,
								   SSPSolutionInterface best) {

			this.repaint();
		}

		LinkedList<Color> oColorStack = new LinkedList<Color>();

		/**
		 * It's supposed to look like a runway!
		 *
		 * @param g
		 * @param x
		 * @param y
		 */
		private void drawAirport(Graphics g, int x, int y) {

			oColorStack.push(g.getColor());

			g.setColor(new Color(80, 80, 80));
			g.fillRect(x, y, 12, 12);

			g.setColor(Color.WHITE);

			// 12 x 12
			g.drawLine(x, y, x + 12, y);

			g.drawLine(x, y+5, x + 12, y+5);

			g.drawLine(x, y+2, x + 2, y+2);
			g.drawLine(x, y+4, x + 2, y+4);
			g.drawLine(x, y+6, x + 2, y+6);
			g.drawLine(x, y+8, x + 2, y+8);
			g.drawLine(x, y+10, x + 2, y+10);

			g.drawLine(x + 10, y+2, x + 12, y+2);
			g.drawLine(x + 10, y+4, x + 12, y+4);
			g.drawLine(x + 10, y+6, x + 12, y+6);
			g.drawLine(x + 10, y+8, x + 12, y+8);
			g.drawLine(x + 10, y+10, x + 12, y+10);

			g.drawLine(x, y+12, x + 12, y+12);


			g.setColor(oColorStack.pop());
		}

		private void drawHotel(Graphics g, int x, int y, int width, int height) {

			oColorStack.push(g.getColor());

			g.setColor(new Color(160, 80, 80));
			g.fillRect(x, y, width, height);

			g.setColor(Color.WHITE);

			int HALF_WIDTH = width / 2;
			int THIRD_WIDTH = width / 3;
			int TWO_THIRDS_WIDTH = THIRD_WIDTH * 2;
			
			int QUARTER_HEIGHT = height / 4;
			int HALF_HEIGHT = height / 2;

			// 12 x 12
			g.drawLine(x, y + QUARTER_HEIGHT, x + HALF_WIDTH, y);
			g.drawLine(x + HALF_WIDTH, y, x + width, y + QUARTER_HEIGHT);
			g.drawLine(x + width, y + QUARTER_HEIGHT, x, y + QUARTER_HEIGHT);
			g.drawLine(x, y + QUARTER_HEIGHT, x, y + height);
			g.drawLine(x, y + height, x + width, y + height);
			g.drawLine(x + width, y + height, x + width, y + QUARTER_HEIGHT);
			// door
			g.drawLine(x + THIRD_WIDTH, y + height, x + THIRD_WIDTH, y + HALF_HEIGHT);
			g.drawLine(x + THIRD_WIDTH, y + HALF_HEIGHT, x + TWO_THIRDS_WIDTH, y + HALF_HEIGHT);
			g.drawLine(x + TWO_THIRDS_WIDTH, y + height, x + TWO_THIRDS_WIDTH, y + HALF_HEIGHT);

			g.setColor(oColorStack.pop());
		}

		public void drawSSP(SightseeingProblemDomain oProblem, Graphics g) {

			SSPSolutionInterface solution = oProblem.getBestSolution();
			if (solution != null && solution.getSolutionRepresentation() != null
					&& solution.getSolutionRepresentation().getSolutionRepresentation() instanceof int[]) {

				SSPSolution oSSPSolution = (SSPSolution) solution;
				if (solution != null && solution.getSolutionRepresentation() != null) {

					int[] rep = oSSPSolution.getSolutionRepresentation().getSolutionRepresentation();
					Location oAirportLocation = oProblem.getLoadedInstance().getAirportLocation();
					Location oHotelLocation = oProblem.getLoadedInstance().getHotelLocation();

					int width = getWidth();
					int height = getHeight();

					double max_x = Integer.MIN_VALUE;
					double max_y = Integer.MIN_VALUE;
					double min_x = Integer.MAX_VALUE;
					double min_y = Integer.MAX_VALUE;

					// find min and max x and y coordinates
					max_x = Math.max(max_x, oAirportLocation.x());
					max_y = Math.max(max_y, oAirportLocation.y());
					min_x = Math.min(min_x, oAirportLocation.x());
					min_y = Math.min(min_y, oAirportLocation.y());

					max_x = Math.max(max_x, oHotelLocation.x());
					max_y = Math.max(max_y, oHotelLocation.y());
					min_x = Math.min(min_x, oHotelLocation.x());
					min_y = Math.min(min_y, oHotelLocation.y());

					for (int i : rep) {

						Location l = oInstance.getSightseeingLocation(rep[i]);
						max_x = Math.max(max_x, l.x());
						max_y = Math.max(max_y, l.y());
						min_x = Math.min(min_x, l.x());
						min_y = Math.min(min_y, l.y());
					}

					// draw hotel location to first sightseeing location
					int x1, x2, y1, y2;
					Location l1 = oHotelLocation, l2 = oInstance.getSightseeingLocation(rep[0]);
					x1 = map(l1.x(), min_x, max_x, 10, width - 10);
					x2 = map(l2.x(), min_x, max_x, 10, width - 10);
					y1 = height - map(l1.y(), min_y, max_y, 10, height - 10);
					y2 = height - map(l2.y(), min_y, max_y, 10, height - 10);

					g.setColor(Color.YELLOW);
					g.drawLine(x1, y1, x2, y2);

					g.setColor(oLocationsColor);
					g.fillOval(x1 - 2, y1 - 2, 4, 4);

					drawHotel(g, x1, y1, 12, 12);

					// draw sightseeing routes
					for (int i = 0; i < rep.length - 1; i++) {

						l1 = oInstance.getSightseeingLocation(rep[i]);
						l2 = oInstance.getSightseeingLocation(rep[i + 1]);

						x1 = map(l1.x(), min_x, max_x, 10, width - 10);
						x2 = map(l2.x(), min_x, max_x, 10, width - 10);
						y1 = height - map(l1.y(), min_y, max_y, 10, height - 10);
						y2 = height - map(l2.y(), min_y, max_y, 10, height - 10);

						g.setColor(oRoutesColor);
						g.drawLine(x1, y1, x2, y2);

						g.setColor(oLocationsColor);
						g.fillOval(x1 - 2, y1 - 2, 4, 4);
					}

					g.fillOval(x2 - 2, y2 - 2, 4, 4);

					// draw route from last sightseeing location to the airport
					l1 = oInstance.getSightseeingLocation(rep[rep.length - 1]);
					l2 = oAirportLocation;
					x1 = map(l1.x(), min_x, max_x, 10, width - 10);
					x2 = map(l2.x(), min_x, max_x, 10, width - 10);
					y1 = height - map(l1.y(), min_y, max_y, 10, height - 10);
					y2 = height - map(l2.y(), min_y, max_y, 10, height - 10);

					g.setColor(Color.YELLOW);
					g.drawLine(x1, y1, x2, y2);

					g.setColor(oLocationsColor);
					g.fillOval(x2 - 2, y2 - 2, 4, 4);

					drawAirport(g, x2, y2);

				}
			} else {
				g.setColor(Color.WHITE);
				System.out.println("Unsupported");
				g.drawString("Unsupported solution representation...", (int) (0), (int) (getHeight() / 2.0));
			}
		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);

			int width = getWidth();
			int height = getHeight();

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);

			if (oProblem != null) {
				this.drawSSP(oProblem, g);
			}

			g.dispose();

		}
	}
}
