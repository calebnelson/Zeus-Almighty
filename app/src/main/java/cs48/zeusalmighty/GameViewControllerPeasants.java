package cs48.zeusalmighty;

/**
 * Created by Po on 3/5/15.
 */


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


public class GameViewControllerPeasants {

    private Main activity;
    private GameView gameView;


    public GameViewControllerPeasants(GameView gameView, Main activity) {
        this.activity = activity;
        this.gameView = gameView;
    }


    private Peasant createPeasant(int bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap assets = BitmapFactory.decodeResource(gameView.getResources(), bitmap);

        assets = Bitmap.createScaledBitmap(assets, (int) (gameView.getHeight() * .20), (int) (gameView.getHeight() * .20), true);

        return new Peasant(gameView, assets);
    }


    public void addPeasant(List<Peasant> peasant, HealthBar healthBar) {
        Random random = new Random();
        float temp = random.nextFloat();
        gameView.systemTime = System.currentTimeMillis();
        if (peasant.isEmpty()) {
            peasant.add(createPeasant(R.drawable.test1));
        } else if (temp > gameView.difficultyDecider) {
            peasant.add(createPeasant(R.drawable.test1));
        }
        if (healthBar.currentHealth > 0) {
            if ((gameView.systemTime - gameView.startTime) / gameView.timeTracker > 1) {
                //the longer they survive, the harder it gets
                //the time tracker is determine the next time where the difficulty will increase
                gameView.timeTracker = gameView.timeTracker * 1.4;
                gameView.difficultyDecider = gameView.difficultyDecider - .01;
            } else if ((gameView.systemTime - gameView.startTime) / gameView.timeTracker < 0) {
                //if it is midnight and goes to 12:01 this would be negative
                //so we need to add on the midnight time in millis
                gameView.systemTime = gameView.systemTime + 86400000;
                if ((gameView.systemTime - gameView.startTime) % gameView.timeTracker > 1) {
                    //the longer they survive, the harder it gets
                    //the time tracker is determine the next time where the difficulty will increase
                    gameView.timeTracker = gameView.timeTracker * 1.4;
                    gameView.difficultyDecider = gameView.difficultyDecider - .01;
                }

            }
        }
    }

    public void drawPeasant(List<Peasant> peasants, HealthBar healthBar, Canvas canvas) {
        Iterator<Peasant> it = peasants.iterator();
        while (it.hasNext()) {
            Peasant peasant = it.next();
            if (peasant.getX() <= 0) {
                //if peasant is off screen then don't render anymore
                healthBar.takeHit();
                it.remove();
                peasant.bmp.recycle();
                peasant.bmp = null;
                peasant = null;
            } else if (peasant.getX() > 0) {
                peasant.onDraw(canvas);
            }
        }
    }




    public void peasantHit(List<Peasant> peasants, float x, float y, Bitmap boom) {


        Iterator<Peasant> it = peasants.iterator();
        while (it.hasNext()) {
            Peasant peasant = it.next();
            if (peasant.isCollision(x, y)) {
                if (gameView.removed == 0) {
                    int xcoord = peasant.getX();

                    gameView.dst = new Rect(xcoord - (int) (boom.getWidth() / 2), (int) (gameView.getHeight() * 0.85), xcoord + boom.getWidth() - (int) (boom.getWidth() / 2), (int) (gameView.getHeight() * 0.85) + boom.getHeight());
                    gameView.src = new Rect(0, (int) (boom.getHeight() * .3), boom.getWidth(), (int) (boom.getHeight() * .7));

                    activity.increaseScore();
                    it.remove();
                    peasant.bmp.recycle();
                    peasant.bmp = null;
                    peasant = null;

                    gameView.chooseBoom2 = 1;
                    break;
                }
                gameView.removed = 1;
            }
        }


    }




}
