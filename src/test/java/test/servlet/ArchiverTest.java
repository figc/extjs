package test.servlet;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ArchiverTest {

	@Test
	public void testArchiver() {
		Archiver archiver = new Archiver();
		
		ServletContext servletContext = Mockito.mock(ServletContext.class);
		MySqlDAO dao = Mockito.mock(MySqlDAO.class);
		
		WebApplicationContext webContext = Mockito.mock(WebApplicationContext.class);
		
		Mockito.when(WebApplicationContextUtils.getWebApplicationContext(servletContext)).thenReturn(webContext);
		Mockito.when(webContext.getBean(MySqlDAO.class)).thenReturn(dao);
		
		archiver.setServletContext(servletContext);
		archiver.start();
	}
}