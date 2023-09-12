package com.lastminute.recruitment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.domain.error.FileParseException;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;

import java.io.File;
import java.io.IOException;

public class JsonWikiReader implements WikiReader {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private final JsonWikiClient client;

    public JsonWikiReader(JsonWikiClient client) {
        this.client = client;
    }

    @Override
    public WikiPage read(String link) {
        File file = getFile(link);

        try {
            JsonWikiPage json = MAPPER.readValue(file, JsonWikiPage.class);

            return json.toWikiPage();
        } catch (IOException e) {
            throw new FileParseException("could not parse json file", e);
        }
    }


    private File getFile(String link) {
        try {
            String filePath = client.readJson(link);
            return new File(filePath);
        } catch (NullPointerException e) {
            throw new WikiPageNotFound(String.format("wiki page does not exist: %s", link), e);
        }
    }
}
