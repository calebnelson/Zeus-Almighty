package cs48.zeusalmighty;

import android.content.*;
import android.graphics.*;
import android.view.*;
import java.util.*;

/**
 * Controller class for the peasant enemies present in the game. This class is responsible for drawing, creating, and
 * destroying peasant enemies during the course of gameplay.
 * @author Group 9
 */

public class GameViewControllerPeasants {

    // instance variables
    private Main activity;
    private GameView gameView;

    /**
    * Default constructor
    * @param gameView       GameView object currently running game
    * @param activity       Main activity running game
    */
    public GameViewControllerPeasants(GameView gameView, Main activity) {
        this.activity = activity;
        this.gameView = gameView;
    }

    /**
    * Creates a peasant enemy from a given bitmap to be added to the list of peasants in the game
    * @param bitmap         bitmap used to draw peasant enemy
    */
    private Peasant createPeasant(int bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap assets = BitmapFactory.decodeResource(gameView.getResources(), bitmap);

        assets = Bitmap.createScaledBitmap(assets, (int) (gameView.getHeight() * .20), (int) (gameView.getHeight() * .20), true);

        return new Peasant(gameView, assets);
    }

    /**
    * Adds a new peasant enemy to the list of peasants in the GameView. Checks if the player has health before adding a new
    * peasant to the list.
    * @param peasant        arrayList of peasant to be added to 
    * @param healthBar      health bar from game to check to make sure player is still alive
    */
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

    /**
    * method to call from onDraw method of GameView. iterates over the list of peasants in the game and draws each one that 
    * has not yet reached the edge of the screen
    * @param peasants       list of peasants currently in the game
    * @param healthBar      health bar object from game
    * @param canvas         canvas to be drawn on
    */
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

    /**
    * Method to determine if any peasants were hit based on a given lightning bolt's location
    * @param peasants       list of peasant enemies in the game currently
    * @param x              x coordinate of lightning bolt
    * @param y              y coordinate of lightning bolt
    * @param boom           bitmap of explosion graphic to display after a kill
    */
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
