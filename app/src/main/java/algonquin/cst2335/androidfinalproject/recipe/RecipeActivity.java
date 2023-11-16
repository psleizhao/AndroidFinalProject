package algonquin.cst2335.androidfinalproject.recipe;



import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import algonquin.cst2335.androidfinalproject.databinding.ActivityRecipeBinding;



public class RecipeActivity extends AppCompatActivity {

    ActivityRecipeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}