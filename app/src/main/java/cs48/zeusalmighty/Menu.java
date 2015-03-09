package cs48.zeusalmighty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Menu is a class to display an interactive menu when the user first starts the game. this menu allows the player
 * to start a new game or proceed to the options menu to change their difficulty. 
 * @author Group 9
 */

public class Menu extends Activity {

    @Override
    /**
    * Initializes menu and sets its content to the layout defined in activity_main.xml
    */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    /**
    * Method called when "Start Game" button is pressed, proceeds to Main activity where game is run
    * @param view       current view when button is pressed
    */
    public void startGame(View view) {
        Intent i = new Intent(this, Main.class);
        startActivity(i);
    }

    /**
    * Method called when "Options" button is pressed, proceeds to OptionsMenu activity 
    * where player can modify difficulty
    * @param view       current view when button is pressed
    */
    public void startOptions(View view) {
        Intent i = new Intent(this, OptionsMenu.class);
        startActivity(i);
    }
}
