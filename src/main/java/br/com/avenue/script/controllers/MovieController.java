package br.com.avenue.script.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.avenue.script.daos.CharacterMovieDAOImpl;
import br.com.avenue.script.daos.CharacterMovieSettingsDAOImpl;
import br.com.avenue.script.daos.MovieDAOImpl;
import br.com.avenue.script.daos.SettingsDAOImpl;
import br.com.avenue.script.daos.WordDAOImpl;
import br.com.avenue.script.model.CharacterMovie;
import br.com.avenue.script.model.CharacterSettings;
import br.com.avenue.script.model.Movie;
import br.com.avenue.script.model.Settings;
import br.com.avenue.script.model.Word;

@Controller
public class MovieController {

	private static final String BLANK = " ";
	private static final String DIALOGUE = "          ";
	private static final String CHARACTER = "                      ";

	private Movie movie = null;

	@RequestMapping("/script/form")
	public String form() {
		return "script/form";
	}

	@RequestMapping(value = "/script", method = RequestMethod.POST)
	public ModelAndView gravar(@RequestParam(value = "file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		InputStream inputStream;
		try {
			inputStream = file.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			String title = bufferedReader.readLine();
			while (title.isEmpty())
				title = bufferedReader.readLine().trim();

			if (title != null) {
				String subtitle = bufferedReader.readLine();
				while (subtitle.isEmpty())
					subtitle = bufferedReader.readLine().trim();

				if (subtitle != null) {
					String fullTitle = title.concat(" - ").concat(subtitle);
					try {
						processMovie(fullTitle);
					} catch (RuntimeException ex) {
						redirectAttributes.addFlashAttribute("message", ex.getMessage());
						return new ModelAndView("redirect:script/form");
					}

				}
			}

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (isSettings(line)) {
					Settings settings = processSettings(line);
					while ((line = bufferedReader.readLine()) != null && !isSettings(line)) {
						if (isCharacter(line)) {
							CharacterMovie character = processCharacter(line, settings);
							while ((line = bufferedReader.readLine()) != null && !isCharacter(line)) {
								if (isDialogue(line)) {
									processDialogue(line, character);
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:produtos/lista");
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String settings() {
		JSONObject jsonObject = new JSONObject();
		MovieDAOImpl movieDAO = new MovieDAOImpl();
		List<Movie> result = movieDAO.findAll();
		jsonObject.put("output", result);
		return jsonObject.toString();
	}

	@RequestMapping(value = "/settings/{value}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String getSettings(@PathVariable(value = "value") String idSettings) {
		JSONObject jsonObject = new JSONObject();
		SettingsDAOImpl settingsDAO = new SettingsDAOImpl();
		Settings settings = settingsDAO.findByID(idSettings);
		List<Settings> result = new ArrayList<Settings>();
		result.add(settings);
		jsonObject.put("output", result);
		return jsonObject.toString();
	}

	@RequestMapping(value = "/characters", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String characters() {
		JSONObject jsonObject = new JSONObject();
		CharacterMovieDAOImpl characterDAO = new CharacterMovieDAOImpl();
		List<CharacterMovie> result = characterDAO.findAll();
		jsonObject.put("output", result);
		return jsonObject.toString();
	}

	@RequestMapping(value = "/characters/{value}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public String getCharacters(@PathVariable(value = "value") String idSettings) {
		JSONObject jsonObject = new JSONObject();
		CharacterMovieDAOImpl characterDAO = new CharacterMovieDAOImpl();
		CharacterMovie character = characterDAO.findByID(idSettings);
		List<CharacterMovie> result = new ArrayList<CharacterMovie>();
		result.add(character);
		jsonObject.put("output", result);
		return jsonObject.toString();
	}

	private boolean isDialogue(String line) {
		return line.startsWith(DIALOGUE);
	}

	private boolean isCharacter(String line) {
		return line.startsWith(CHARACTER);
	}

	private CharacterMovie processCharacter(String line, Settings settings) {
		CharacterMovieDAOImpl characterDAO = new CharacterMovieDAOImpl();
		CharacterMovieSettingsDAOImpl characterSettingsDAO = new CharacterMovieSettingsDAOImpl();

		String characterName = line.trim();
		CharacterMovie character = characterDAO.findByName(characterName);

		if (character == null) {
			character = new CharacterMovie();
			character.setName(characterName);
			characterDAO.save(character);
		}

		CharacterSettings characterSettings = characterSettingsDAO.find(settings.getId(), character.getId());

		if (characterSettings == null) {
			characterSettings = new CharacterSettings();
			characterSettings.setIdSettings(settings.getId());
			characterSettings.setIdCharacter(character.getId());

			characterSettingsDAO.save(characterSettings);
		}

		return character;
	}

	private void processMovie(String fullTitle) {
		MovieDAOImpl movieDao = new MovieDAOImpl();
		this.movie = movieDao.findByTitle(fullTitle);

		if (movie != null) {
			throw new RuntimeException("Movie already Exists!");
		} else {
			movie = new Movie();
			movie.setTitle(fullTitle);
			movieDao.save(movie);
		}
	}

	private Settings processSettings(String line) {

		SettingsDAOImpl settingsDAO = new SettingsDAOImpl();

		String settingsName = "";
		if (line.startsWith("INT./EXT. ")) {
			settingsName = line.replace("INT./EXT. ", "").split("-")[0].trim();
		} else if (line.startsWith("INT. ")) {
			settingsName = line.replace("INT. ", "").split("-")[0].trim();
		} else if (line.startsWith("EXT. ")) {
			settingsName = line.replace("EXT. ", "").split("-")[0].trim();
		}
		Settings settings = settingsDAO.findByName(settingsName, movie.getId());
		if (settings == null) {
			settings = new Settings();
			settings.setName(settingsName);
			settings.setIdMovie(movie.getId());
			settingsDAO.save(settings);
		}
		return settings;
	}

	private void processDialogue(String line, CharacterMovie character) {
		for (String wordDesc : line.split(BLANK)) {
			if (!wordDesc.isEmpty()) {
				wordDesc = wordDesc.replaceAll("[,.()!?]", "");
				WordDAOImpl wordDAO = new WordDAOImpl();
				Word word = wordDAO.findByWord(character.getId(), wordDesc);
				if (word == null) {
					word = new Word();
					word.setIdCharacter(null);
					word.setWordCount(1);
					word.setWord(wordDesc);
					word.setIdCharacter(character.getId());
					wordDAO.save(word);
				} else {
					int count = word.getWordCount() + 1;
					word.setWordCount(count);
					wordDAO.update(word);
				}
			}
		}
	}

	private boolean isSettings(String line) {
		return line.startsWith("INT.") || line.startsWith("EXT.");
	}
	
	public static void main(String[] args) {
		//INITIALIZE
    }

}
