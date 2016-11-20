package test.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import test.model.User;
import test.util.UserXmlWriter;

public class BufferingAceTransporter implements AceTransporter {

	private List<User> userList = Collections.synchronizedList(new ArrayList<User>());
	
	private int maxSize = 0;
	
	private UserXmlWriter xmlWriter;
	
	public BufferingAceTransporter(int maxSize, UserXmlWriter xmlWriter) {
		this.maxSize = maxSize;
		this.xmlWriter = xmlWriter;
	}

	@Override
	public void transportEvent(User user) {
		try {
			userList.add(user);
			
			int size = userList.size();
			if (size > maxSize) {
				
				StringBuilder buf = new StringBuilder();
				String s;
				for (User u : userList) {
					s = xmlWriter.marshalUser(u);
					buf.append(s);
				}
				
				post(buf.toString());
				
				userList.clear();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	void post(String xml) {
		System.out.println(xml);
	}
}
