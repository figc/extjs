package test.route.worker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import test.model.User;

public class LogEventWorker extends Worker {
	
	private BlockingQueue<User> queue = new ArrayBlockingQueue<User>(100);
	
	private AceTransporter aceTransporter;
	
	public LogEventWorker() {
//		Runtime.getRuntime().addShutdownHook(new Thread(){
//			@Override
//			public void run() {
//				aceTransporter.flushEvents();
//				stopWorker();
//			}
//		});
	}
	
	@Override
	public void stopWorker() {
		System.out.println("About to stop worker for " + getClass().getName() + " size - " + queue.size());
		super.stopWorker();
	}

	public void setAceTransporter(AceTransporter aceTransporter) {
		this.aceTransporter = aceTransporter;
	}
	
	public void offer(User user) {
		queue.add(user);
	}

	@Override
	public void run() {
		System.out.println("Starting " + getClass().getName());
		while (running) {
			try {
				User user = queue.take();
				System.out.printf("Got the user - id : {'%s'}, name : {'%s'}", user.getId(), user.getName());
				
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
