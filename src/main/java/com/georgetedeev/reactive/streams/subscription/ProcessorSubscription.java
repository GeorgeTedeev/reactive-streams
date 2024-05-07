package com.georgetedeev.reactive.streams.subscription;

import java.util.concurrent.Flow;

public class ProcessorSubscription implements Flow.Subscription {
    private final Flow.Subscription parentSubscription;
    private final String processorName;

    public ProcessorSubscription(Flow.Subscription parentSubscription,
                                 String processorName) {
        this.parentSubscription = parentSubscription;
        this.processorName = processorName;
    }

    @Override
    public void request(long n) {
        System.out.println("Processor with name '" + processorName + "' demanded data with request: " + n);
        this.parentSubscription.request(n);
    }

    @Override
    public void cancel() {
        System.out.println("Processor with name '" + processorName + "' canceled ");
        this.parentSubscription.cancel();
    }
}
