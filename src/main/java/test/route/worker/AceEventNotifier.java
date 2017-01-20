package test.route.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import test.model.User;

@Component
public class AceEventNotifier {

	private LogEventWorker worker;
	
	@Autowired
	public AceEventNotifier(LogEventWorker worker) {
		this.worker = worker;
	}

	public void setWorker(LogEventWorker worker) {
		this.worker = worker;
	}

	public void publishEvent(User user) {
		worker.offer(user);
	}
}
