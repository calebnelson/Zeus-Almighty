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


public class GameViewControllerKnights {

    private Main activity;
    public Rect src;
    public Rect dst;
    private GameView gameView;


    public GameViewControllerKnights(GameView gameView, Main activity) {
        this.activity = activity;
        this.gameView = gameView;
    }



    public Knight createKnight(int bitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap assets = BitmapFactory.decodeResource(gameView.getResources(), bitmap);

        assets = Bitmap.createScaledBitmap(assets, (int)(gameView.getHeight()*.20),(int)(gameView.getHeight()*.20),true);

        return new Knight(gameView,assets);
    }

    public void addKnight(List<Knight> knight) {
        Random random = new Random();
        float temp = random.nextFloat();
        gameView.systemTime = System.currentTimeMillis();

        if(temp > (gameView.difficultyDecider)+0.04) {
            knight.add(createKnight(R.drawable.test2));
        }

    }
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
