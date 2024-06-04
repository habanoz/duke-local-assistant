package com.habanoz.duke.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record DDGS(int k) {
    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";
    private static final Logger log = LoggerFactory.getLogger(DDGS.class);

    public List<SearchResult> search(String query) {
        Document doc;
        try {
            doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).get();

            ArrayList<Element> results = Optional.ofNullable(doc.getElementById("links"))
                    .map(s -> s.getElementsByClass("results_links"))
                    .map(ArrayList::new)
                    .orElseGet(ArrayList::new);

            ArrayList<SearchResult> searchResults = new ArrayList<>(results.size());
            for (Element result : results) {

                Element titleElement = result.getElementsByClass("links_main").stream().findFirst()
                        .map(s -> s.getElementsByTag("a")).map(Elements::first).orElse(null);

                if (titleElement == null) {
                    log.warn("Links not found on a result!");
                    continue;
                }

                if (!titleElement.hasAttr("href")) {
                    log.warn("Href not found on a result!");
                    continue;
                }

                Element snippetElement = result.getElementsByClass("result__snippet").first();
                String snippet = snippetElement == null ? "" : snippetElement.text();

                String title = titleElement.text();
                String hrefAttr = titleElement.attr("href");

                searchResults.add(new SearchResult(hrefAttr, title, snippet));
            }

            return searchResults.subList(0, Math.min(searchResults.size(), k));
        } catch (IOException e) {
            log.warn("Search failed", e);
            return Collections.emptyList();
        }
    }

    public record SearchResult(String href, String title, String snippet) {
    }

}