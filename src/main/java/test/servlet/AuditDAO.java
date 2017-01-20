package test.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import test.model.User;

@Repository("auditDAO")
public class AuditDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public AuditDAO(@Qualifier("derbyJdbcTemplate") JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public int logAceEvent(List<User> list) throws IOException {
		
		int records = 0;
		
		ObjectMapper mapper = new ObjectMapper();
		String json;
		for (User u : list) {
			json = mapper.writeValueAsString(u);
			System.out.println("Saving to DB : " + u.getId());
			records += jdbcTemplate.update("insert into AUDITING values (?, ?, ?)",	u.getId(), u.getEmail(), json);
		}
		
		return records;
	}
	
	public int logAceEvent(User user) throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(user);
		
		int records = jdbcTemplate.update("insert into AUDITING (AUDIT_ID, AUDIT_USER, DATA) values (?, ?, ?)",
				user.getId(), user.getEmail(), json);
		
		return records;
	}
	
	public void listAceEvents() {
		
		final ObjectMapper mapper = new ObjectMapper();
		
		String sql = "select * from AUDITING";
		List<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u = null;
				try {
					u = mapper.readValue(rs.getString("DATA"), User.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return u;
			}
		});
		
		for (User u : users) {
			System.out.println("Getting from DB : " + u.getId());
		}
	}
}
