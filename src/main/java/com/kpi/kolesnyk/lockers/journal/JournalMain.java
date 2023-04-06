package com.kpi.kolesnyk.lockers.journal;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class JournalMain {
    private static final Group it01 = new Group("IT-01");
    private static final Group it02 = new Group("IT-02");
    private static final Group it03 = new Group("IT-03");

    public static void main(String[] args) {
        var parallelComputing = new Subject(List.of(it01, it02, it03));
        var results = getStudentResults();
        for (int i = 0; i < 4; i++) {
            new Thread(new Teacher(results))
                    .start();
        }
        System.out.println(parallelComputing);
    }

    private static Queue<Homework> getStudentResults() {
        return new LinkedBlockingQueue<>(List.of(
                new Homework(it01, "roma", 10),
                new Homework(it01, "roma", 40),
                new Homework(it01, "roma", 80),
                new Homework(it02, "alex", 100),
                new Homework(it02, "alex", 20)
        ));
    }
}
