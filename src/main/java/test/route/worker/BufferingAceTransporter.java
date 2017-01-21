package test.route.worker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import test.model.User;

public class BufferingAceTransporter implements AceTransporter {

	private List<User> userList = Collections.synchronizedList(new ArrayList<User>());
	
	private int maxSize = 0;
	private FailedAceNotifier failedAceNotifier;
	private TransportManager transportManager;

	/**
	 * 
	 * @param maxSize
	 * @param failedAceNotifier
	 * @param transportManager
	 */
	public BufferingAceTransporter(int maxSize, FailedAceNotifier failedAceNotifier, TransportManager transportManager) {
		this.maxSize = maxSize;
		this.failedAceNotifier = failedAceNotifier;
		this.transportManager = transportManager;
	}

	@Override
	public void flushEvents() {
		System.out.println("Flushing events to DB");
		synchronized (userList) {
			if (!userList.isEmpty()) {
				List<User> temp = Collections.unmodifiableList(new ArrayList<User>(userList));
				userList.clear();
				System.out.println("Events to be flushed : " + temp.size());
				
				failedAceNotifier.publishEvents(temp);
			}
		}
	}

	@Override
	public void transportEvent(User user) {
		synchronized (userList) {
			List<User> temp = null;
			System.out.println("Adding event to cache..." + user.getId());
			userList.add(user);
			int size = userList.size();
			
			if (size > maxSize) {
				System.out.println("Cache limit reached...sending to ACE");
				temp = Collections.unmodifiableList(new ArrayList<User>(userList));
				userList.clear();
				
				boolean success = transportManager.sendEvents(temp);
				if (!success) {
					failedAceNotifier.publishEvents(temp);
				}
			}
		}
	}
}
