package com.lastminute.recruitment;

import com.lastminute.recruitment.client.HtmlWikiClient;
import com.lastminute.recruitment.client.HtmlWikiReader;
import com.lastminute.recruitment.client.JsonWikiClient;
import com.lastminute.recruitment.client.JsonWikiReader;
import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.WikiPageRepository;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.persistence.InMemoryWikiPageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class WikiScrapperConfiguration {

    @Bean
    @Profile("html")
    public WikiReader htmlWikiReader(HtmlWikiClient htmlWikiClient) {
        return new HtmlWikiReader(htmlWikiClient);
    }

    @Bean
    @Profile("json")
    public WikiReader jsonWikiReader(JsonWikiClient jsonWikiClient) {
        return new JsonWikiReader(jsonWikiClient);
    }

    @Bean
    public WikiPageRepository wikiPageRepository() {
        return new InMemoryWikiPageRepository();
    }

    @Bean
    public WikiScrapper wikiScrapper(WikiPageRepository wikiPageRepository, WikiReader wikiReader) {
        return new WikiScrapper(wikiReader, wikiPageRepository);
    }

    @Bean
    public HtmlWikiClient htmlWikiClient() {
        return new HtmlWikiClient();
    }

    @Bean
    public JsonWikiClient jsonWikiClient() {
        return new JsonWikiClient();
    }
}
