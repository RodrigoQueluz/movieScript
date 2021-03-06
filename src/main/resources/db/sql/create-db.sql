
CREATE TABLE PUBLIC.MOVIES (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY , 
  title VARCHAR(300),
);

CREATE TABLE PUBLIC.movies_settings (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY , 
  id_movie int,
  name  VARCHAR(300)
);

ALTER TABLE movies_settings 
ADD FOREIGN KEY (id_movie) REFERENCES movies (id) 

CREATE TABLE PUBLIC.movies_characters (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY , 
  name  VARCHAR(300)
);

CREATE TABLE PUBLIC.movies_settings_characters (
  id_settings INTEGER,
  id_character INTEGER,
  PRIMARY KEY(id_settings, id_character)
);

ALTER TABLE movies_settings_characters 
ADD FOREIGN KEY (id_settings) REFERENCES movies_settings (id) 

ALTER TABLE movies_settings_characters 
ADD FOREIGN KEY (id_character) REFERENCES movies_characters (id) 

CREATE TABLE PUBLIC.words_count (
  word VARCHAR(100),
  id_character int,
  word_count int,
  PRIMARY KEY(word, id_character)
);

ALTER TABLE words_count 
ADD FOREIGN KEY (id_character) REFERENCES movies_characters (id) 
