package cs48.zeusalmighty;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Background {

    private GameView gameView;
    private Bitmap bg;
    private Bitmap cloud;

    public Background(GameView gameView, Bitmap bg, Bitmap cloud) {
        this.gameView = gameView;
        this.bg = getResizedBg(bg, gameView.getHeight(), gameView.getWidth());
        this.cloud = getResizedCloud(cloud, gameView.getHeight(), gameView.getWidth());
    }

    public void onDraw(Canvas canvas) {
        Rect src = new Rect(0, 0, bg.getWidth(), bg.getHeight());
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bg, src, dst, null);
        Rect src2 = new Rect(0, 0, cloud.getWidth(), cloud.getHeight());
        Rect dst2 = new Rect(0, 0, cloud.getWidth(), cloud.getHeight());
        canvas.drawBitmap(cloud, src2, dst2, null);
    }

    public Bitmap getResizedBg(Bitmap bm, int viewheight, int viewwidth) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap (bm, viewwidth, viewheight, false);
        return resizedBitmap;
    }

    public Bitmap getResizedCloud(Bitmap bm, int viewheight, int viewwidth) {
        int height = (int)(viewheight*.25);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap (bm, viewwidth, height, false);
        return resizedBitmap;
    }

}

