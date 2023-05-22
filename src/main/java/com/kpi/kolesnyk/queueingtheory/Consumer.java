package com.kpi.kolesnyk.queueingtheory;

import java.util.Random;

public class Consumer extends Thread {
    private final Buffer buff;
    private final int mean;
    private final int std;
    private final String name;

    private int acceptedCount = 0;
    private boolean toEnd = false;

    public Consumer(Buffer buff, int mean, int std) {
        this.buff = buff;
        this.mean = mean;
        this.std = std;
        this.name = null;
    }

    public Consumer(Buffer buff, int mean, int std, String name) {
        this.buff = buff;
        this.mean = mean;
        this.std = std;
        this.name = name;
    }

    public void end() {
        this.toEnd = true;
    }

    public int getAcceptedCount() {
        return this.acceptedCount;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (!toEnd) {
            if (buff.get()) {
                try {
                    Thread.sleep(Math.max(0, (int) rand.nextGaussian() * std + mean));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                acceptedCount++;
                if (name != null) {
                    System.out.println("Consumer " + name + " processed client");
                }
            }
        }
    }
}
