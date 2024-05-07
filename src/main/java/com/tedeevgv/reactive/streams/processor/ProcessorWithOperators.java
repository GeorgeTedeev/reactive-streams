package com.tedeevgv.reactive.streams.processor;

import com.tedeevgv.reactive.streams.publisher.PublisherWithOperators;

import java.util.concurrent.Flow;

public abstract class ProcessorWithOperators<T, R> extends PublisherWithOperators<R> implements Flow.Subscriber<T> {
}
