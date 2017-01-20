package test.route.worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import test.model.User;
import test.util.UserXmlWriter;

public class BufferingAceTransporter implements AceTransporter {

	private List<User> userList = Collections.synchronizedList(new ArrayList<User>());
	
	private int maxSize = 0;
	
	private UserXmlWriter xmlWriter;
	
	private CloseableHttpClient httpclient = null;
	
	private FailedAceNotifier failedAceNotifier;
	

	public BufferingAceTransporter(int maxSize, UserXmlWriter xmlWriter, FailedAceNotifier failedAceNotifier) {
		this.maxSize = maxSize;
		this.xmlWriter = xmlWriter;
		this.failedAceNotifier = failedAceNotifier;
	}

	@Override
	public void flushEvents() {
		System.out.println("Flushing events to DB");
		synchronized (userList) {
			if (!userList.isEmpty()) {
				List<User> temp = Collections.unmodifiableList(new ArrayList<User>(userList));
				userList.clear();
				System.out.println(userList.size());
				System.out.println(temp.size());
			}
		}
	}

	@Override
	public void transportEvent(User user) {
		synchronized (userList) {
			List<User> temp = null;
			try {
				userList.add(user);
				int size = userList.size();
				
				if (size > maxSize) {
					temp = Collections.unmodifiableList(new ArrayList<User>(userList));
					userList.clear();
					
					String xml = xmlWriter.marshalUsers(temp);

					int responseCode = post(xml);
					System.out.println(responseCode);
					
					if (responseCode != HttpStatus.SC_OK) {
						failedAceNotifier.publishEvents(temp);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				if (temp != null) {
					failedAceNotifier.publishEvents(temp);
				}
			}
		}
	}
	
	private int post(String xml) throws IOException {
		if (httpclient == null) {
			httpclient = HttpClients.custom().build();
		}
		
		String url = "http://127.0.0.1/extjs/services/1/user/SSsendData.json";

		System.out.println(url);
		
	    HttpPost post = new HttpPost(url);
	    
	    post.setHeader("Content-Type", "application/xml");
	    post.setEntity(new ByteArrayEntity(xml.getBytes("UTF-8")));
	    
        CloseableHttpResponse response = httpclient.execute(post);
	    HttpEntity entity = response.getEntity();
	    EntityUtils.consumeQuietly(entity);
	    
	    int status = response.getStatusLine().getStatusCode();
	    
	    return status;
	}
}
