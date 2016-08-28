package br.com.avenue.script.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import br.com.avenue.script.model.CharacterSettings;

public class CharacterMovieSettingsDAOImpl implements CharacterMovieSettingsDAO {

	private BasicDataSource dbcpDataSource;

	@Override
	public CharacterSettings save(CharacterSettings character) {
		buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		String SQL = "INSERT INTO movies_settings_characters VALUES (:id_settings, :id_character)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id_character", character.getIdCharacter());
		parameters.addValue("id_settings", character.getIdSettings());

		try {
			namedParameterJdbcTemplate.update(SQL, parameters);
			System.out.println("Created Record Name = " + character.toString());
		} catch (DuplicateKeyException ex) {
			System.out.println("Registro Duplicado");
		}
		return character;
	}

	public CharacterSettings find(int idSettings, int idCharacter) {

		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id_settings", idSettings);
		params.put("id_character", idCharacter);

		CharacterSettings result = new CharacterSettings();
		String sql = "SELECT * FROM movies_settings_characters WHERE id_settings=:id_settings and id_character = :id_character";

		try {
			result = (CharacterSettings) namedParameterJdbcTemplate.queryForObject(

					sql, params, new CharacterSettingsWrapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}

	private static final class CharacterSettingsWrapper implements RowMapper<CharacterSettings> {

		public CharacterSettings mapRow(ResultSet rs, int rowNum) throws SQLException {
			CharacterSettings character = new CharacterSettings();
			character.setIdSettings(rs.getInt("id_settings"));
			character.setIdCharacter(rs.getInt("id_character"));
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