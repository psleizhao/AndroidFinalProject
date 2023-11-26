package algonquin.cst2335.androidfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

import algonquin.cst2335.androidfinalproject.databinding.ActivityMainBinding;
import algonquin.cst2335.androidfinalproject.dictionary.DictActivity;
import algonquin.cst2335.androidfinalproject.music.MusicActivity;
import algonquin.cst2335.androidfinalproject.recipe.RecipeActivity;
import algonquin.cst2335.androidfinalproject.sun.SunActivity;

/**
 * The MainActivity class represents the main entry point of the Android Final Project application.
 * This activity provides a user interface with buttons to navigate to different features of the app.
 *
 * The features include:
 * - Recipe search and display
 * - Sun activity information
 * - Music player functionality
 * - Dictionary lookup and definition
 *
 * @author Lei Zhao
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting. This is where most initialization
     * should go: calling {@code setContentView(int)} to inflate the activity's UI,
     * using {@code findViewById} to programmatically interact with widgets in the UI,
     * and other initialization.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains the data
     *                           it most recently supplied in {@link #onSaveInstanceState}.
     *                           Note: Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button recipeButton = binding.recipeButton;
        Button sunButton = binding.sunButton;
        Button musicButton = binding.musicButton;
        Button dictButton = binding.dictButton;

        // Set click listeners for each button to navigate to their respective activities
        recipeButton.setOnClickListener( clk-> {

            Intent nextPage = new Intent( MainActivity.this, RecipeActivity.class);
            startActivity(nextPage);
        } );

        sunButton.setOnClickListener( clk-> {

            Intent nextPage = new Intent( MainActivity.this, SunActivity.class);
            startActivity(nextPage);
        } );

        musicButton.setOnClickListener( clk-> {

            Intent nextPage = new Intent( MainActivity.this, MusicActivity.class);
            startActivity(nextPage);
        } );

        dictButton.setOnClickListener( clk-> {

            Intent nextPage = new Intent( MainActivity.this, DictActivity.class);
            startActivity(nextPage);
        } );
    }
}