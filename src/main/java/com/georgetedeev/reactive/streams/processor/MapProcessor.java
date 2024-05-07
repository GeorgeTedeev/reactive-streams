package com.georgetedeev.reactive.streams.processor;

import com.georgetedeev.reactive.streams.subscription.ProcessorSubscription;

import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapProcessor<T, R> extends ProcessorWithOperators<T, R> {
    private Flow.Subscription subscriptionAsSubscriber;
    private Flow.Subscriber<? super R> subscriberAsPublisher;
    private final Function<T, R> converter;
    private final Consumer<Flow.Subscriber<? super T>> doSubscribe;
    private final String processorName = "map";

    public MapProcessor(Function<T, R> converter,
                        Consumer<Flow.Subscriber<? super T>> doSubscribe) {
        this.converter = converter;
        this.doSubscribe = doSubscribe;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super R> subscriber) {
        doSubscribe.accept(this);

        this.subscriberAsPublisher = subscriber;
        var subscription = new ProcessorSubscription(this.subscriptionAsSubscriber, processorName);
        subscriber.onSubscribe(subscription);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("Processor with name '" + processorName + "' subscribed");

        this.subscriptionAsSubscriber = subscription;
    }

    @Override
    public void onNext(T item) {
        System.out.println("Processor with name '" + processorName + "' received data: " + item);

        subscriberAsPublisher.onNext(this.converter.apply(item));
    }

    @Override
    public void onError(Throwable throwable) {
        subscriberAsPublisher.onError(throwable);
    }

    @Override
    public void onComplete() {
        subscriberAsPublisher.onComplete();
    }
}
