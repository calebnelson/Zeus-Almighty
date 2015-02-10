package cs48.zeusalmighty;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;


public class GameView extends SurfaceView {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameThread gameLoopThread;
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private Lightning lightning;
    private long lastClick;
    private Background background;
    private Main activity;
    private Paint paint;
    private int highScore, currentScore;

    public GameView(Context context) {
        super(context);
        activity = (Main) context;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        highScore = activity.getHighScore();
        gameLoopThread = new GameThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createBackground();
                createLightning();
                createSprites();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

    }

    private void createSprites() {
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
        sprites.add(createSprite(R.drawable.dude_animation_sheet));
    }

    private Sprite createSprite(int resource) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(this, bmp);
    }


    private void createBackground() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.newcloud);
        background = new Background(this, bg, cloud);
    }

    private void createLightning() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.lightningbmp);
        lightning = new Lightning(this, bg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        background.onDraw(canvas);
        if (lightning.isVisible())
            lightning.onDraw(canvas);
        for (Sprite sprite : sprites) {
            sprite.onDraw(canvas);
        }
        currentScore = activity.getCurrentScore();
        highScore = activity.getHighScore();
        canvas.drawText(String.valueOf(currentScore)+"\nHigh: "+String.valueOf(highScore), 25, 90, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 1000) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                if (event.getX() > 1800)
                    activity.saveScore();
                if (event.getY() < 300) {
                    lightning.setX(event.getX() - 60);
                    lightning.setVisible(500, activity);
                }
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollition(event.getX(), event.getY())) {
                        sprites.remove(sprite);
                        activity.increaseScore();
                        break;
                    }
                }
            }
        }
        return true;
    }
}