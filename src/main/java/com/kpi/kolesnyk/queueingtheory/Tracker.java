package com.kpi.kolesnyk.queueingtheory;

import java.util.ArrayList;

public class Tracker extends Thread{
    private final Buffer buff;
    private final int interval;
    private final ArrayList<Integer> log = new ArrayList<>();

    private boolean toEnd = false;

    public Tracker(Buffer buff, int interval) {
        this.buff = buff;
        this.interval = interval;
    }

    public void end() {
        toEnd = true;
    }

    public ArrayList<Integer> getLog() {
        return this.log;
    }

    @Override
    public void run() {
        while(!toEnd) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.add(buff.getSize());
        }
    }
}
