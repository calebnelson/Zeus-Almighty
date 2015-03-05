package cs48.zeusalmighty;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Color;

public class HealthBar {

    private int health = 10;
    private Bitmap bmp;
    private GameView gameView;
    private Paint healthColor;
    private int height, width, x, y;
    public double currentHealth;

    public HealthBar(GameView gameView, Bitmap bmp) {
        this.healthColor = new Paint();
        healthColor.setColor(Color.GREEN);
        this.gameView = gameView;
        this.x = (int)(gameView.getWidth()*.25);
        this.y = 25;
        this.currentHealth = 1.0;

    }

    public void takeHit() {
        currentHealth = currentHealth - 0.1;
    }

    public void onDraw(Canvas canvas) {

        //Rect dst = new Rect(x, y, x + bmp.getWidth(),(int)(y+gameView.getHeight()*.10));
        //canvas.drawBitmap(bmp, src, dst, null);
        if(currentHealth > 0) {
            canvas.drawRect(x, y, x + (int) ((gameView.getWidth() * 0.5) * currentHealth), (int) (y + gameView.getHeight() * .05), healthColor);
        }

    }

    public Bitmap getResizedBitmap(Bitmap bm, int viewheight, int viewwidth) {
        int height = (int)(viewheight*.3);
        int width = (int)(viewwidth*.5);




        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, width, height, false);

        return resizedBitmap;
    }

}
