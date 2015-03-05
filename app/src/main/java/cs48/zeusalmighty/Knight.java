package cs48.zeusalmighty;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.Random;


public class Knight {
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


    public Knight(GameView gameView, Bitmap bmp) {
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
            xSpeed = -5;
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
        dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
        src = null;
        dst = null;
    }



    public boolean isCollision(float x2, float y2) {
        return x2 > (x-10) && x2 < (x + width+10) && y2 < 300;
    }



}

