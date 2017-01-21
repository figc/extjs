package test.route.worker;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import test.model.User;
import test.servlet.AuditDAO;

/**
 * 
 * @author mario
 */
public class PersistentWorker extends Worker {

	private BlockingQueue<User> queue = new ArrayBlockingQueue<User>(100);

	private AuditDAO auditDAO;
	
	/** */
	public PersistentWorker(AuditDAO auditDAO) {
		this.auditDAO = auditDAO;
	}

	public void offer(User user) {
		queue.offer(user);
	}
	
	public void offerAll(List<User> users) {
		for (User u : users) {
			queue.offer(u);
		}
	}

	@Override
	public void run() {
		System.out.println("Starting " + getClass().getName());
		while (running) {
			try {
				User user = queue.take();
				
				int records = auditDAO.logAceEvent(user);
				System.out.println("Records saved : " + records); 
				
//				auditDAO.listAceEvents();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Stopping " + getClass().getName());
	}
}
