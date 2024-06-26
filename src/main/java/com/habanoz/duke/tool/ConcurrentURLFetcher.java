package com.habanoz.duke.tool;

import com.habanoz.duke.core.model.Event;
import com.habanoz.duke.core.model.EventPublisher;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public record ConcurrentURLFetcher(EventPublisher eventPublisher) {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentURLFetcher.class);

    public List<Document> fetch(List<String> urls) throws InterruptedException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<FetchURLTask> tasks = urls.stream().map((String url) -> new FetchURLTask(url, eventPublisher)).toList();
            List<Future<FetchURLTaskResult>> results = executor.invokeAll(tasks, 3, TimeUnit.SECONDS);

            List<FetchURLTaskResult> completedResults = results.stream()
                    .filter(future -> future.isDone() && !future.isCancelled())
                    .map(Future::resultNow)
                    .filter(res -> res.content() != null)
                    .toList();

            return completedResults.stream().map(res -> new Document(res.content(), Map.of("url", res.url()))).toList();
        }

    }

    private record FetchURLTaskResult(String url, String content) {
    }

    private record FetchURLTask(String url, EventPublisher eventPublisher) implements Callable<FetchURLTaskResult> {

        @Override
        public FetchURLTaskResult call() {
            try {

                try (HttpClient client = HttpClient.newHttpClient()) {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI(url))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (response.statusCode() == 200) {
                        String text = Jsoup.parse(response.body()).body().text();
                        eventPublisher.publishEvent(new Event("StatusUpdate", "Web Page Fetched:" + url));
                        return new FetchURLTaskResult(url, text);
                    } else {
                        eventPublisher.publishEvent(new Event("StatusUpdate", "Web Page Fetch Failed:" + url + " Return Code:" + response.statusCode()));
                        log.warn("Fetch URL ('{}') failed with code {}", url, response.statusCode());
                        return new FetchURLTaskResult(url, null);
                    }
                }
            } catch (InterruptedException e) {
                log.warn("Fetch URL ('{}') interrupted", url);
                eventPublisher.publishEvent(new Event("StatusUpdate", "Web Page Fetch Failed:" + url + " Reason: Interrupted"));
                return new FetchURLTaskResult(url, null);
            } catch (Exception e) {
                log.warn("Fetch URL ('{}') failed", url, e);
                eventPublisher.publishEvent(new Event("StatusUpdate", "Web Page Fetch Failed:" + url + " Reason: " + e.getMessage()));
                return new FetchURLTaskResult(url, null);
            }
        }
    }
}