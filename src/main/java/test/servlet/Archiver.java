package test.servlet;

import javax.servlet.ServletContext;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Archiver {

	private Scheduler scheduler;
	
	private ServletContext servletContext;

	public Archiver() {
		System.out.println("Creating...." + getClass().getName());
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void stop() {
		System.out.println("Stopping scheduler...." + getClass().getName());
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		
		WebApplicationContext webAppContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		
		MySqlDAO dao = webAppContext.getBean(MySqlDAO.class);
		
		JobDataMap quartzMap = new JobDataMap();
		quartzMap.put("dao", dao);
		
		System.out.println("Starting scheduler...." + getClass().getName());
		JobDetail jobDetail = JobBuilder.newJob(CloseRequestScheduledJob.class)
				.withIdentity("closeRequestJob", "closeRequestGroup")
				.build();
		try {
			Trigger trigger = TriggerBuilder
					  .newTrigger()
					  .withIdentity("anyTriggerName", "group1")
					  .usingJobData(quartzMap)
					  .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")) // every 10seconds
					  .build();			
			
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(jobDetail, trigger);
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
