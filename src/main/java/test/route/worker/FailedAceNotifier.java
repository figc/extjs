package test.route.worker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import test.model.User;

@Component("failedAceNotifier")
public class FailedAceNotifier {

	private PersistentWorker worker;
	
	@Autowired
	public FailedAceNotifier(PersistentWorker worker) {
		this.worker = worker;
	}

	public void publishEvents(List<User> users) {
		worker.offerAll(users);
	}

	public void publishEvent(User user) {
		worker.offer(user);
	}
}
