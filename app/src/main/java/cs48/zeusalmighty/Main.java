package cs48.zeusalmighty;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class Main extends Activity {

    private int currentScore, highScore;
    private MediaPlayer inc, high;
    private GameView gameView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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

    public int getHighScore() {
        return highScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void increaseScore(){
        this.inc.seekTo(0);
        this.inc.start();
        this.currentScore++;
    }

    public void saveScore() {
        if (this.currentScore > this.highScore) {
            this.high.seekTo(0);
            this.high.start();
            this.highScore = this.currentScore;
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("High Score", this.highScore);
            editor.commit();
        }
        //currentScore = 0;
    }

}