package cs48.zeusalmighty;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
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
    private HealthBar healthBar;
    private Paint paint;
    private int highScore, currentScore;
    private int spritesAdded = 0;
    private int spritesDeleted = 0;
    private int spritesThatMadeIt = 0;
    private int positionCounter = 1000;
    //the lower the number, the harder it is. must be between 0 and 1
    private SharedPreferences preferences;
    public double difficultyDecider;
    private double timeTracker = 15000;
    private int speed = 20;
    private double startTime = System.currentTimeMillis();
    private double systemTime = System.currentTimeMillis();


    public GameView(Context context) {
        super(context);
        activity = (Main) context;
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30);


        highScore = activity.getHighScore();
        preferences = activity.getSharedPreferences("Options", Context.MODE_PRIVATE);
        difficultyDecider = preferences.getFloat("Difficulty", .97f);
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
                createHealthBar();
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
        Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.clowd3);
        background = new Background(this, bg, cloud);
    }

    private void createLightning() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.lightningbmp);
        lightning = new Lightning(this, bg);
    }

    private void createHealthBar() {
        Bitmap hb = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar);
        healthBar = new HealthBar(this, hb);
    }

    public void addPeasant(List<Peasant> peasant) {
        Random random = new Random();
        float temp = random.nextFloat();
        systemTime = System.currentTimeMillis();
        if(peasant.isEmpty()) {
            peasant.add(createPeasant(R.drawable.test1));
            spritesAdded++;
        }

        else if(temp > difficultyDecider) {
            peasant.add(createPeasant(R.drawable.test1));
            spritesAdded++;
        }
        if(spritesThatMadeIt <= 10) {
            if ((systemTime - startTime) / timeTracker > 1) {
                //the longer they survive, the harder it gets
                //the time tracker is determine the next time where the difficulty will increase
                timeTracker = timeTracker * 1.3;
                difficultyDecider = difficultyDecider - .01;
            } else if ((systemTime - startTime) / timeTracker < 0) {
                //if it is midnight and goes to 12:01 this would be negative
                //so we need to add on the midnight time in millis
                systemTime = systemTime + 86400000;
                if ((systemTime - startTime) % timeTracker > 1) {
                    //the longer they survive, the harder it gets
                    //the time tracker is determine the next time where the difficulty will increase
                    timeTracker = timeTracker * 1.3;
                    difficultyDecider = difficultyDecider - .01;
                }

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        background.onDraw(canvas);
        //healthBar.onDraw(canvas);
        if (spritesThatMadeIt >= 10) {
            Paint paint1 = new Paint();
            paint1.setColor(Color.RED);
            paint1.setTextSize(100);
            canvas.drawText("GAME OVER", this.getWidth()/2-250 ,this.getHeight()/2 , paint1);

        }
        else {
            if (lightning.isVisible())
                lightning.onDraw(canvas);
        }
        currentScore = activity.getCurrentScore();
        highScore = activity.getHighScore();
        synchronized (getHolder()) {
            addPeasant(peasants);
            Iterator<Peasant> it = peasants.iterator();
            while (it.hasNext()) {
                Peasant peasant = it.next();
                if (peasant.getX() <= 0) {
                    //if peasant is off screen then don't render anymore
                    spritesDeleted++;
                    spritesThatMadeIt++;
                    //if (healthBar.takeHit() == false) {
                    //    activity.saveScore();
                    //    activity.finish();
                    //}
                    it.remove();
                    peasant.bmp.recycle();
                    peasant.bmp = null;
                    peasant = null;
                    //break;
                } else if (peasant.getX() > 0) {
                    peasant.onDraw(canvas);
                }
            }
        }
        canvas.drawText("Current Score:" + String.valueOf(currentScore), 25, 30, paint);
        canvas.drawText("High Score:"+String.valueOf(highScore), this.getWidth()-275, 30, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(spritesThatMadeIt <= 10) {
            if (System.currentTimeMillis() - lastClick > 500) {
                lastClick = System.currentTimeMillis();
                synchronized (getHolder()) {
                    if (event.getX() > 1800)
                        activity.saveScore();
                    if (event.getY() < (this.getHeight() * 0.25)) {
                        lightning.setX(event.getX() - 45);
                        lightning.setVisible(150, activity);
                    }
                    Iterator<Peasant> it = peasants.iterator();
                    while (it.hasNext()) {
                        Peasant peasant = it.next();
                        if (peasant.isCollition(event.getX(), event.getY())) {
                            activity.increaseScore();
                            spritesDeleted++;
                            it.remove();
                            peasant.bmp.recycle();
                            peasant.bmp = null;
                            peasant = null;
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }
}