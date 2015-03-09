package cs48.zeusalmighty;

import android.graphics.*;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.View;
import java.io.*;
import java.util.*;

/**
 * Lightning is a class used to implement the graphical effect of different lightning bolts coming down from the
 * cloud when a user taps the screen. Lightning objects have an important boolean named "visible" that determines
 * whether they will get drawn on the screen at a given time.
 * @author Group 9
 */

public class Lightning {

    // instance variables
    private GameView gameView;
    private Bitmap bmp;
    private boolean visible;
    private float x, y;
    private MediaPlayer player;
    int counter = 0;

    /**
    * Constructor to create bitmap for lightning bolt and set the location to be the top 1/4 of the screen
    * @param gameView       GameView object in which the lightning will be drawn
    * @param bmp            bitmap to use to display lightning bolt
    */
    public Lightning(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = getResizedBitmap(bmp, gameView.getHeight(), bmp.getWidth());
        x = 0;
        y = (int)(gameView.getHeight()*0.25);
    }

    /**
    * Returns a boolean indicating if the lightning bolt should be drawn at this time or not
    */
    public boolean isVisible() {
        return visible;
    }

    /**
    * Sets the visible instance variable to "true" for a given duration. This makes the lightning bolt get drawn for 
    * the amount of milliseconds specified by duration parameter.
    * @param duration       time in milliseconds to display lightning bolt
    * @param main           main activity running game
    */
    public void setVisible(int duration, Main main) {
        // only create mediaplayer if one hasn't been created yet
        if(counter == 0) {
            player = MediaPlayer.create(main, R.raw.thunderclap);
            counter++;
        }

        visible = true;

        player.seekTo(0);
        player.start();

        // schedule task to make lightning invisible again after a certain duration, given as a function parameter
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run(){
                visible = false;
            }
        }, duration);
        
    }
    
    /**
    * Return width of lightning bolt's bitmap
    */
    public int width() {
        return bmp.getWidth();
    }

    /**
    * Sets x coordinate to center lightning bolt over
    */
    public void setX(float x) {
        this.x = x;
    }

    /**
    * onDraw method to draw lightning bolt, will only be called by GameView if "visible" boolean is set to true
    * @param canvas         the canvas to be drawn on
    */
    public void onDraw(Canvas canvas) {
        Rect src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        Rect dst = new Rect((int)x, (int)y, (int)x + bmp.getWidth(), (int)y + bmp.getHeight());
        canvas.drawBitmap(bmp, src, dst, null);

    }

    /**
    * Returns a bitmap that is the proper size for the screen being player on
    */
    public Bitmap getResizedBitmap(Bitmap bm, int viewheight, int bmpwidth) {

        int height = (int)(viewheight*.7);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, bmpwidth * 2, height, false);
        return resizedBitmap;
    }

}
