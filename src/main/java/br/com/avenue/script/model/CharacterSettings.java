package br.com.avenue.script.model;

import javax.persistence.Entity;

@Entity
public class CharacterSettings {

	private Integer idCharacter;

	private Integer idSettings;
	
	public Integer getIdCharacter() {
		return idCharacter;
	}

	public void setIdCharacter(Integer idCharacter) {
		this.idCharacter = idCharacter;
	}

	public Integer getIdSettings() {
		return idSettings;
	}

	public void setIdSettings(Integer idSettings) {
		this.idSettings = idSettings;
	}

	@Override
	public String toString() {
		return "Produto [idCharacter=" + idCharacter + ", id_settings=" + idSettings +"]";
	}
}