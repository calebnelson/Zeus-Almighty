package cs48.zeusalmighty;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.Random;


public class Peasant {
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private static final int MAX_SPEED = 5;
    private GameView gameView;
    public Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed = 15;
    private int ySpeed;
    private int currentFrame = 0;
    private int width;
    private int height;

    public Peasant(GameView gameView, Bitmap bmp) {
        this.x = gameView.getWidth();
        this.y = (int)(gameView.getHeight()*0.9);
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
    }

    public int getX() {
        return this.x;
    }

    private void update() {
        if (x > gameView.getWidth() - width - xSpeed) {
            xSpeed = -15;
        }

        x = x + xSpeed;
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = currentFrame * width;
        int srcY = 1 * height;
        //source the bitmap that the image comes from
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        //destination is where on the screen the image will be located
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
        src = null;
        dst = null;
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

    public boolean isCollition(float x2, float y2) {
        return x2 > (x - 25) && x2 < (x + 25 + width) && y2 < 300;
    }
}
