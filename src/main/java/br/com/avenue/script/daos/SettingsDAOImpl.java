package br.com.avenue.script.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import br.com.avenue.script.model.Settings;

@Repository	
@Transactional
public class SettingsDAOImpl implements SettingsDAO {

	BasicDataSource dbcpDataSource;

    @Override
    public Settings save(Settings settings){
    	buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);

    	String SQL = "INSERT INTO movies_settings VALUES (DEFAULT, :id_movie, :name)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
    	MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("name", settings.getName());   
    	parameters.addValue("id_movie", settings.getIdMovie());   

          try{
        	  namedParameterJdbcTemplate.update(SQL, parameters, keyHolder);
        	  settings.setId((Integer) keyHolder.getKey());
        	  System.out.println("Created Record Name = " + settings.toString());
          } catch(DuplicateKeyException ex){
        	  System.out.println("Registro Duplicado");
          }
    	  return settings;
    }
    
    @Override
    public List<Settings> findByMovie(Integer id) {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id_movie", id);
		String sql = "SELECT * FROM movies_settings where id_movie = :id_movie";

		List<Settings> result = namedParameterJdbcTemplate.query(sql, params, new SettingsMapper());

		return result;
	}
    @Override
	public Settings findByName(String settingsName, Integer id) {
    	buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id_movie", id);
		params.put("name", settingsName);
		String sql = "SELECT * FROM movies_settings where id_movie = :id_movie and name =:name";
		Settings result = new Settings();
		try {
			result = namedParameterJdbcTemplate.queryForObject(

					sql, params, new SettingsMapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}

		return result;
	}
    

	@Override
	public Settings findByID(String idSettings) {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", idSettings);
		Settings result = new Settings();
		String sql = "SELECT * FROM MOVIES_SETTINGS WHERE id=:id";

		try {
			result = namedParameterJdbcTemplate.queryForObject(

					sql, params, new SettingsMapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}
    
	private static final class SettingsMapper implements RowMapper<Settings> {

		public Settings mapRow(ResultSet rs, int rowNum) throws SQLException {
			Settings settings = new Settings();
			settings.setId(rs.getInt("id"));
			settings.setIdMovie(rs.getInt("id_movie"));
			settings.setName(rs.getString("name"));
			settings.setCharacters(new CharacterMovieDAOImpl().findBySettings(settings.getId()));
			return settings;
		}
	}
	
    private void buildDataSource() {
		dbcpDataSource = new BasicDataSource();
		dbcpDataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dbcpDataSource.setUrl("jdbc:hsqldb:mem:avenue");
		dbcpDataSource.setUsername("sa");
		dbcpDataSource.setPassword("");
	}
}