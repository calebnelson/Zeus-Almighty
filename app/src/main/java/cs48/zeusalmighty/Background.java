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
        this.bg = getResizedBitmap(bg, 1080, 1920);
        this.cloud = getResizedBitmap(cloud, 300, 1880);
    }

    public void onDraw(Canvas canvas) {
        Rect src = new Rect(0, 0, bg.getWidth(), bg.getHeight());
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bg, src, dst, null);
        Rect src2 = new Rect(0, 0, cloud.getWidth(), cloud.getHeight());
        Rect dst2 = new Rect(20, 10, cloud.getWidth() + 20, cloud.getHeight() + 10);
        canvas.drawBitmap(cloud, src2, dst2, null);
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

