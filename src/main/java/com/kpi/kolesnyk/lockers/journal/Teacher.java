package com.kpi.kolesnyk.lockers.journal;

import java.util.Queue;

public class Teacher implements Runnable {
    private final Queue<Homework> queue;

    public Teacher(Queue<Homework> queue) {
        this.queue = queue;
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
        return queue.poll();
    }
}
