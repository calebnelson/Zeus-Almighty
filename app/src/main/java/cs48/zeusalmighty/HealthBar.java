package cs48.zeusalmighty;

import android.graphics.*;

/**
 *  HealthBar is a class to implement a green bar on the top of the screen that depletes as a user takes hits from enemies.
 * @author Group 9
 */

public class HealthBar {
    
    // instance variable for bar's color and current health status
    private GameView gameView;
    private Paint healthColor;
    private int x, y;
    public double currentHealth;

    /**
     * Constructor: sets x and y coordinates for health bar to appear at and initializes health to be full
     * @param gameView          GameView object that current game is taking place in 
     */
    public HealthBar(GameView gameView) {
        this.healthColor = new Paint();
        healthColor.setColor(Color.GREEN);
        this.gameView = gameView;
        this.x = (int)(gameView.getWidth()*.25);
        this.y = 25;
        this.currentHealth = 1.0;
    }

    /**
     * Method to subtract from a player's health whenever a hit is taken from an enemy
     */
    public void takeHit() {
        currentHealth = currentHealth - 0.1;
    }

    /**
     * onDraw method to draw health bar at the top of the screen
     * @param canvas            canvas to be drawn on
     */
    public void onDraw(Canvas canvas) {
        if(currentHealth > 0) {
            canvas.drawRect(x, y, x + (int) ((gameView.getWidth() * 0.5) * currentHealth), (int) (y + gameView.getHeight() * .05), healthColor);
        }

    }

}
