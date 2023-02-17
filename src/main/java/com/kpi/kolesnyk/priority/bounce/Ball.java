package com.kpi.kolesnyk.priority.bounce;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

class Ball {
    private static final int XSIZE = 20;
    private static final int YSIZE = 20;

    private final Component canvas;
    private final String color;

    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;

    public Ball(Component c, String color) {
        canvas = c;
        this.color = color;
        if (Math.random() < 0.5) {
            x = new Random().nextInt(getWidth());
            y = 0;
        } else {
            x = 0;
            y = new Random().nextInt(getHeight());
        }
    }

    public int getWidth() {
        return canvas.getWidth();
    }

    public int getHeight() {
        return canvas.getHeight();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColor() {
        return color;
    }

    public void remove() {
        x = -50;
        y = -50;
    }

    public void draw(Graphics2D g2) {
        if ("blue".equals(color)) {
            g2.setColor(Color.blue);
        } else if ("red".equals(color)) {
            g2.setColor(Color.red);
        } else {
            throw new IllegalArgumentException("color should be either red or blue");
        }
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < 0) {
            x = 0;
            dx = -dx;
        } else if (x + XSIZE >= canvas.getWidth()) {
            x = canvas.getWidth() - XSIZE;
            dx = -dx;
        }
        if (y < 0) {
            y = 0;
            dy = -dy;
        } else if (y + YSIZE >= canvas.getHeight()) {
            y = canvas.getHeight() - YSIZE;
            dy = -dy;
        }
        canvas.repaint();
    }
}