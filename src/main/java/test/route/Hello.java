package test.route;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import test.model.User;

@Component
public class Hello {

    @Autowired
    @Qualifier("derbyJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

	@Handler
	public String listMessage(@Body User user) {

		final ObjectMapper mapper = new ObjectMapper();
		UUID uuid = UUID.randomUUID();
		User jsonUser = null;
		
		try {
			String json = mapper.writeValueAsString(user);
		
			int records = jdbcTemplate.update("insert into AUDITING values (?, ?, ?)",
					uuid.toString(), user.getName(), json);
		
			List<User> list = jdbcTemplate.query("select * from AUDITING", new RowMapper<User>() {
	    		@Override
	    		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
	    			User user = null;
	    			try {
	    				user = mapper.readValue(rs.getString("DATA"), User.class);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    			return user;
	    		}
			});
			
			if (!list.isEmpty()) {
				jsonUser = list.get(0);
			} else {
				jsonUser = user;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Your data is : " + jsonUser.getName() + jsonUser.getEmail() + " " + jsonUser.getResults().toString();
	}
}
