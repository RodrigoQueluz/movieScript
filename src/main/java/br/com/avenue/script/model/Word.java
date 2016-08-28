package br.com.avenue.script.model;

import javax.persistence.Entity;

@Entity
public class Word {
	
	private String word;

	private Integer idCharacter;
	
	private Integer wordCount;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Integer getIdCharacter() {
		return idCharacter;
	}

	public void setIdCharacter(Integer idCharacter) {
		this.idCharacter = idCharacter;
	}

	public Integer getWordCount() {
		return wordCount;
	}

	public void setWordCount(Integer wordCount) {
		this.wordCount = wordCount;
	}

	@Override
	public String toString() {
		return "Produto [word=" + word + ", id_character=" + idCharacter + ", word_count=" + wordCount + "]";
	}
}