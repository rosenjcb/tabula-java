package com.pdfextract.common;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LayoutTest {

	@Test
	public void testJson() throws JsonParseException, JsonMappingException, IOException {
		InputStream is = LayoutTest.class.getResourceAsStream("/layout.json");
		//String json = "{\"leftStartX\": \"80\"}";
		ObjectMapper m = new ObjectMapper();
		Layout l = m.readValue(is, Layout.class);
		System.out.println(l);
	}

}
