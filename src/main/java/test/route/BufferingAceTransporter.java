package test.route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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
			synchronized (userList) {
				userList.add(user);
				
				int size = userList.size();
				if (size > maxSize) {
					
					List<User> temp = Collections.unmodifiableList(new ArrayList<User>(userList));
	
					String xml = xmlWriter.marshalUsers(temp);
					int responseCode = post(xml);
					
					System.out.println(responseCode);
					if (responseCode != HttpStatus.SC_OK) {
						System.out.println("oooops....not 200");
					} else {
						userList.clear();	
					}
				}
			}// sync
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private int post(String xml) throws IOException {
		if (httpclient == null) {
			httpclient = HttpClients.custom().build();
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("data", xml));
	    
	    HttpPost post = new HttpPost("http://127.0.0.1/extjs/services/1/user/sendData.json");
	    post.setEntity(new UrlEncodedFormEntity(params));
	    
        CloseableHttpResponse response = httpclient.execute(post);
	    HttpEntity entity = response.getEntity();
	    EntityUtils.consumeQuietly(entity);
	    
	    int status = response.getStatusLine().getStatusCode();
	    
	    return status;
	}
}
