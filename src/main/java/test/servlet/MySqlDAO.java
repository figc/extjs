package test.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class MySqlDAO {

	private JdbcTemplate jdbcTemplate;

	public MySqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<String> doSql() {
		
		String sql = "select name from pets";
		
		List<String> vets = jdbcTemplate.query(sql, new RowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
//				if (rs.next()) {
					return rs.getString("name");
//				}
//				return null;
			}
		});
		
		return vets;
	}
	
	
}
