package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.WikiScrapperApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = WikiScrapperApplication.class)
@TestPropertySource(properties = "spring.profiles.active=json")
public class JsonWikiScrapperResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testScrapWikipedia() throws Exception {
        String requestBody = "\"http://wikiscrapper.test/site2\"";

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/wiki/scrap")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(requestBody));

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testScrapWikipediaThrowsNotFoundException() throws Exception {
        String requestBody = "\"http://wikiscrapper.test/site6\"";

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/wiki/scrap")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(requestBody));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("wiki page does not exist: \"http://wikiscrapper.test/site6\""));
    }

    @Test
    public void testScrapWikipediaThrowsBadResourceException() throws Exception {
        String requestBody = "\"http://wikiscrapper.test/site5\"";

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/wiki/scrap")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content(requestBody));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("could not parse json file"));
    }
}
