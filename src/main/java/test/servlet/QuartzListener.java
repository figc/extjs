package test.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class QuartzListener implements ServletContextListener {

	private Archiver archiver;
	
	public QuartzListener() {
		archiver = new Archiver();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		archiver.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext servletContext = servletContextEvent.getServletContext();
		
		archiver.setServletContext(servletContext);
		archiver.start();
	}

}
