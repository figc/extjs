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
	
	/**
	 * 
	 * @param auditDAO
	 */
	public PersistentWorker(AuditDAO auditDAO) {
		this.auditDAO = auditDAO;
	}

	public void offer(User user) {
		queue.add(user);
	}
	
	public void offerAll(List<User> users) {
		queue.addAll(users);
	}
	
	@Override
	public void run() {
		System.out.println("Starting " + getClass().getName());
		while (running) {
			try {
				User user = queue.take();
				
				int records = auditDAO.logAceEvent(user);
				System.out.println(records); 
				
//				auditDAO.listAceEvents();
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
				stopWorker();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Stopping " + getClass().getName());
	}
}
