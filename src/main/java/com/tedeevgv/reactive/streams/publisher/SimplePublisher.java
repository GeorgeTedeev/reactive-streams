package com.tedeevgv.reactive.streams.publisher;

import com.tedeevgv.reactive.streams.subscription.CustomSubscription;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.function.Function;

public class SimplePublisher<T> extends PublisherWithOperators<T> implements AutoCloseable {

    private final Set<Flow.Subscription> subscriptions = new LinkedHashSet<>();
    private final Function<Flow.Subscription, Boolean> doUnsubscribe = this.subscriptions::remove;
    private final List<T> source;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public SimplePublisher(List<T> source) {
        this.source = Collections.unmodifiableList(source);
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        var subscription = new CustomSubscription<>(subscriber, doUnsubscribe, source, threadPool);
        subscriptions.add(subscription);

        subscriber.onSubscribe(subscription);
    }

    @Override
    public void close() {
        threadPool.shutdown();
    }
}
