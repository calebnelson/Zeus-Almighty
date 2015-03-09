package cs48.zeusalmighty;

import android.graphics.Canvas;

/**
 * GameThread is a cutomized thread used to draw the graphics of the game. By default the thread is set to 
 * draw at 10 frames per second.
 * @author Group 9
 */

public class GameThread extends Thread {
    
    //instance variables
    static final long FPS = 10;
    private GameView view;
    private boolean running = false;

    /**
     * Constructor for GameThread objects
     * @param view      gameview that thread will be running on
     */
    public GameThread(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    /**
     * Draws the graphics for the entire game, updating 10 times per second
     */
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }
}
