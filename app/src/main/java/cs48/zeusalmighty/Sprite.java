package cs48.zeusalmighty;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;


public class Sprite {
    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 7;
    private static final int MAX_SPEED = 5;
    private GameView gameView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int xSpeed;
    private int ySpeed;
    private int currentFrame = 0;
    private int width;
    private int height;

    public Sprite(GameView gameView, Bitmap bmp) {
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.gameView = gameView;
        this.bmp = bmp;

        x = 0;
        y = gameView.getHeight() - height*3 + 25;
        xSpeed = (int) (Math.random() * 25);
        ySpeed = 0;
    }

    private void update() {
        if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (currentFrame == 26)
            currentFrame = 0;
        else
            currentFrame = ++currentFrame;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = (currentFrame % BMP_COLUMNS) * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }

    private int getAnimationRow() {
        if (currentFrame < 7)
                return 0;
        else if (currentFrame < 14)
                return 1;
        else if (currentFrame < 21)
            return 2;
        else
            return 3;
    }

    public boolean isCollition(float x2, float y2) {
        return x2 > (x - 100) && x2 < (x + 100 + width) && y2 < 300;
    }
}
