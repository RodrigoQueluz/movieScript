package br.com.avenue.script.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import br.com.avenue.script.model.Movie;

@Component
public class MovieDAOImpl implements MovieDAO {

	BasicDataSource dbcpDataSource;
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Movie save(Movie movie) {

		buildDataSource();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		if(namedParameterJdbcTemplate == null)
			this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		String SQL = "INSERT INTO movies (title) VALUES (:title)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("title", movie.getTitle());
		try {
			namedParameterJdbcTemplate.update(SQL, parameters, keyHolder);
			movie.setId((Integer) keyHolder.getKey());
			System.out.println("Created Record Name = " + movie.toString());
		} catch (DuplicateKeyException ex) {
			System.out.println("Registro Duplicado");
		}

		return movie;
	}

	@Override
	public Movie findByTitle(String titulo) {
		buildDataSource();

		if(namedParameterJdbcTemplate == null)
			this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("title", titulo);
		Movie result = new Movie();
		String sql = "SELECT * FROM MOVIES WHERE title=:title";

		try {
			result = namedParameterJdbcTemplate.queryForObject(

					sql, params, new MovieMapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}

	@Override
	public List<Movie> findAll() {
		buildDataSource();

		if(namedParameterJdbcTemplate == null)
			this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		
		Map<String, Object> params = new HashMap<String, Object>();

		String sql = "SELECT * FROM MOVIES";

		List<Movie> result = namedParameterJdbcTemplate.query(sql, params, new MovieMapper());

		return result;
	}
	
	private static final class MovieMapper implements RowMapper<Movie> {

		public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
			Movie movie = new Movie();
			movie.setTitle(rs.getString("title"));
			movie.setId(rs.getInt("id"));
			movie.setSettings(new SettingsDAOImpl().findByMovie(movie.getId()));
			return movie;
		}
	}

	private void buildDataSource() {
		dbcpDataSource = new BasicDataSource();
		dbcpDataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dbcpDataSource.setUrl("jdbc:hsqldb:mem:avenue");
		dbcpDataSource.setUsername("sa");
		dbcpDataSource.setPassword("");
	}

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate template) {
		this.namedParameterJdbcTemplate = template;
	}

}