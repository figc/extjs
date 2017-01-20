package test.route;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component("goodbye")
public class GoodBye {

	@Handler
	public String listMessage(@Body String body) {
		System.out.println(body);
		
		return "The body of message is : " + body;
	}
}
