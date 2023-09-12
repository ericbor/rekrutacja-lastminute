package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.domain.WikiScrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/wiki")
@RestController
public class WikiScrapperResource {

    private final WikiScrapper scrapper;

    public WikiScrapperResource(WikiScrapper scrapper) {
        this.scrapper = scrapper;
    }

    @PostMapping("/scrap")
    public ResponseEntity<Void> scrapWikipedia(@RequestBody String link) {
        scrapper.read(link);

        return ResponseEntity.ok().build();
    }
}
