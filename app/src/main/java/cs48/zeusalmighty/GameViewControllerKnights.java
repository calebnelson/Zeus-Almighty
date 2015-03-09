package cs48.zeusalmighty;

import android.content.*;
import android.graphics.*;
import android.view.*;
import java.util.*;

/**
 * Controller class for the knight enemies present in the game. Knights are a beefed-up version of peasants and thus
 * are able to take more hits before dying. This class is responsible for drawing, creating, and
 * destroying knight enemies during the course of gameplay.
 * @author Group 9
 */

public class GameViewControllerKnights {

    // instance variables
    private Main activity;
    public Rect src;
    public Rect dst;
    private GameView gameView;

    /**
    * Default constructor
    * @param gameView       GameView object currently running game
    * @param activity       Main activity running game
    */
    public GameViewControllerKnights(GameView gameView, Main activity) {
        this.activity = activity;
        this.gameView = gameView;
    }
    
    /**
    * Creates a knight enemy from a given bitmap to be added to the list of knights in the game
    * @param bitmap         bitmap used to draw peasant enemy
    */
    public Knight createKnight(int bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap assets = BitmapFactory.decodeResource(gameView.getResources(), bitmap);

        assets = Bitmap.createScaledBitmap(assets, (int)(gameView.getHeight()*.20),(int)(gameView.getHeight()*.20),true);

        return new Knight(gameView,assets);
    }

    /**
    * Adds a new knight enemy to the list of knights in the GameView.
    * @param knight        arrayList of peasant to be added to 
    */
    public void addKnight(List<Knight> knight) {
        Random random = new Random();
        float temp = random.nextFloat();
        gameView.systemTime = System.currentTimeMillis();

        if(temp > (gameView.difficultyDecider)+0.04) {
            knight.add(createKnight(R.drawable.test2));
        }

    }
    
    /**
    * method to call from onDraw method of GameView. iterates over the list of knights in the game and draws each one that 
    * has not yet reached the edge of the screen
    * @param knights        list of knights currently in the game
    * @param healthBar      health bar object from game
    * @param canvas         canvas to be drawn on
    */
    public void drawKnight(List<Knight> knights, HealthBar healthBar, Canvas canvas) {
        Iterator<Knight> it1 = knights.iterator();
        while (it1.hasNext()) {
            Knight knight = it1.next();
            if (knight.getX() <= 0) {
                //if peasant is off screen then don't render anymore
                healthBar.takeHit();
                healthBar.takeHit();
                it1.remove();
                knight.bmp.recycle();
                knight.bmp = null;
                knight = null;

            } else if (knight.getX() > 0) {
                if (knight.bmp != null) {
                    knight.onDraw(canvas);
                }
            }
        }
    }

    /**
    * Method to determine if any knights were hit based on a given lightning bolt's location
    * @param knights       list of knight enemies in the game currently
    * @param x              x coordinate of lightning bolt
    * @param y              y coordinate of lightning bolt
    * @param boom           bitmap of explosion graphic to display after a hit
    * @param boom2          bitmap of explosion graphic to display after a kill
    */
    public void knightHit(List<Knight> knights, float x, float y, Bitmap boom, Bitmap boom2) {

        Iterator<Knight> it1 = knights.iterator();
        while (it1.hasNext()) {
            Knight knight = it1.next();
            if (knight.isCollision(x, y)) {
                if (knight.hitsTaken < 5 && gameView.removed == 0) {
                    knight.hitsTaken += 1;
                    int xcoord = knight.getX();
                    gameView.dst = new Rect(xcoord - (int) (boom2.getWidth()) / 3, (int) (gameView.getHeight() * 0.87), xcoord + boom2.getWidth() - (int) (boom2.getWidth()) / 3, (int) (gameView.getHeight() * 0.87) + boom2.getHeight());
                    gameView.src = new Rect(0, 0, boom2.getWidth(), (int) (boom2.getHeight()));
                    gameView.chooseBoom1 = 1;
                    gameView.removed = 1;
                } else if (knight.hitsTaken >= 5) {
                    if (gameView.removed == 0 && gameView.chooseBoom1 == 0) {

                        int xcoord = knight.getX();

                        gameView.dst = new Rect(xcoord - (int) (boom.getWidth() / 2), (int) (gameView.getHeight() * 0.85), xcoord + boom.getWidth() - (int) (boom.getWidth() / 2), (int) (gameView.getHeight() * 0.85) + boom.getHeight());
                        gameView.src = new Rect(0, (int) (boom.getHeight() * .3), boom.getWidth(), (int) (boom.getHeight() * .7));

                        activity.increaseScore();
                        it1.remove();

                        knight.bmp.recycle();
                        knight.bmp = null;
                        knight = null;
                        gameView.chooseBoom2 = 1;

                    }
                    gameView.removed = 1;
                    break;
                }
            }
        }

    }
}
