package test.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import test.model.User;
import test.route.AceEventNotifier;

/**
 * A sample user service to power the EXTJS application
 */
@Controller
@RequestMapping("/1") // a version # for this services
public class UserService {
	
    private static Map<String, User> users = new HashMap<String, User>();
    static {
        User u1 = new User();
        u1.setId(UUID.randomUUID().toString());
        u1.setName("Ed");
        u1.setEmail("ed@sencha.com");
        users.put(u1.getId(), u1);

        User u2 = new User();
        u2.setId(UUID.randomUUID().toString());
        u2.setName("Tommy");
        u2.setEmail("tommy@sencha.com");
        users.put(u2.getId(), u2);
    }
    
//    private JmsTemplate jmsTemplate;
    
    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private AceEventNotifier notifier;

	/**
     * Authenticate a user
     *
     * @return the authenticated user
     */
    @RequestMapping("/user/authenticate")
    public User authenticateUser( HttpServletRequest request, HttpServletResponse response, 
    		@RequestParam("email") String email, @RequestParam("password") String password, 
    		@RequestParam(value = "salt", required = false) String salt)
    {
        //TODO replace this with your real authentication code here.

        // return a new user object for the "authenticated" user
        User user = new User();
        user.setId("1");
        user.setName(email.split("@")[0]);
        user.setEmail(email);
        return user;
    }

    /**
     * return a list of users
     */
    @RequestMapping("/user/list")
    public Collection<User> listUsers(HttpServletRequest request, HttpServletResponse response, Principal principal) {
        return users.values();
    }


    /**
     * Update a user in the system
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public Map<String, Object> updateUser(@RequestBody User parsedUser)         
    {
        User localUser = users.get(parsedUser.getId());
        if ( localUser == null ) {
            throw new RuntimeException("Invalid User");
        }

        // save changes to local user
        localUser.setName(parsedUser.getName());
        localUser.setEmail(parsedUser.getEmail());

        Map<String, Object> results = new HashMap<String, Object>();
        results.put("success", true);
        return results;
    }
    
    @RequestMapping(value = "/user/sendMessage", method = RequestMethod.POST)
    public Map<String, Object> sendMessage() {
    	/*jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createMessage();
				message.setStringProperty("username", "mario");
				message.setStringProperty("password", "oiram");
				return message;
			}
		});*/
    	
    	ArrayList<String> list = new ArrayList<String>();
    	list.add("two");
    	list.add("ten");
    	list.add("nine");
    	list.add("two");
    	
    	User user = new User();
    	user.setEmail("m_mastran@foo.ca");
    	user.setId(UUID.randomUUID().toString());
    	user.setName("Mario");
    	user.setResults(list);
    	
//    	producerTemplate.sendBody("seda:aceLogging", ExchangePattern.InOnly, user);
    	notifier.publishEvent(user);

        Map<String, Object> results = new HashMap<String, Object>();
        results.put("success", true);

        return results;
    }

    /**
     * Parse an json packet of user(s)
     */
    private Collection<User> parseUserJson( String json ) throws Exception
    {
        try
        {
            if ( json.startsWith("[") && json.endsWith("]") )
            {
                // array of users
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<Collection<User>> ref = new TypeReference<Collection<User>>(){};
                @SuppressWarnings("unchecked")
				Collection<User> user = (Collection<User>) mapper.readValue(json, ref);
                return user;
            }
            else
            {
                // Single object
                ObjectMapper mapper = new ObjectMapper();
                Collection<User> users = new ArrayList<User>();
                users.add( (User) mapper.readValue(json, User.class) );
                return users;
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Invalid USER Json");
        }
    }
}
