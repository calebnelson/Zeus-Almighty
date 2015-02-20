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


public class OptionsMenu extends Activity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private MediaPlayer player;
    private Button easy;
    private Button medium;
    private Button hard;

    @Override
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
                changeDifficulty(.97f);
                player.seekTo(0);
                player.start();
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDifficulty(.94f);
                player.seekTo(0);
                player.start();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDifficulty(.91f);
                player.seekTo(0);
                player.start();
            }
        });
    }

    public void changeDifficulty(float diff) {
        preferences = getSharedPreferences("Options", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putFloat("Difficulty", diff);
        editor.commit();
    }

}
