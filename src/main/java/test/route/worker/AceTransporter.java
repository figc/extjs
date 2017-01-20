package test.route.worker;

import test.model.User;

public interface AceTransporter {

	void transportEvent(User user);
	
	void flushEvents();
}
