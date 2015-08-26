import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class LanderFrame extends JFrame implements KeyListener {

	private LanderCanvas canvas;

	private static final int WINDOW_SIZE = 600;
	private static final float GRAVITY = 1f;
	private static final int MOVEMENT_X = 5;
	private static final int FUEL_USAGE = 5;
	private static final float TERMINAL_VELOCITY = 5f;

	private float velocity = 0f;
	private int fuelLeft = 1000;
	private int thrusterStrength = 5;

	private boolean gameOver;

	public LanderFrame() {
		super("Moon Lander");
		canvas = new LanderCanvas(this); // create canvas
		setLayout(new BorderLayout()); // use border layout
		add(canvas, BorderLayout.CENTER); // add canvas
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack(); // pack components tightly together
		setResizable(false); // prevent us from being resizeable
		addKeyListener(this);
		setVisible(true); // make sure we are visible!
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WINDOW_SIZE, WINDOW_SIZE);
	}

	public void gravity() {
		if (velocity < TERMINAL_VELOCITY)
			velocity += 0.01f;
		canvas.decrementY(GRAVITY + velocity);
	}

	public boolean safeLanding() {
		/** landing on flat terrain */
		int shipXPos = canvas.getShipX();
		System.out.println("Velocity: " + velocity);
		if (velocity <= 1.2 && shipXPos >= 230 && shipXPos <= 300)
			return true;
		return false;
	}

	public void gameState() {
		if (gameOver == true)
			return;
		if (canvas.collisionDetected()) {
			gameOver = true;
			if (safeLanding()) {
				canvas.victory();
			} else {
				canvas.loss();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {
			canvas.moveLander(MOVEMENT_X);
		} else if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
			canvas.moveLander(-MOVEMENT_X);
		} else if (code == KeyEvent.VK_UP || code == KeyEvent.VK_KP_UP) {
			if (fuelLeft <= 0) { // If the fuel has been used, do nothing.
				return;
			}
			velocity -= 0.5f;
			fuelLeft -= FUEL_USAGE;
			canvas.setFuel(fuelLeft);
			canvas.incrementY(thrusterStrength);
			canvas.setThrusting(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		canvas.setThrusting(false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void resetGameOver() {
		gameOver = false;
		velocity = 0f;
		fuelLeft = 1000;
	}
}
