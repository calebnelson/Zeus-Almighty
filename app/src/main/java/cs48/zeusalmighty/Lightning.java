package cs48.zeusalmighty;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class Lightning {

    private GameView gameView;
    private Bitmap bmp;
    private boolean visible;
    private float x, y;
    private MediaPlayer player;

    public Lightning(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = getResizedBitmap(bmp, 600, 90);
        x = 0;
        y = 300;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(int duration, Main main) {
        visible = true;
        player = MediaPlayer.create(main, R.raw.woosh);
        player.seekTo(0);
        player.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run(){
                visible = false;
            }
        }, duration);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void onDraw(Canvas canvas) {
        Rect src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        Rect dst = new Rect((int)x, (int)y, (int)x + bmp.getWidth(), (int)y + bmp.getHeight());
        canvas.drawBitmap(bmp, src, dst, null);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

}
