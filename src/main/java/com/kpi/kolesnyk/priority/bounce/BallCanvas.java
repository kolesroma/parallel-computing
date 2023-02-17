package com.kpi.kolesnyk.priority.bounce;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    private final ArrayList<Ball> balls = new ArrayList<>();

    public void add(Ball ball) {
        balls.add(ball);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Ball ball : balls) {
            ball.draw(g2);
        }
    }
}