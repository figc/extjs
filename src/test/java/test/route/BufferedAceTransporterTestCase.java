package test.route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import test.model.User;
import test.util.UserXmlWriter;

public class BufferedAceTransporterTestCase {

	
	@Test
	public void testTransport() {
		BufferingAceTransporter transporter = new BufferingAceTransporter(3, new UserXmlWriter());
		
		for (int i = 0; i < 5; i++) {
			
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(i));
			
			User u = new User();
			u.setId(UUID.randomUUID().toString());
			u.setEmail("foo" + i + "@mail.com");
			u.setName("foo" + i);
			u.setResults(list);
			
			transporter.transportEvent(u);
		}	
	}
}
