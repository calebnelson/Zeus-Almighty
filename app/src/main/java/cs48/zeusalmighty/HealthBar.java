package cs48.zeusalmighty;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Color;

public class HealthBar {
    private GameView gameView;
    private Paint healthColor;
    private int x, y;
    public double currentHealth;

    public HealthBar(GameView gameView) {
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
        if(currentHealth > 0) {
            canvas.drawRect(x, y, x + (int) ((gameView.getWidth() * 0.5) * currentHealth), (int) (y + gameView.getHeight() * .05), healthColor);
        }

    }

}
