package com.kpi.kolesnyk.priority.bounce;

import javax.swing.*;
import java.awt.*;

public class BounceFrame extends JFrame {
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;

    private final BallCanvas canvas;

    public BounceFrame() {
        setSize(WIDTH, HEIGHT);
        setTitle("Bounce program");

        canvas = new BallCanvas();
        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
        Container content = getContentPane();
        content.add(canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        JButton buttonBlue = new JButton("Blue (slow)");
        JButton buttonRed = new JButton("Red (fast)");
        JButton buttonStop = new JButton("Close");

        buttonBlue.addActionListener(event -> {
            Ball b = new Ball(canvas, "blue");
            canvas.add(b);
            BallThread thread = new BallThread(b);
            thread.start();
            thread.setPriority(Thread.MIN_PRIORITY);
            try {
                thread.join(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread name = " + thread.getName());
        });

        buttonRed.addActionListener(event -> {
            Ball b = new Ball(canvas, "red");
            canvas.add(b);
            BallThread thread = new BallThread(b);
            thread.start();
            thread.setPriority(Thread.MAX_PRIORITY);
            System.out.println("Thread name = " + thread.getName());
        });

        buttonStop.addActionListener(event -> System.exit(0));

        buttonPanel.add(buttonBlue);
        buttonPanel.add(buttonRed);
        buttonPanel.add(buttonStop);
        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}