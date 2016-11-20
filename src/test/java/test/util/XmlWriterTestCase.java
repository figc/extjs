package test.util;

import java.util.ArrayList;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import test.model.User;

public class XmlWriterTestCase {

	@Test
	public void testXml() throws JAXBException {
		UserXmlWriter userXmlWriter = new UserXmlWriter();
		
    	ArrayList<String> list = new ArrayList<String>();
    	list.add("two");
    	list.add("ten");
    	list.add("nine");
    	list.add("two");

		User u = new User();
		u.setId(UUID.randomUUID().toString());
		u.setEmail("foo@bar.ca");
		u.setName("Mario");
		u.setResults(list);
		
		userXmlWriter.marshalUser(u);
		
//		long bytes = userXmlWriter.numberOfBytes();
		
//		Assert.assertTrue(bytes > 0);
	}
}
