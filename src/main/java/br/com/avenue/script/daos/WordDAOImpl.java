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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.avenue.script.model.Word;

@Repository
@Transactional
public class WordDAOImpl implements WordDAO {


	private BasicDataSource dbcpDataSource;
	@Override
	public void save(Word word) {
		buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
    	String SQL = "INSERT INTO words_count VALUES (:word, :id_character, :word_count)";
		HashMap<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("word", word.getWord());
		namedParameters.put("id_character", word.getIdCharacter());
		namedParameters.put("word_count", word.getWordCount());

		try {
			namedParameterJdbcTemplate.update(SQL, namedParameters);
			System.out.println("Created Record Name = " + word.toString());
		} catch (DuplicateKeyException ex) {
			System.out.println("Registro Duplicado");
		}
	}

	@Override
	public Word findByWord(int idCharacter, String word) {
		buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("word", word);
		params.put("id_character", idCharacter);
		Word result = null;
		String sql = "SELECT * FROM words_count WHERE id_character=:id_character and word=:word";

		try {
			result = (Word) namedParameterJdbcTemplate.queryForObject(

					sql, params, new WordMapper());

		} catch (EmptyResultDataAccessException ex) {
			System.out.println("Registro nao encontrardo");
			return null;
		}
		return result;
	}
	
	@Override
	public void update(Word word) {
		buildDataSource();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		String SQL = "UPDATE words_count SET word_count = :word_count where word = :word and id_character = :id_character";
		HashMap<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("word", word.getWord());
		namedParameters.put("id_character", word.getIdCharacter());
		namedParameters.put("word_count", word.getWordCount());

		try {
			namedParameterJdbcTemplate.update(SQL, namedParameters);
			System.out.println("Created Record Name = " + word.toString());
		} catch (DuplicateKeyException ex) {
			System.out.println("Registro Duplicado");
		}
	}
	
	@Override
    public List<Word> findByCharacter(Integer id) {
		buildDataSource();

		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dbcpDataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id_character", id);
		String sql = "SELECT * FROM words_count WHERE id_character = :id_character";

		List<Word> result = namedParameterJdbcTemplate.query(sql, params, new WordMapper());

		return result;
	}

	private static final class WordMapper implements RowMapper<Word> {

		public Word mapRow(ResultSet rs, int rowNum) throws SQLException {
			Word word = new Word();
			word.setIdCharacter(rs.getInt("id_character"));
			word.setWord(rs.getString("word"));
			word.setWordCount(rs.getInt("word_count"));

			return word;
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