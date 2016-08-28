package br.com.avenue.script.daos;

import java.util.List;

import br.com.avenue.script.model.CharacterMovie;

public interface CharacterMovieDAO {

    public CharacterMovie save(CharacterMovie character);

    public CharacterMovie findByName(String name);

	public List<CharacterMovie> findBySettings(Integer id);

	public List<CharacterMovie> findAll();

	public CharacterMovie findByID(String idSettings);
}