package br.com.avenue.script.daos;

import java.util.List;

import br.com.avenue.script.model.Movie;

public interface MovieDAO {

    public Movie save(Movie movie);

    public Movie findByTitle(String title);

	public List<Movie> findAll();
}