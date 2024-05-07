package com.tedeevgv.reactive.streams;

import com.tedeevgv.reactive.streams.publisher.SimplePublisher;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;
import org.testng.annotations.Test;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@Test
class CustomPublisherTest extends PublisherVerification<Byte> {

    public CustomPublisherTest() {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<Byte> createPublisher(long l) {
        return FlowAdapters.toPublisher(
                new SimplePublisher<>(Stream.iterate((byte) 0, UnaryOperator.identity()).limit(l).toList()));
    }

    @Override
    public Publisher<Byte> createFailedPublisher() {
        return null;
    }
}