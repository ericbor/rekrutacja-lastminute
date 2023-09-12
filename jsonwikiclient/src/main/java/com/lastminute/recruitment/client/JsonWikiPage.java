package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.WikiPage;

import java.util.List;

public class JsonWikiPage {

    private String title;
    private String content;
    private String selfLink;
    private List<String> links;

    public List<String> getLinks() {
        return links;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public WikiPage toWikiPage() {
        return new WikiPage(this.title, this.content, this.selfLink, this.links);
    }
}
