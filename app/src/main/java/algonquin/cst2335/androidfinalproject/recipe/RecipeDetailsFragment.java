package algonquin.cst2335.androidfinalproject.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.RecipeDetailsLayoutBinding;

public class RecipeDetailsFragment extends Fragment {
    Recipe selected;

    public RecipeDetailsFragment(Recipe r) {selected = r;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        RecipeDetailsLayoutBinding binding = RecipeDetailsLayoutBinding.inflate(getLayoutInflater());

        binding.recipeNameText.setText(selected.recipeName);
        binding.recipeImageView.setImageResource(R.drawable.recipe_sample);
        binding.summary.setText(selected.summary);
        return binding.getRoot();
    }
}
