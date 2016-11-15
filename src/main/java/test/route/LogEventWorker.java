package test.route;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import test.model.User;

public class LogEventWorker extends Thread {

	private BlockingQueue<User> queue;
	
	private Executor executor;
	
	private volatile boolean running = false;
	
	private CloseableHttpClient httpclient;

	public LogEventWorker() {
		queue = new LinkedBlockingQueue<User>();
	}
	
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	
	public void offer(User user) {
		queue.add(user);
	}

	@Override
	public synchronized void start() {
		if (executor != null) {
			executor.execute(this);
			running = true;
		}
	}

	@Override
	public void run() {

		httpclient = HttpClients.custom().build();		
		
		HttpGet httpget = new HttpGet("https://httpbin.org/");

		while (running) {
			try {
				User user = queue.take();
				
				System.out.println("got the user...." + user.getId() + " " + user.getName());
				
				System.out.println("Executing request " + httpget.getRequestLine());
				
				// send to ace
		        CloseableHttpResponse response = httpclient.execute(httpget);
		        
		        HttpEntity entity = response.getEntity();
		        EntityUtils.consume(entity);

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}	
}
