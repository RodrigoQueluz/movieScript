package br.com.avenue.script.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Settings {

	private Integer id;

	private Integer idMovie;
	
	private String name;
	
	private List<CharacterMovie> characters;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdMovie() {
		return idMovie;
	}

	public void setIdMovie(Integer idMovie) {
		this.idMovie = idMovie;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Produto [id=" + id + ", id_movie=" + idMovie + ", name=" + name + "]";
	}

	public List<CharacterMovie> getCharacters() {
		return characters;
	}

	public void setCharacters(List<CharacterMovie> characters) {
		this.characters = characters;
	}
}