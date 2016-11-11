package test.route;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import test.model.User;

public class LogEventWorker extends Thread {

	private BlockingQueue<User> queue;
	
	private Executor executor;
	
	private volatile boolean running = false;

	public LogEventWorker() {
		queue = new ArrayBlockingQueue<User>(10);
	}
	
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	
	public void offer(User user) {
		queue.add(user);
	}

	@Override
	public synchronized void start() {
		if (executor != null) {
			executor.execute(this);
			running = true;
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				try {
					User user = queue.take();
					
					System.out.println("got the user...." + user);
					
					// send to ace
					
					// remove from DB
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}	
}
