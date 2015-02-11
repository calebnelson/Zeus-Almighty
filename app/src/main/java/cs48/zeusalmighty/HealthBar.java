package cs48.zeusalmighty;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class HealthBar {

    private int health = 10;
    private Bitmap bmp;
    private GameView gameView;
    private int height, width, x, y, currentFrame;

    public HealthBar(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.bmp = getResizedBitmap(bmp, 500, 920);
        this.x = 500;
        this.y = 10;
        currentFrame = 0;
        this.height = bmp.getHeight() / 10;
        this.width = bmp.getWidth() / 10;
    }

    public boolean takeHit() {
        currentFrame++;
        if (health == 1)
            return false;
        else {
            health--;
            return true;
        }
    }

    public void onDraw(Canvas canvas) {
        int srcX = 0;
        int srcY = currentFrame * height;
        Rect src = new Rect(srcX, srcY, width, srcY + height);
        Rect dst = new Rect(x, y, x + health*width, y + height);
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

}
