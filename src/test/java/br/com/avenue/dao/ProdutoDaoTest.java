package br.com.avenue.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.avenue.script.controllers.MovieController;
import br.com.avenue.script.daos.MovieDAO;

public class ProdutoDaoTest {

    private EmbeddedDatabase db;
    RedirectAttributes redirectAttributes;    

    MovieDAO movieDAO;
    
    @Before
    public void setUp() {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
    	db = new EmbeddedDatabaseBuilder()
    		.setName("avenue")
    		.setType(EmbeddedDatabaseType.HSQL)
    		.addScript("db/sql/create-db.sql")
    		.build();
    }

    @Test
    public void testFindByname() throws IOException {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("screenplay.txt").getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
    	
    	InputStream inputStream = multipartFile.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		String title = bufferedReader.readLine();
    	System.out.println(title);
    	MovieController movieController = new MovieController();
		movieController .gravar(multipartFile, redirectAttributes);

    }

    @After
    public void tearDown() {
        db.shutdown();
    }

}