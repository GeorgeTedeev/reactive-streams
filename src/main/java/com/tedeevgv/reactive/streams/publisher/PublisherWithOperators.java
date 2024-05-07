package com.tedeevgv.reactive.streams.publisher;

import com.tedeevgv.reactive.streams.processor.FilterProcessor;
import com.tedeevgv.reactive.streams.processor.MapProcessor;

import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class PublisherWithOperators<T> implements Flow.Publisher<T> {
    public final <G> PublisherWithOperators<G> map(Function<T, G> converter) {
        Consumer<Flow.Subscriber<? super T>> doSubscribe = this::subscribe;
        return new MapProcessor<>(converter, doSubscribe);
    }

    public final PublisherWithOperators<T> filter(Predicate<T> predicate) {
        Consumer<Flow.Subscriber<? super T>> doSubscribe = this::subscribe;
        return new FilterProcessor<>(predicate, doSubscribe);
    }
}
