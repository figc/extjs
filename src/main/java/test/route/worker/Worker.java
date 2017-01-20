package test.route.worker;

import java.util.concurrent.Executor;

public abstract class Worker implements Runnable {
	
	private Executor executor;
	
	protected volatile boolean running = false;

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	
	public void start() {
		executor.execute(this);
		running = true;
	}
	
	public void stopWorker() {
		running = false;
	}
}