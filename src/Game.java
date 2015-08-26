public class Game {

	public static class Timer extends Thread {

		private final LanderFrame frame;

		public Timer(LanderFrame frame) {
			this.frame = frame;
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(10); // 1ms delay
					frame.gravity();
					frame.gameState();
					frame.repaint();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		new Timer(new LanderFrame()).start();
	}
}
