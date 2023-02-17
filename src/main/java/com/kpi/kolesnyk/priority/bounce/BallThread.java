package com.kpi.kolesnyk.priority.bounce;

public class BallThread extends Thread {
    private static final int deltaGoal = 8;
    private static final int ballSize = 20;

    private final Ball ball;

    public BallThread(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < 10000; i++) {
                ball.move();
                if (isGoal()) {
                    interrupt();
                    ball.remove();
                    Counter.showResults();
                    return;
                }
                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isGoal() {
        boolean leftTopGoal = ball.getX() < deltaGoal && ball.getY() < deltaGoal;
        boolean leftBottomGoal = ball.getX() < deltaGoal && ball.getY() > ball.getHeight() - deltaGoal - ballSize;
        boolean rightTopGoal = ball.getX() > ball.getWidth() - deltaGoal - ballSize && ball.getY() < deltaGoal;
        boolean rightBottomGoal = ball.getX() > ball.getWidth() - deltaGoal - ballSize && ball.getY() > ball.getHeight() - deltaGoal - ballSize;
        return leftTopGoal || leftBottomGoal || rightTopGoal || rightBottomGoal;
    }
}