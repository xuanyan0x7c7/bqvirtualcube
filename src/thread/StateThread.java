package thread;

import model.MyPanel;

public class StateThread implements Runnable {

	MyPanel panel;

	public StateThread(MyPanel panel) {
		this.panel = panel;
	}

	@Override
	public void run() {
		while (true) {
			if (panel.timer.state == MyTimer.solve
					&& panel.thread_queue.isEmpty() && panel.puzzle.isSolved()) {
				panel.thread_queue.queue.clear();
				if (!panel.nextPuzzle()) {
					panel.timer.stopTimer();
					String message = panel.puzzle.getName() + ": "
							+ MyTimer.time2str(panel.timer.finaltime);
					System.out.println(message);
				}
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
