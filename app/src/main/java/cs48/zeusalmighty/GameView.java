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
    private Bitmap bmp;
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
    private Bitmap boom;
    private Bitmap boom2;
    private double startTime = System.currentTimeMillis();
    private double systemTime = System.currentTimeMillis();
    private int lightningChanger = 0;
    private int xcood = 0;
    public Rect src;
    public Rect dst;
    public int chooseBoom1=0;
    public int chooseBoom2=0;

    public GameView(Context context) {
        super(context);
        activity = (Main) context;
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

    }

    private Peasant createPeasant(int bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap assets = BitmapFactory.decodeResource(getResources(), bitmap);

        assets = Bitmap.createScaledBitmap(assets, (int)(this.getHeight()*.20),(int)(this.getHeight()*.20),true);

        return new Peasant(this,assets);
    }
    private Knight createKnight(int bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap assets = BitmapFactory.decodeResource(getResources(), bitmap);

        assets = Bitmap.createScaledBitmap(assets, (int)(this.getHeight()*.20),(int)(this.getHeight()*.20),true);

        return new Knight(this,assets);
    }



    private void createBackground() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap cloud = BitmapFactory.decodeResource(getResources(), R.drawable.clowd3);
        background = new Background(this, bg, cloud);
    }

    private void createBoom() {
        boom = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
        boom = Bitmap.createScaledBitmap(boom, (int)(this.getHeight()*.2),(int)(this.getHeight()*.2),true);
    }

    private void createBoom2() {
        boom2 = BitmapFactory.decodeResource(getResources(), R.drawable.boom2);
        boom2 = Bitmap.createScaledBitmap(boom2, (int)(this.getHeight()*.1),(int)(this.getHeight()*.1),true);
    }

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
        if(healthBar.currentHealth > 0 ) {
            if ((systemTime - startTime) / timeTracker > 1) {
                //the longer they survive, the harder it gets
                //the time tracker is determine the next time where the difficulty will increase
                timeTracker = timeTracker * 1.2;
                difficultyDecider = difficultyDecider - .01;
            } else if ((systemTime - startTime) / timeTracker < 0) {
                //if it is midnight and goes to 12:01 this would be negative
                //so we need to add on the midnight time in millis
                systemTime = systemTime + 86400000;
                if ((systemTime - startTime) % timeTracker > 1) {
                    //the longer they survive, the harder it gets
                    //the time tracker is determine the next time where the difficulty will increase
                    timeTracker = timeTracker * 1.2;
                    difficultyDecider = difficultyDecider - .01;
                }

            }
        }
    }

    public void addKnight(List<Knight> knight) {
        Random random = new Random();
        float temp = random.nextFloat();
        systemTime = System.currentTimeMillis();
        /*if(knight.isEmpty()) {
            knight.add(createPeasant(R.drawable.test1));
            spritesAdded++;
        }*/

        if(temp > (difficultyDecider)+0.04) {
            knight.add(createKnight(R.drawable.test2));
            spritesAdded++;
        }

    }

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
            addPeasant(peasants);
            addKnight(knights);
            Iterator<Peasant> it = peasants.iterator();
            while (it.hasNext()) {
                Peasant peasant = it.next();
                if (peasant.getX() <= 0) {
                    //if peasant is off screen then don't render anymore
                    spritesDeleted++;
                    spritesThatMadeIt++;
                    healthBar.takeHit();
                    it.remove();
                    peasant.bmp.recycle();
                    peasant.bmp = null;
                    peasant = null;
                    //break;
                } else if (peasant.getX() > 0) {
                    peasant.onDraw(canvas);
                }
            }
            Iterator<Knight> it1 = knights.iterator();
            while (it1.hasNext()) {
                Knight knight = it1.next();
                if (knight.getX() <= 0) {
                    //if peasant is off screen then don't render anymore
                    spritesDeleted++;
                    spritesThatMadeIt++;
                    healthBar.takeHit();
                    healthBar.takeHit();
                    it1.remove();
                    knight.bmp.recycle();
                    knight.bmp = null;
                    knight = null;
                    //break;
                } else if (knight.getX() > 0) {
                    if(knight.bmp != null) {
                        knight.onDraw(canvas);
                    }
                }
            }
        }
        canvas.drawText("Current Health", this.getWidth()/2 - 85 , 30, paint);
        canvas.drawText("Current Score:" + String.valueOf(currentScore), 25, 50, paint);
        canvas.drawText("High Score:"+String.valueOf(highScore), this.getWidth()-275, 50, paint);
        dst = null;
        src=null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int removed = 0;
        if(healthBar.currentHealth > 0.0) {
            if (System.currentTimeMillis() - lastClick > 500) {
                lastClick = System.currentTimeMillis();
                synchronized (getHolder()) {
                    if (event.getX() > 1800)
                        activity.saveScore();
                    if (event.getY() < (this.getHeight() * 0.25)) {
                        lightning.setX(event.getX()-(lightning.width())/2);
                        lightning.setVisible(150, activity);
                        lightning2.setX(event.getX()-(lightning2.width())/2);
                        lightning2.setVisible(150, activity);
                        lightning3.setX(event.getX()-(lightning3.width())/2);
                        lightning3.setVisible(150, activity);



                        Iterator<Knight> it1 = knights.iterator();
                        while (it1.hasNext()) {
                            Knight knight = it1.next();
                            if (knight.isCollision(event.getX(), event.getY())) {
                                if(knight.hitsTaken < 5 && removed == 0) {
                                    knight.hitsTaken += 1;
                                    int xcoord = knight.getX();
                                    dst = new Rect(xcoord-(int)(boom2.getWidth())/3, (int)(this.getHeight()*0.87), xcoord + boom2.getWidth()-(int)(boom2.getWidth())/3, (int)(this.getHeight()*0.87)+ boom2.getHeight());
                                    src = new Rect(0, 0, boom2.getWidth(), (int)(boom2.getHeight()));
                                    chooseBoom1 = 1;
                                    removed = 1;
                                }
                                else if(knight.hitsTaken >= 5) {
                                    if(removed == 0 && chooseBoom1 == 0) {

                                        int xcoord = knight.getX();

                                        dst = new Rect(xcoord - (int) (boom.getWidth() / 2), (int) (this.getHeight() * 0.85), xcoord + boom.getWidth() - (int) (boom.getWidth() / 2), (int) (this.getHeight() * 0.85) + boom.getHeight());
                                        src = new Rect(0, (int) (boom.getHeight() * .3), boom.getWidth(), (int) (boom.getHeight() * .7));

                                        activity.increaseScore();
                                        spritesDeleted++;
                                        it1.remove();


                                        knight.bmp.recycle();
                                        knight.bmp = null;
                                        knight = null;
                                        chooseBoom2 = 1;
                                        break;
                                    }
                                    removed = 1;
                                }
                            }
                        }
                    Iterator<Peasant> it = peasants.iterator();
                    while (it.hasNext()) {
                        Peasant peasant = it.next();
                        if (peasant.isCollision(event.getX(), event.getY())) {
                            if(removed == 0) {
                                int xcoord = peasant.getX();

                                dst = new Rect(xcoord - (int) (boom.getWidth() / 2), (int) (this.getHeight() * 0.85), xcoord + boom.getWidth() - (int) (boom.getWidth() / 2), (int) (this.getHeight() * 0.85) + boom.getHeight());
                                src = new Rect(0, (int) (boom.getHeight() * .3), boom.getWidth(), (int) (boom.getHeight() * .7));

                                activity.increaseScore();
                                spritesDeleted++;
                                it.remove();
                                peasant.bmp.recycle();
                                peasant.bmp = null;
                                peasant = null;

                                chooseBoom2 = 1;
                                break;
                            }
                            removed = 1;
                        }
                    }

                    }
                }
            }
        }
        return true;
    }
}