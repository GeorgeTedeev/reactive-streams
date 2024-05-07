package com.georgetedeev.reactive.streams;

import com.georgetedeev.reactive.streams.publisher.SimplePublisher;
import com.georgetedeev.reactive.streams.subscriber.SimpleSubscriber;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        try (var publisher = new SimplePublisher<>(List.of("Hello", "How are you?", "Fine", "And you?", "Oк", "Great"))) {
            var subscriber1 = new SimpleSubscriber<>("First", 100L);
            var subscriber2 = new SimpleSubscriber<>("Second", 2L);

            publisher
                    .map(s -> s.concat("_test"))
                    .filter(s -> !s.startsWith("Hello"))
                    .subscribe(subscriber1);

            publisher
                    .map(s -> s.getBytes(StandardCharsets.UTF_8))
                    .subscribe(subscriber2);
        }
    }
}
