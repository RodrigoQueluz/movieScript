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

import br.com.avenue.script.model.CharacterMovie;

@Repository	
@Transactional
public class CharacterMovieDAOImpl implements CharacterMovieDAO {

	
    private BasicDataSource dbcpDataSource;

	@Override
    public CharacterMovie save(CharacterMovie character){
    	buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
    	String SQL = "INSERT INTO movies_characters VALUES (DEFAULT, :name)";
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	MapSqlParameterSource parameters = new MapSqlParameterSource();
    	parameters.addValue("name", character.getName());   

          try{
        	  namedParameterJdbcTemplate.update(SQL, parameters,keyHolder);
        	  character.setId((Integer) keyHolder.getKey());
        	  System.out.println("Created Record Name = " + character.toString());
          } catch(DuplicateKeyException ex){
        	  System.out.println("Registro Duplicado");
          }
          return character;
    }
	
	@Override
	public CharacterMovie findByName(String characterName) {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", characterName);
		CharacterMovie result = new CharacterMovie();
		String sql = "SELECT * FROM MOVIES_CHARACTERS WHERE name=:name";

		try {
			result = (CharacterMovie) namedParameterJdbcTemplate.queryForObject(

					sql, params, new CharacterMovieWrapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}
	
	@Override
    public List<CharacterMovie> findBySettings(Integer id) {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "SELECT * FROM movies_characters WHERE id in (SELECT id_character FROM movies_settings_characters where id_settings = :id)";

		List<CharacterMovie> result = namedParameterJdbcTemplate.query(sql, params, new CharacterMovieWrapper());

		return result;
	}
	
	@Override
	public List<CharacterMovie> findAll() {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		
		Map<String, Object> params = new HashMap<String, Object>();

		String sql = "SELECT * FROM movies_characters";

		List<CharacterMovie> result = namedParameterJdbcTemplate.query(sql, params, new CharacterMovieWrapper());

		return result;
	}
	

	@Override
	public CharacterMovie findByID(String idCharacter) {
		
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", idCharacter);
		CharacterMovie result = new CharacterMovie();
		String sql = "SELECT * FROM MOVIES_CHARACTERS WHERE id=:id";

		try {
			result = namedParameterJdbcTemplate.queryForObject(

					sql, params, new CharacterMovieWrapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}
	
	private static final class CharacterMovieWrapper implements RowMapper<CharacterMovie> {

		public CharacterMovie mapRow(ResultSet rs, int rowNum) throws SQLException {
			CharacterMovie character = new CharacterMovie();
			character.setName(rs.getString("name"));
			character.setId(rs.getInt("id"));
			character.setWords(new WordDAOImpl().findByCharacter(character.getId()));
			return character;
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