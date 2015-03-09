package cs48.zeusalmighty;

import android.content.*;
import android.graphics.*;
import android.view.*;
import java.util.*;

/**
 * GameView is the class that implements the bulk of the game's mechanics. This class keeps track of all the 
 * enemies currently in the game in addition to determining when the player has successfully hit an enemy 
 * with lightning. Additionally, GameView keeps track of hits to a player's health bar and updates it accordingly.
 * @author Group 9
 */

public class GameView extends SurfaceView {

    // Instance variables. This is where the game's enemies are stored, along with the player's health bar object, 
    // their score and difficulty settings, and the classes needed to display lightning bolts and other effects. 
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

    /**
     * Constructor for GameView objects. Initializes controllers for enemy knights and peasants, attempts to
     * load high score and difficulty settings, and defines behavior for when the gameview surface is initialized
     * and destroyed.
     * @param context       the Main object that is initialized when the game starts
     */
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
            /**
             * behavior for when "back" button is pressed or when game is forcefully closed
            */
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
            /**
             * behavior for when gameview is initialized
             */
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
    /**
     * Create the background image with the cloud over it
     */
    private void createBackground() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.clowd3);
        background = new Background(this, bg, cloud);
    }
    
    /**
     * create multicolored explosion that replaces character when destroyed
     */
    private void createBoom() {
        boom = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
        boom = Bitmap.createScaledBitmap(boom, (int)(this.getHeight()*.2),(int)(this.getHeight()*.2),true);
    }

    /**
     * create electric-shock looking explosion used on Knights only
     */
    private void createBoom2() {
        boom2 = BitmapFactory.decodeResource(getResources(), R.drawable.boom2);
        boom2 = Bitmap.createScaledBitmap(boom2, (int)(this.getHeight()*.1),(int)(this.getHeight()*.1),true);
    }

    //>>>>>>There are 3 types of lightning that all must be declared to create illusion<<<<<<<<<<<
    /**
     * creates first kind of lightning
     */
    private void createLightning() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.lightning1);
        lightning = new Lightning(this, bg);
    }
    /**
     * creates second kind of lightning
     */
    private void createLightning2() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.lightning2);
        lightning2 = new Lightning(this, bg);
    }
    /**
     * creates third kind of lightning
     */
    private void createLightning3() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.lightning3);
        lightning3 = new Lightning(this, bg);
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End of Lightning declarations<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    /**
     * Create Health bar on top of screen
     */
    private void createHealthBar() {
        healthBar = new HealthBar(this);
    }

    /*-----------------------End of Background and Effects Declarations------------------------*/







    @Override
    /**
     * onDraw method to draw the game's graphics. Draws the background, health bar, and score information. Also calls 
     * methods that will potentially add a knight or peasant to the game, depending on the difficulty and player's luck.
     * Calls draw method for enemies currently in the game and displays a large red "Game Over" message if a player's
     * health bar runs out.
     * @param canvas        the canvas the game will be drawn on
     */
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
        src = null;
    }

    @Override
    /**
     * Code to handle the event of a player touching the screen. This method determines where the user touched
     * and, based on that location, determines if it is appropriate to display lightning and remove an enemy
     * @param event     motion event corrensponding to the user touching the screen
     */
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
