package br.com.avenue.script.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Movie {

	private Integer id;

	private String title;
	
	private List<Settings> settings;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Produto [id=" + id + ", title=" + title + "]";
	}

	public List<Settings> getSettings() {
		return settings;
	}

	public void setSettings(List<Settings> settings) {
		this.settings = settings;
	}
}