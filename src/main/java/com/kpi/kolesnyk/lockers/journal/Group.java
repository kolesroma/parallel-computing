package com.kpi.kolesnyk.lockers.journal;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Group {
    private final String name;
    private final Map<String, Queue<Integer>> journal;

    public Group(String name) {
        this.name = name;
        this.journal = new ConcurrentHashMap<>();
    }

    public Map<String, Queue<Integer>> getJournal() {
        return journal;
    }

    public void addMark(String surname, Integer mark) {
        Queue<Integer> markList;
        synchronized (journal) {
            markList = journal.get(surname);
            if (markList == null) {
                journal.put(surname, new ConcurrentLinkedQueue<>(List.of(mark)));
                return;
            }
        }
        markList.add(mark);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", marks=" + journal +
                '}';
    }
}
