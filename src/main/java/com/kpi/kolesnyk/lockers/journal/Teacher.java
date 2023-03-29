package com.kpi.kolesnyk.lockers.journal;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Teacher implements Runnable {
    private final LinkedList<Homework> queue;
    private final Lock lock;

    public Teacher(LinkedList<Homework> queue) {
        this.queue = queue;
        this.lock = new ReentrantLock();
    }

    @Override
    public void run() {
        while (true) {
            var homework = readQueue();
            if (homework == null) {
                return;
            }
            homework.group().addMark(
                    homework.surname(),
                    homework.mark()
            );
        }
    }

    private Homework readQueue() {
        lock.lock();
        try {
            return queue.pollFirst();
        } finally {
            lock.unlock();
        }
    }
}
