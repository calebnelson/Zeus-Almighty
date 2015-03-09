package cs48.zeusalmighty;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * OptionsMenu is a class to display a menu for the player to change the game's difficulty. These settings are stored in 
 * a preferences file whenever a difficulty button is pressed.
 * @author Group 9
 */

public class OptionsMenu extends Activity {

    // instance variables
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private MediaPlayer player;
    private Button easy;
    private Button medium;
    private Button hard;

    @Override
    /**
    * Creates 3 difficulty buttons which will update the game's difficulty variable to a correspoding value by 
    * implementing onclicklisteners for each button that will call the helper method changeDifficulty
    */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.options_main);
        easy = (Button) findViewById(R.id.button3);
        medium = (Button) findViewById(R.id.button5);
        hard = (Button) findViewById(R.id.button4);
        player = MediaPlayer.create(this,R.raw.coinsound);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDifficulty(.99f);
                player.seekTo(0);
                player.start();
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDifficulty(.97f);
                player.seekTo(0);
                player.start();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDifficulty(.95f);
                player.seekTo(0);
                player.start();
            }
        });
    }

    /**
    * Helper method called when a button is pressed; changes difficulty based on which button was pressed
    * @param diff       float value of difficulty used by code to determine frequency of new enemy creation
    */
    public void changeDifficulty(float diff) {
        preferences = getSharedPreferences("Options", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putFloat("Difficulty", diff);
        editor.commit();
    }

}
