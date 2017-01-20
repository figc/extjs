package test.servlet;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CloseRequestScheduledJob implements Job {
	
//	@Autowired
//	private MySqlDAO sqlDAO;

	public CloseRequestScheduledJob() {
//		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDataMap dataMap = context.getMergedJobDataMap();
		MySqlDAO sqlDAO = (MySqlDAO) dataMap.get("dao");
		
		System.out.println(sqlDAO);		
		
		System.out.println(" Quartz 2.2.1 job running....from version 2.0");
		System.out.println("=============================");
		List<String> list = sqlDAO.doSql();
		
		for (String name : list) {
			System.out.println(name);
		}
		System.out.println("=============================");
	}
}
