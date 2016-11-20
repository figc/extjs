import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Main { 

	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		CloseableHttpClient httpclient = HttpClients.custom().build();
		HttpPost post = new HttpPost("http://127.0.0.1/extjs/services/1/user/sendMessage.json");
		
        CloseableHttpResponse response = null;
        while (true) {
        	response = httpclient.execute(post);
	        HttpEntity entity = response.getEntity();
	        EntityUtils.consume(entity);
        }
	}
	
	/*
	public static void main(String[] args) {
	
		
		LogEventWorker worker = new LogEventWorker();
		worker.setExecutor(Executors.newSingleThreadExecutor());
		worker.start();
		
		final AceEventNotifier notifier = new AceEventNotifier(worker);
		
		Thread t = new MyThread("FirstThread", notifier);
		Thread t2 = new MyThread("SecondThread", notifier);
		
		t.start();
		t2.start();
	}
	static class MyThread extends Thread {
		
		private AceEventNotifier eventNotifier;

		public MyThread(String name, AceEventNotifier eventNotifier) {
			setName(name);
			this.eventNotifier = eventNotifier;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				User u = new User();
				u.setId(UUID.randomUUID().toString());
				u.setName(getName() + " : " + i);
				
				eventNotifier.publishEvent(u);
			}
		}		
	}*/
}
