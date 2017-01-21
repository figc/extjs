package test.route.worker;

import java.io.IOException;
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


public class TransportManager {

	private UserXmlWriter xmlWriter = new UserXmlWriter();

	private CloseableHttpClient httpclient = null;
	
	public boolean sendEvents(List<User> list) {
		try {
			String xml = xmlWriter.marshalUsers(list);
	
			int responseCode = post(xml);
			System.out.println("Response code : " + responseCode);
			
			return responseCode == HttpStatus.SC_OK; 
			
		} catch (Exception ex) {
			return false;
		}
	}
	
	private int post(String xml) throws IOException {
		if (httpclient == null) {
			httpclient = HttpClients.custom().build();
		}
		
		String url = "http://127.0.0.1/extjs/services/1/user/sendData.json";

		System.out.println(url);
		
	    HttpPost post = new HttpPost(url);
	    post.setHeader("Content-Type", "application/xml");
	    post.setEntity(new ByteArrayEntity(xml.getBytes("UTF-8")));
	    
	    CloseableHttpResponse response = null;
	    try {
	    	response = httpclient.execute(post);
		    HttpEntity entity = response.getEntity();
		    EntityUtils.consumeQuietly(entity);
	    
		    return response.getStatusLine().getStatusCode();
	    } finally {
	    	if (response != null) {
	    		response.close();
	    	}
	    }
	}
}
