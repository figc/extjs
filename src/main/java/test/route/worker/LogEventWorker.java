package test.route.worker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import test.model.User;

public class LogEventWorker extends Worker {
	
	private BlockingQueue<User> queue = new ArrayBlockingQueue<User>(100);
	
	private AceTransporter aceTransporter;
	
	public LogEventWorker() {	}
	
	@Override
	public void stopWorker() {
		System.out.println("About to stop worker for " + getClass().getName() + " size - " + queue.size());
		aceTransporter.flushEvents();
		super.stopWorker();
	}

	public void setAceTransporter(AceTransporter aceTransporter) {
		this.aceTransporter = aceTransporter;
	}
	
	public void offer(User user) {
		queue.offer(user);
	}

	@Override
	public void run() {
		System.out.println("Starting " + getClass().getName());
		while (running) {
			try {
				User user = queue.take();
				System.out.printf("Got the user - id : {'%s'}, name : {'%s'}\n", user.getId(), user.getName() );
				
				aceTransporter.transportEvent(user);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
				stopWorker();
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Stopping " + getClass().getName());
	}	
}
