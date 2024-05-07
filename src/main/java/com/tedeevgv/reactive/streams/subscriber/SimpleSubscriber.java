package com.tedeevgv.reactive.streams.subscriber;

import java.util.concurrent.Flow;

public class SimpleSubscriber<T> implements Flow.Subscriber<T> {
    private Flow.Subscription subscription;
    private final String name;
    private final Long request;

    public SimpleSubscriber(String name, Long request) {
        this.name = name;
        this.request = request;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("Subscriber with name '" + name + "' subscribed");
        this.subscription = subscription;

        System.out.println("Subscriber with name '" + name + "' demanded data with request: " + request);
        subscription.request(request);
    }

    @Override
    public void onNext(T item) {
        System.out.println("Subscriber with name '" + name + "' received data: " + item);

        System.out.println("Subscriber with name '" + name + "' demanded data with request: " + request);
        subscription.request(request);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Subscriber with name '" + name + "' received exception with following message: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Subscriber with name '" + name + "' completed");
    }
}
