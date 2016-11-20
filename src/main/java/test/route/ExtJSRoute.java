package test.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;


@Component("extjsRoute")
public class ExtJSRoute extends SpringRouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("seda:aceLogging")
		.setExchangePattern(ExchangePattern.InOnly)
		
		.to("bean:hello")
		
		.to("bean:goodbye")
		
		.to("bean:goodbye2")
		
		.end();
	}

}
