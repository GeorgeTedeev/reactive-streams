package com.georgetedeev.reactive.streams.subscription;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class SimpleSubscription<T> implements Flow.Subscription {

    private final Flow.Subscriber<? super T> subscriber;
    private final Function<Flow.Subscription, Boolean> unsubscribe;
    private final AtomicBoolean terminated = new AtomicBoolean();
    private final AtomicLong demand = new AtomicLong();
    private final AtomicInteger currentPosition = new AtomicInteger();
    private final List<T> source;
    private final ExecutorService threadPool;

    public SimpleSubscription(Flow.Subscriber<? super T> subscriber,
                              Function<Flow.Subscription, Boolean> doUnsubscribe,
                              List<T> source, ExecutorService threadPool) {
        this.subscriber = subscriber;
        this.unsubscribe = doUnsubscribe;
        this.source = Collections.unmodifiableList(source);
        this.threadPool = threadPool;
    }


    @Override
    public void request(long n) {
        threadPool.submit(() -> {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException("Request param can not be less than 1"));
                return;
            }

            if (terminated.get()) {
                return;
            }

            if (demand.getAndAdd(n) > 0) return;

            do {
                int i = currentPosition.getAndIncrement();
                if (source.size() <= i) {
                    unsubscribe.apply(this);
                    terminated.getAndSet(true);
                    demand.getAndSet(0L);
                    subscriber.onComplete();
                    break;
                }

                subscriber.onNext(source.get(i));
            } while (demand.getAndDecrement() > 1);
        });
    }

    @Override
    public void cancel() {
        unsubscribe.apply(this);
        terminated.getAndSet(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleSubscription<?> that = (SimpleSubscription<?>) o;
        return Objects.equals(subscriber, that.subscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriber);
    }
}
