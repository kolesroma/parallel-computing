package com.kpi.kolesnyk.lockers.journal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Group {
    private final String name;
    private final Map<String, List<Integer>> journal;
    private final Lock lock;

    public Group(String name) {
        this.name = name;
        this.journal = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public Map<String, List<Integer>> getJournal() {
        return journal;
    }

    public void addMark(String surname, Integer mark) {
        synchronized (journal) {
            lock.lock();
            try {
                List<Integer> markList = journal.get(surname);
                if (markList == null) {
                    journal.put(surname, new ArrayList<>(List.of(mark)));
                    return;
                }
                markList.add(mark);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", marks=" + journal +
                '}';
    }
}
