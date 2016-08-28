package br.com.avenue.script.daos;

import br.com.avenue.script.model.CharacterSettings;

public interface CharacterMovieSettingsDAO {

    public CharacterSettings save(CharacterSettings character);

    public CharacterSettings find(int idSettings, int idCharacter);
}