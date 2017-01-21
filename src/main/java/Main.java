import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class Main { 

	public static void main(String[] args) {
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);

        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
        
		MyThread[] threads = new MyThread[5];
		
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new MyThread("MyThread [" + i + "]", httpclient);
			threads[i].start();
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
	}*/
	static class MyThread extends Thread {
		
		private final CloseableHttpClient  httpClient;
		
		public MyThread(String name, CloseableHttpClient httpClient) {
			setName(name);
			this.httpClient = httpClient;
		}
		
		@Override
		public void run() {
			
			for (;;) {
				HttpPost post = new HttpPost("http://127.0.0.1/extjs/services/1/user/sendMessage.json");
				
				System.out.println(getName() + " - sending request to " + post.getURI());
		        CloseableHttpResponse response = null;
	        	try {
					response = httpClient.execute(post);
			        HttpEntity entity = response.getEntity();
			        EntityUtils.consume(entity);
					
			        Thread.sleep(1000);
			        
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (response != null) {
							response.close();
						}
					} catch (IOException e) { }
				}
			}
		}		
	}
}
