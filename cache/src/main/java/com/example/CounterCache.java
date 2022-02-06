package com.example;

import java.util.concurrent.atomic.AtomicLong;

public class CounterCache {

    private static final AtomicLong counter = new AtomicLong(0);

    public static long incrementAndGet() {
        return counter.incrementAndGet();
    }

}
