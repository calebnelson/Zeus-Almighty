package cs48.zeusalmighty;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private GameThread gameLoopThread;
    private List<Peasant> peasants = new ArrayList<Peasant>();
    private List<Knight> knights = new ArrayList<Knight>();
    private Lightning lightning;
    private Lightning lightning2;
    private Lightning lightning3;
    private long lastClick;
    private Background background;
    private Main activity;
    private GameViewControllerKnights knightscontroller;
    private GameViewControllerPeasants peasantscontroller;
    private HealthBar healthBar;
    private Paint paint;
    private int highScore, currentScore;
    private SharedPreferences preferences;
    public double difficultyDecider;
    public double timeTracker = 15000;
    private Bitmap boom;
    private Bitmap boom2;
    public double startTime = System.currentTimeMillis();
    public double systemTime = System.currentTimeMillis();
    private int lightningChanger = 0;
    public Rect src;
    public Rect dst;
    public int chooseBoom1=0;
    public int chooseBoom2=0;
    public int removed = 0;

    public GameView(Context context) {
        super(context);
        activity = (Main) context;
        knightscontroller = new GameViewControllerKnights(this, activity);
        peasantscontroller = new GameViewControllerPeasants(this, activity);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30);


        highScore = activity.getHighScore();
        preferences = activity.getSharedPreferences("Options", Context.MODE_PRIVATE);
        difficultyDecider = preferences.getFloat("Difficulty", .98f);
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
                createBoom();
                createBoom2();
                createLightning();
                createLightning2();
                createLightning3();
                createHealthBar();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }
        });

    }//End of GameView Constructor





    /*----------------------Create all the Images for Background and Effects-----------------------*/

    //Create the background image with the cloud over it
    private void createBackground() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.clowd3);
        background = new Background(this, bg, cloud);
    }

    //create multicolored explosion that replaces character when destroyed
    private void createBoom() {
        boom = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
        boom = Bitmap.createScaledBitmap(boom, (int)(this.getHeight()*.2),(int)(this.getHeight()*.2),true);
    }

    //create electric-shock looking explosion used on Knights only
    private void createBoom2() {
        boom2 = BitmapFactory.decodeResource(getResources(), R.drawable.boom2);
        boom2 = Bitmap.createScaledBitmap(boom2, (int)(this.getHeight()*.1),(int)(this.getHeight()*.1),true);
    }

    //>>>>>>There are 3 types of lightning that all must be declared to create illusion<<<<<<<<<<<
    private void createLightning() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.lightning1);
        lightning = new Lightning(this, bg);
    }
    private void createLightning2() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.lightning2);
        lightning2 = new Lightning(this, bg);
    }
    private void createLightning3() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.lightning3);
        lightning3 = new Lightning(this, bg);
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End of Lightning declarations<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //Create Health bar on top of screen
    private void createHealthBar() {
        healthBar = new HealthBar(this);
    }

    /*-----------------------End of Background and Effects Declarations------------------------*/







    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);
        background.onDraw(canvas);
        healthBar.onDraw(canvas);

        if(dst != null) {
            if(chooseBoom2 == 1) {
                canvas.drawBitmap(boom, src, dst, null);
                chooseBoom2 = 0;
            }
            if(chooseBoom1 == 1) {
                canvas.drawBitmap(boom2, src, dst, null);
                chooseBoom1 = 0;
            }
        }
        if (healthBar.currentHealth <= 0.0) {
            Paint paint1 = new Paint();
            paint1.setColor(Color.RED);
            paint1.setTextSize(100);
            canvas.drawText("GAME OVER", this.getWidth()/2-250 ,this.getHeight()/2 , paint1);
            activity.saveScore();


        }
        else {
            if(lightningChanger % 3 ==0) {
                if (lightning.isVisible())
                    lightning.onDraw(canvas);
                lightningChanger+=1;
            }
            else if(lightningChanger % 3 ==1) {
                if(lightning2.isVisible()) {
                    lightning2.onDraw(canvas);
                    lightningChanger+=1;
                }
            }
            else if(lightningChanger % 3 ==2) {
                if(lightning3.isVisible()) {
                    lightning3.onDraw(canvas);
                    lightningChanger+=1;
                }
            }
        }
        currentScore = activity.getCurrentScore();
        highScore = activity.getHighScore();
        synchronized (getHolder()) {

            peasantscontroller.addPeasant(peasants, healthBar);
            knightscontroller.addKnight(knights);
            peasantscontroller.drawPeasant(peasants, healthBar, canvas);
            knightscontroller.drawKnight(knights, healthBar, canvas);

        }
        canvas.drawText("Current Health", this.getWidth()/2 - 85 , 30, paint);
        canvas.drawText("Current Score:" + String.valueOf(currentScore), 25, 50, paint);
        canvas.drawText("High Score:"+String.valueOf(highScore), this.getWidth()-275, 50, paint);
        dst = null;
        src=null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        removed = 0;
        if(healthBar.currentHealth > 0.0) {
            if (System.currentTimeMillis() - lastClick > 300) {
                lastClick = System.currentTimeMillis();
                synchronized (getHolder()) {
                    if (event.getY() < (this.getHeight() * 0.25)) {
                        lightning.setX(event.getX()-(lightning.width())/2);
                        lightning.setVisible(150, activity);
                        lightning2.setX(event.getX()-(lightning2.width())/2);
                        lightning2.setVisible(150, activity);
                        lightning3.setX(event.getX()-(lightning3.width())/2);
                        lightning3.setVisible(150, activity);


                        knightscontroller.knightHit(knights, event.getX(), event.getY(), boom, boom2);
                        peasantscontroller.peasantHit(peasants, event.getX(), event.getY(), boom);

                    }
                }
            }
        }
        return true;
    }
}