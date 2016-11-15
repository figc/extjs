import java.util.UUID;
import java.util.concurrent.Executors;

import test.model.User;
import test.route.AceEventNotifier;
import test.route.LogEventWorker;

public class Main { 

	public static void main(String[] args) {
		
		LogEventWorker worker = new LogEventWorker();
		worker.setExecutor(Executors.newSingleThreadExecutor());
		worker.start();
		
		final AceEventNotifier notifier = new AceEventNotifier(worker);
		
		Thread t = new MyThread("FirstThread", notifier);
		Thread t2 = new MyThread("SecondThread", notifier);
		
		t.start();
		t2.start();
	}
	
	
	static class MyThread extends Thread {
		
		private AceEventNotifier eventNotifier;

		public MyThread(String name, AceEventNotifier eventNotifier) {
			setName(name);
			this.eventNotifier = eventNotifier;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				User u = new User();
				u.setId(UUID.randomUUID().toString());
				u.setName(getName() + " : " + i);
				
				eventNotifier.publishEvent(u);
			}
		}		
	}
}
