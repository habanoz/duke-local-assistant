package com.habanoz.duke.web;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DuckDuckGoSearch {

    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";

    public static void getSearchResults(String query) {
        Document doc = null;

        try {
            doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).get();
            Elements results = doc.getElementById("links").getElementsByClass("results_links");

            for (Element result : results) {

                Element title = result.getElementsByClass("links_main").first().getElementsByTag("a").first();
                System.out.println("\nURL:" + title.attr("href"));
                System.out.println("Title:" + title.text());
                System.out.println("Snippet:" + result.getElementsByClass("result__snippet").first().text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String query = "How to reset my windows password?";
        getSearchResults(query);
    }
}
