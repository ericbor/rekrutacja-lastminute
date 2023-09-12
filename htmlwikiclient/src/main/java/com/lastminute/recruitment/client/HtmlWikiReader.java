package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.domain.error.FileParseException;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class HtmlWikiReader implements WikiReader {

    private final HtmlWikiClient client;

    public HtmlWikiReader(HtmlWikiClient client) {
        this.client = client;
    }

    @Override
    public WikiPage read(String link) {
        Document doc = getDocument(link);
        String title = getTitle(doc);
        String content = getContent(doc);
        String selfLink = getSelfLink(doc);
        List<String> links = getLinks(doc);

        return new WikiPage(title, content, selfLink, links);
    }

    private List<String> getLinks(Document doc) {
        Elements elements = doc.select(HtmlConstants.LINK_TAG);

        return elements.stream()
                .map(e -> e.attr(HtmlConstants.REF_ATTR))
                .toList();
    }

    private String getSelfLink(Document doc) {
        Element element = doc.selectFirst(HtmlConstants.META_TAG);

        return Objects.nonNull(element) ? element.attr(HtmlConstants.SELF_ATTR) : null;
    }

    private String getContent(Document doc) {
        Element element = doc.selectFirst(HtmlConstants.CONTENT_CLASS);

        return Objects.nonNull(element) ? element.text() : null;
    }

    private String getTitle(Document doc) {
        return doc.title();
    }

    private Document getDocument(String link) {
        File file = getFile(link);

        try {
            return Jsoup.parse(file, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new FileParseException("could not parse html file", e);
        }
    }

    private File getFile(String link) {
        try {
            String filePath = client.readHtml(link);
            return new File(filePath);
        } catch (NullPointerException e) {
            throw new WikiPageNotFound(String.format("wiki page does not exist: %s", link), e);
        }
    }
}
