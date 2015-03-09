package cs48.zeusalmighty;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

/**
 * Main is the class that the game actually takes place within. Main is responsible for saving a player's high score and
 * for initializing the GameView object that will run the game. 
 * @author Group 9
 */

public class Main extends Activity {

    // instance variables
    private int currentScore, highScore;
    private MediaPlayer inc, high;
    private GameView gameView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    /**
    * Creates new GameView object to run game from and loads player's high score to display
    */
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameView = new GameView(this);
        setContentView(gameView);

        preferences = this.getPreferences(Context.MODE_PRIVATE);
        currentScore = 0;
        highScore = preferences.getInt("High Score", 0);
        inc = MediaPlayer.create(this,R.raw.coinsound);
        high = MediaPlayer.create(this, R.raw.high);
    }

    /**
    * returns player's high score
    */
    public int getHighScore() {
        return highScore;
    }

    /**
    * returns player's current score
    */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
    * increases player's score by one
    */
    public void increaseScore(){
        this.inc.seekTo(0);
        this.inc.start();
        this.currentScore++;
    }

    /**
    * Saves player's score if it is higher than current high score
    */
    public void saveScore() {
        if (this.currentScore > this.highScore) {
            this.highScore = this.currentScore;
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("High Score", this.highScore);
            editor.commit();
        }
    }

}
