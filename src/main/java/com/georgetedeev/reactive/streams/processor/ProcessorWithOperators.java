package com.georgetedeev.reactive.streams.processor;

import com.georgetedeev.reactive.streams.publisher.PublisherWithOperators;

import java.util.concurrent.Flow;

public abstract class ProcessorWithOperators<T, R> extends PublisherWithOperators<R> implements Flow.Subscriber<T> {
}
