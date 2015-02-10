package cs48.zeusalmighty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameView extends SurfaceView {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameThread gameLoopThread;
    private List<Peasant> peasants = new ArrayList<Peasant>();
    private Lightning lightning;
    private long lastClick;
    private Background background;
    private Main activity;
    private Paint paint;
    private int highScore, currentScore;
    private int positionCounter = 1000;
    //the lower the number, the harder it is. must be between 0 and 1
    public double difficultyDecider = 0.95;
    private double timeTracker = 15000;
    private int speed = 20;
    private double startTime = System.currentTimeMillis();
    private double systemTime = System.currentTimeMillis();

    public GameView(Context context) {
        super(context);
        activity = (Main) context;
        paint = new Paint();
        paint.setColor(Color.BLUE);
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
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

    }

    private Peasant createPeasant(int bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap assets = BitmapFactory.decodeResource(getResources(), bitmap, options);
        options.inBitmap = assets;
        assets = Bitmap.createScaledBitmap(assets, 250,250,true);
        return new Peasant(this,assets);
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

    public void addPeasant(List<Peasant> peasant) {
        Random random = new Random();
        float temp = random.nextFloat();
        systemTime = System.currentTimeMillis();
        if(peasant.isEmpty()) {
            peasant.add(createPeasant(R.drawable.good1));
        }

        else if(temp > difficultyDecider) {
            peasant.add(createPeasant(R.drawable.good1));
        }

        if((systemTime - startTime) / timeTracker > 1) {
            //the longer they survive, the harder it gets
            //the time tracker is determine the next time where the difficulty will increase
            timeTracker = timeTracker * 1.3;
            difficultyDecider = difficultyDecider - .01;
        }
        else if((systemTime - startTime) / timeTracker < 0) {
            //if it is midnight and goes to 12:01 this would be negative
            //so we need to add on the midnight time in millis
            systemTime = systemTime + 86400000;
            if((systemTime - startTime) % timeTracker > 1) {
                //the longer they survive, the harder it gets
                //the time tracker is determine the next time where the difficulty will increase
                timeTracker = timeTracker * 1.3;
                difficultyDecider = difficultyDecider - .01;
            }

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        background.onDraw(canvas);
        if (lightning.isVisible())
            lightning.onDraw(canvas);
        addPeasant(peasants);
        synchronized (getHolder()) {
            for (Peasant peasant : peasants) {
                if (peasant.getX() < -20) {
                    //if peasant is off screen then don't render anymore
                    peasants.remove(peasant);
                    peasant.bmp.recycle();
                    peasant.bmp = null;
                    peasant = null;
                } else if (peasant.getX() >= 0) {
                    peasant.onDraw(canvas);
                }
            }
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
                for (Peasant peasant : peasants) {
                    if (peasant.isCollition(event.getX(), event.getY())) {
                        peasants.remove(peasant);
                        peasant.bmp.recycle();
                        peasant.bmp = null;
                        peasant = null;
                        activity.increaseScore();
                        break;
                    }
                }
            }
        }
        return true;
    }
}