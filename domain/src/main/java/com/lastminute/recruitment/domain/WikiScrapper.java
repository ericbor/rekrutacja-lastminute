package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.FileParseException;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class WikiScrapper {

    private final WikiReader wikiReader;
    private final WikiPageRepository repository;

    public WikiScrapper(WikiReader wikiReader, WikiPageRepository repository) {
        this.wikiReader = wikiReader;
        this.repository = repository;
    }


    public void read(String link) {
        WikiPage rootPage = wikiReader.read(link);
        Queue<WikiPage> queue = new LinkedList<>();
        queue.add(rootPage);

        processChildLinks(queue);
    }

    private void processChildLinks(Queue<WikiPage> queue) {
        Set<String> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            WikiPage page = queue.poll();
            visited.add(page.getSelfLink());
            repository.save(page);

            for (String childLink : page.getLinks()) {
                if (visited.contains(childLink)) {
                    continue;
                }

                try {
                    WikiPage childPage = wikiReader.read(childLink);
                    queue.add(childPage);
                    visited.add(childLink);
                } catch (FileParseException | WikiPageNotFound e) {
                    System.out.printf("could not process wiki page: %s%n", childLink);
                }
            }
        }
    }
}
