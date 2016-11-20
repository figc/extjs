package test.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;

import test.model.User;
import test.util.UserXmlWriter;

public class BufferingAceTransporter implements AceTransporter {

	private List<User> userList = Collections.synchronizedList(new ArrayList<User>());
	
	private int maxSize = 0;
	
	private UserXmlWriter xmlWriter;
	
	private CloseableHttpClient httpclient = null;
	
	public BufferingAceTransporter(int maxSize, UserXmlWriter xmlWriter) {
		this.maxSize = maxSize;
		this.xmlWriter = xmlWriter;
	}

	@Override
	public void transportEvent(User user) {
		try {
			
			// todo synch on list
			userList.add(user);
			
			int size = userList.size();
			if (size > maxSize) {
				
				List<User> temp = Collections.unmodifiableList(new ArrayList<User>(userList));

				String xml = xmlWriter.marshalUsers(temp);
				post(xml);
				
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
