package cs48.zeusalmighty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class Menu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {
        Intent i = new Intent(this, Main.class);
        startActivity(i);
    }

    public void startOptions(View view) {
        Intent i = new Intent(this, OptionsMenu.class);
        startActivity(i);
    }
}
