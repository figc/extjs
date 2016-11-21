package test.route;

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
import test.servlet.AuditDAO;
import test.util.UserXmlWriter;

public class BufferingAceTransporter implements AceTransporter {

	private List<User> userList = Collections.synchronizedList(new ArrayList<User>());
	
	private int maxSize = 0;
	
	private UserXmlWriter xmlWriter;
	
	private CloseableHttpClient httpclient = null;
	
	private AuditDAO auditDAO;
	
	public BufferingAceTransporter(int maxSize, UserXmlWriter xmlWriter, AuditDAO auditDAO) {
		this.maxSize = maxSize;
		this.xmlWriter = xmlWriter;
		this.auditDAO = auditDAO;
	}

	@Override
	public void transportEvent(User user) {
		synchronized (userList) {
			try {

				userList.add(user);
				
				int size = userList.size();
				if (size > maxSize) {
					
					List<User> temp = Collections.unmodifiableList(new ArrayList<User>(userList));
					String xml = xmlWriter.marshalUsers(temp);

					int responseCode = post(xml);
					if (responseCode != HttpStatus.SC_OK) {
						int records = auditDAO.logAceEvent(temp);
						System.out.println("############Records persisted == size of "+ size + " // " + (records == size));
						
						auditDAO.listAceEvents();
					}
					userList.clear();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private int post(String xml) throws IOException {
		if (httpclient == null) {
			httpclient = HttpClients.custom().build();
		}

	    HttpPost post = new HttpPost("http://127.0.0.1/extjs/services/1/user/sendData.json");
	    
	    post.setHeader("Content-Type", "application/xml");
	    post.setEntity(new ByteArrayEntity(xml.getBytes("UTF-8")));
	    
        CloseableHttpResponse response = httpclient.execute(post);
	    HttpEntity entity = response.getEntity();
	    EntityUtils.consumeQuietly(entity);
	    
	    int status = response.getStatusLine().getStatusCode();
	    
	    return status;
	}
}
