package cs48.zeusalmighty;

import android.graphics.*;
import java.util.Random;

/**
 * Knight is a class to implement an "elite" enemy, or one that is stronger than the typical peasant enemies.
 * Knights are able to take more hits than peasants, but do not get generated as often.
 * @author Group 9 
 */

public class Knight {
    
    // instance variables
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private GameView gameView;
    public Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed = 5;
    private int currentFrame = 0;
    private int width;
    private int height;
    public Rect dst;
    public int hitsTaken = 0;

    /**
    * Constructor: gets width and height of sprites based on size of sprite sheet.
    * @param gameView       GameView object knights will be present in
    * @param bmp            bitmap of sprite sheet to use for knights
    */
    public Knight(GameView gameView, Bitmap bmp) {
        this.x = gameView.getWidth();
        this.y = (int)(gameView.getHeight()*0.9);
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
    }

    /**
    * Return x coordinate of knight enemy
    */
    public int getX() {
        return this.x;
    }

    /**
    * Called each time onDraw is called to update the x coordinate of the knight
    */
    private void update() {
        if (x > gameView.getWidth() - width - xSpeed) {
            xSpeed = -5;
        }

        x = x + xSpeed;
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    /**
    * onDraw method that selects appropriate region of sprite sheet to draw and draws it on the canvas based on 
    * the knight's x coordinate instance variable
    * @param canvas         the canvas to be drawn on
    */
    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = 1 * height;
        //source the bitmap that the image comes from
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        //destination is where on the screen the image will be located
        dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
        src = null;
        dst = null;
    }

    /**
    * Method to determine whether a lightning bolt at a given x,y location results in hitting a knight
    * @param x2         x coordinate of lightning bolt
    * @param y2         y coordinate of lightning bolt
    */
    public boolean isCollision(float x2, float y2) {
        return x2 > (x-10) && x2 < (x + width+10) && y2 < 300;
    }

}

