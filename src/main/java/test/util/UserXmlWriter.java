package test.util;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import test.model.User;

public class UserXmlWriter {

	private JAXBContext jaxbContext;
	
	public String marshalUsers(List<User> list) throws JAXBException {
		if (jaxbContext == null) {
			jaxbContext = JAXBContext.newInstance(User.class);
		}
		
		ByteArrayOutputStream baout = new ByteArrayOutputStream();

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		for (User user : list) {
			marshaller.marshal(user, baout);	
		}
	
		return new String(baout.toByteArray());
	}
}
