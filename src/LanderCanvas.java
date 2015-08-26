import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LanderCanvas extends JPanel {

	private int x = 150;
	private int y = 10;

	private Polygon groundPoly;
	private Polygon moonLanderPoly;

	private Rectangle2D bb;

	private int fuel = 100;

	private boolean thrusting;

	private LanderFrame frame;

	public LanderCanvas(LanderFrame frame){
		this.frame = frame;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public void paint(Graphics g) {
		/** =============== Background =================== */

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		/** ===============Fuel Gauge =================== */

		g.setColor(Color.red);
		g.fillRect(5, 5, 15, fuel);

		/** =============== Terrain =================== */

		g.setColor(Color.GRAY);
		int[] groundXS = { 0, 30, 40, 100, 140, 160, 180, 200, 220, 230, 300,
				310, 330, 350, 360, 400, 410, 435, 460, 465, 500, 545, 560,
				575, 580, 600, 600, 0 };
		int[] groundYS = { 500, 450, 480, 510, 350, 400, 395, 480, 490, 480,
				480, 520, 515, 520, 515, 550, 400, 350, 360, 400, 410, 480,
				455, 465, 480, 500, 600, 600 };

		groundPoly = new Polygon(groundXS, groundYS, groundXS.length);
		g.setColor(Color.GRAY);
		g.fillPolygon(groundPoly);

		/** =============== Moon Lander =================== */

		int[] landerXS = { 11, 13, 27, 29, 30, 26, 37, 40, 40, 30, 30, 33, 24,
				21, 24, 16, 19, 16, 7, 0, 0, 10, 10, 3, 14, 10 };
		int[] landerYS = { 5, 0, 0, 5, 20, 20, 35, 35, 40, 40, 35, 35, 20, 20,
				25, 25, 20, 20, 35, 35, 40, 40, 35, 35, 20, 20 };

		g.setColor(Color.LIGHT_GRAY);
		g.translate(x, y);
		moonLanderPoly = new Polygon(landerXS, landerYS, landerXS.length);
		g.fillPolygon(moonLanderPoly);

		/** Thrusters */
		if (thrusting) {
			g.setColor(Color.orange);
			g.translate(0, 20);
			g.fillPolygon(new int[] { 10, 30, 20 }, new int[] { 10, 10, 20 }, 3);
			g.setColor(Color.red);
			g.translate(0, 5);
			g.fillPolygon(new int[] { 10, 30, 20 }, new int[] { 10, 10, 20 }, 3);
		}

		/** Calculate bounding box of lander */
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;

		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < landerXS.length; ++i) {
			if (landerXS[i] + x < minX)
				minX = landerXS[i] + x;
			if (landerYS[i] + y < minY)
				minY = landerYS[i] + y;

			if (landerXS[i] + x > maxX)
				maxX = landerXS[i] + x;
			if (landerYS[i] + y > maxY)
				maxY = landerYS[i] + y;
		}
		bb = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

	/** =============== GAME LOGIC =================== */

	public boolean collisionDetected() {
		if (moonLanderPoly != null && groundPoly != null) {
			/* Check intersection with terrain. */
			if (groundPoly.intersects(bb)) {
				System.out.println("Collision Detected");
				return true;
			}
		}
		return false;
	}

	public void victory() {
		int result = JOptionPane.showConfirmDialog(this,
				new JLabel("You WIN! Play Again?"), "Warning!",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			/* restart game */
			resetGame();
		}
		if (result == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
	}

	public void loss() {
		int result = JOptionPane.showConfirmDialog(this,
				new JLabel("You LOSE! Play Again?"), "Warning!",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			/* restart game */
			resetGame();
		}
		if (result == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
	}

	public void resetGame(){
		this.x = 150;
		this.y = 10;
		frame.resetGameOver();
	}

	/** Allows craft to fall towards the ground */
	public void decrementY(float gravity) {
		y += gravity;
	}

	/** Allows craft to thrust upwards */
	public void incrementY(int incrementAmount) {
		y -= incrementAmount;
	}

	/** Allows craft to move left and right */
	public void moveLander(int movementX) {
		x += movementX;
	}

	/** Turns graphics of thrusting on */
	public void setThrusting(boolean bool) {
		this.thrusting = bool;
	}

	/** get height of moon lander */
	public int getDecentHeight() {
		return y;
	}

	/** get ships x position */
	public int getShipX() {
		return x;
	}

	/** set fuel gauge bar */
	public void setFuel(int fuelLeft) {
		fuel = fuelLeft / 10;
	}
}
