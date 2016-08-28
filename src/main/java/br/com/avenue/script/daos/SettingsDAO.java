package br.com.avenue.script.daos;

import java.util.List;

import br.com.avenue.script.model.Settings;

public interface SettingsDAO {

    public Settings save(Settings settings);

	public List<Settings> findByMovie(Integer id);

	public Settings findByName(String settingsName, Integer id);

	public Settings findByID(String idSettings);

}