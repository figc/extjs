package test.route;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import test.model.User;

public class LogEventWorker extends Thread {
	
	private BlockingQueue<User> queue = new LinkedBlockingQueue<User>();
	
	private Executor executor;
	
	private volatile boolean running = false;
	
	private AceTransporter aceTransporter;
	
	public LogEventWorker() {
		// TODO register shutdown hook
	}
	
	public void setAceTransporter(AceTransporter aceTransporter) {
		this.aceTransporter = aceTransporter;
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
				User user = queue.take();
				System.out.println("got the user...." + user.getId() + " " + user.getName());
				
				aceTransporter.transportEvent(user);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}	
}
