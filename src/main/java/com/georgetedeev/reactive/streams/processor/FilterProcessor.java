package com.georgetedeev.reactive.streams.processor;

import com.georgetedeev.reactive.streams.subscription.ProcessorSubscription;

import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FilterProcessor<T> extends ProcessorWithOperators<T, T> {
    private Flow.Subscription subscriptionAsSubscriber;
    private Flow.Subscriber<? super T> subscriberAsPublisher;
    private final Predicate<T> predicate;
    private final Consumer<Flow.Subscriber<? super T>> doSubscribe;
    private final String processorName = "filter";

    public FilterProcessor(Predicate<T> predicate,
                           Consumer<Flow.Subscriber<? super T>> doSubscribe) {
        this.predicate = predicate;
        this.doSubscribe = doSubscribe;
    }


    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
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

        if (predicate.test(item)) {
            subscriberAsPublisher.onNext(item);
        }
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
