package cs48.zeusalmighty;


import android.graphics.*;
import java.io.*;

/**
 * Background is a class to hold the background image and the cloud image so that they can easily be drawn at the same time 
 * with a single call to Background's onDraw() method. 
 * 
 * @author Group 9
 */

public class Background {

    // instance variables
    private GameView gameView;
    private Bitmap bg;
    private Bitmap cloud;

    /**
     * Constructor to make basic background object
     * @param gameView      the gameView instance that is currently running
     * @param bg            a bitmap of the background image
     * @param cloud         a bitmap of the cloud image
     */
    public Background(GameView gameView, Bitmap bg, Bitmap cloud) {
        this.gameView = gameView;
        this.bg = getResizedBg(bg, gameView.getHeight(), gameView.getWidth());
        this.cloud = getResizedCloud(cloud, gameView.getHeight(), gameView.getWidth());
    }

    /**
     * onDraw method to draw the background and cloud images resized properly to the screen size
     * @param canvas        the canvas to be drawn on
     */
    public void onDraw(Canvas canvas) {
        Rect src = new Rect(0, 0, bg.getWidth(), bg.getHeight());
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bg, src, dst, null);
        Rect src2 = new Rect(0, 0, cloud.getWidth(), cloud.getHeight());
        Rect dst2 = new Rect(0, 0, cloud.getWidth(), cloud.getHeight());
        canvas.drawBitmap(cloud, src2, dst2, null);
    }

    /**
     * Returns a resized bitmap of the background image that is the same size as the screen being played on
     * @param bm                background bitmap to resize
     * @param viewheight        height to resize background to
     * @param viewwidth         width to resize background to
     */
    public Bitmap getResizedBg(Bitmap bm, int viewheight, int viewwidth) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap (bm, viewwidth, viewheight, false);
        return resizedBitmap;
    }

    /**
     * Returns a resized bitmap of the cloud image that takes up 1/4 of the height of the screen 
     * @param bm                cloud bitmap to resize
     * @param viewheight        height to resize cloud to
     * @param viewwidth         width to resize cloud to
     */
    public Bitmap getResizedCloud(Bitmap bm, int viewheight, int viewwidth) {
        int height = (int)(viewheight*.25);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap (bm, viewwidth, height, false);
        return resizedBitmap;
    }

}

