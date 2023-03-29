package com.kpi.kolesnyk.priority.symbol;

public class Demo {
    private boolean part2turn = false;

    public synchronized void part1() {
        for (int i = 0; i < 50; i++) {
            while (part2turn) {
                try {
                    System.out.println("\tThread t1 waiting");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Part 1 done");
            part2turn = true;
            notify();
        }
    }

    public synchronized void part2() {
        for (int i = 0; i < 50; i++) {
            while (!part2turn) {
                try {
                    System.out.println("\tThread t2 waiting");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Part 2 done");
            part2turn = false;
            notify();
        }
    }

    public static void main(String[] args) {
        Demo obj = new Demo();

        Thread t1 = new Thread(obj::part1);
        Thread t2 = new Thread(obj::part2);

        t1.start();
        t2.start();
    }
}


