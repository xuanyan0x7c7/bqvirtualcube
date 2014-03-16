package thread;

import java.util.LinkedList;
import java.util.Queue;

import puzzle.Puzzle;

public class ThreadQueue implements Runnable {

	private Puzzle puzzle;
	public Queue<Integer> queue;
	private static int face;

	public ThreadQueue(Puzzle puzzle) {
		this.puzzle = puzzle;
		queue = new LinkedList<Integer>();
	}

	public void apply(int key) {
		queue.offer(key);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public void run() {
		while (true) {
			if (!queue.isEmpty()) {
				Integer k = queue.peek();
				if (puzzle.isParallel(k, face) || MyThread.running == 0) {
					queue.remove();
					face = k;
					++MyThread.running;
					new Thread(new MyThread(puzzle, k)).start();
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
