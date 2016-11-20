package test.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.derby.impl.sql.catalog.SYSROUTINEPERMSRowFactory;

import test.model.User;

public class UserXmlWriter {

	private JAXBContext jaxbContext;
	
	public String marshalUser(User user) throws JAXBException {
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		
//		XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(baout);
		
		if (jaxbContext == null) {
			jaxbContext = JAXBContext.newInstance(User.class);
//			countingOutputStream = new CountingOutputStream(new ByteArrayOutputStream());
		}
		
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		marshaller.marshal(user, baout);
		
		return new String(baout.toByteArray());
	}

//	public long numberOfBytes() {
//		if (countingOutputStream == null) {
//			return 0;
//		}
//		
//		return countingOutputStream.getByteCount();
//	}
//	
//	public String getXml() {
//		if (countingOutputStream == null) {
//			return "";
//		}
//		
//		return "";//countingOutputStream.;
//	}
}
