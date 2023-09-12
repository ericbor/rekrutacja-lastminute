package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.FileParseException;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WikiScrapperTest {

    @InjectMocks
    private WikiScrapper scrapper;
    @Mock
    private WikiReader wikiReader;
    @Mock
    private WikiPageRepository repository;

    @Test
    public void testReadWhenPageHasNoLinks() {
        String link = "test-url";
        WikiPage page = getPage(link, Collections.emptyList());
        when(wikiReader.read(link)).thenReturn(page);
        doNothing().when(repository).save(page);

        scrapper.read(link);

        verify(wikiReader, times(1)).read(anyString());
        verify(repository, times(1)).save(any(WikiPage.class));
    }

    @Test
    public void testReadWhenPageHasNewLinks() {
        String link = "test-url";
        String childLink = "test-url-2";
        WikiPage page = getPage(link, List.of(childLink));
        WikiPage childPage = getPage(childLink, Collections.emptyList());
        when(wikiReader.read(link)).thenReturn(page);
        when(wikiReader.read(childLink)).thenReturn(childPage);
        doNothing().when(repository).save(any(WikiPage.class));

        scrapper.read(link);

        verify(wikiReader, times(2)).read(anyString());
        verify(repository, times(2)).save(any(WikiPage.class));
    }

    @Test
    public void testReadWhenPageHasCyclicLinks() {
        String link = "test-url";
        String childLink1 = "test-url-1";
        String childLink2 = "test-url-2";
        WikiPage page = getPage(link, List.of(childLink1, childLink2));
        WikiPage childPage1 = getPage(childLink1, List.of(link, childLink2));
        WikiPage childPage2 = getPage(childLink2, List.of(link, childLink1));

        when(wikiReader.read(link)).thenReturn(page);
        when(wikiReader.read(childLink1)).thenReturn(childPage1);
        when(wikiReader.read(childLink2)).thenReturn(childPage2);

        doNothing().when(repository).save(any(WikiPage.class));

        scrapper.read(link);

        verify(wikiReader, times(3)).read(anyString());
        verify(repository, times(3)).save(any(WikiPage.class));
    }

    @Test
    public void testReadWhenNoRootPageExists() {
        String link = "test-url";
        when(wikiReader.read(link)).thenThrow(new WikiPageNotFound("wiki page does not exist"));
        WikiPageNotFound thrown = Assertions.assertThrows(WikiPageNotFound.class, () -> {
            scrapper.read(link);
        });

        Assertions.assertEquals("wiki page does not exist", thrown.getMessage());
        verify(wikiReader, times(1)).read(link);
        verify(repository, never()).save(any(WikiPage.class));
    }

    @Test
    public void testReadWhenChildLinkThrowsException() {
        String link = "test-url";
        String childLink = "test-url-2";
        WikiPage page = getPage(link, List.of(childLink));
        when(wikiReader.read(link)).thenReturn(page);
        when(wikiReader.read(childLink)).thenThrow(new FileParseException("could not parse the file"));
        doNothing().when(repository).save(any(WikiPage.class));

        scrapper.read(link);

        verify(wikiReader, times(2)).read(anyString());
        verify(repository, times(1)).save(any(WikiPage.class));
    }

    private WikiPage getPage(String link, List<String> links) {
        return new WikiPage("title", "content", link, links);
    }
}
