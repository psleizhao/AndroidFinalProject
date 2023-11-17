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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button recipeButton = binding.recipeButton;
        Button sunButton = binding.sunButton;
        Button musicButton = binding.musicButton;
        Button dictButton = binding.dictButton;

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