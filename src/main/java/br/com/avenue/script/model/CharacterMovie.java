package br.com.avenue.script.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class CharacterMovie {

	private Integer id;

	private String name;
	
	private List<Word> words;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Produto [id=" + id +", name=" + name + "]";
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}
}