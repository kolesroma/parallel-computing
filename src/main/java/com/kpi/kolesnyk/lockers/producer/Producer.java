package com.kpi.kolesnyk.lockers.producer;

import java.util.Random;
import java.util.stream.Stream;

public class Producer implements Runnable {
    private final Drop drop;

    public Producer(Drop drop) {
        this.drop = drop;
    }

    @Override
    public void run() {
        Random random = new Random();
        Stream.generate(() -> random.nextInt(500))
                .limit(100)
                .map(String::valueOf)
                .forEach(drop::put);
        drop.put("DONE");
    }
}
