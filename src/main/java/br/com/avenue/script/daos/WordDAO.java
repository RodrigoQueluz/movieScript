package br.com.avenue.script.daos;

import java.util.List;

import br.com.avenue.script.model.Word;

public interface WordDAO {

    public void save(Word word);

    public Word findByWord(int idCharacter, String word);
    
    public void update(Word word);

	public List<Word> findByCharacter(Integer id);
}