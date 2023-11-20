package algonquin.cst2335.androidfinalproject.recipe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.io.File;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.RecipeDetailsLayoutBinding;

public class RecipeDetailsFragment extends Fragment {
    Recipe selected;

    public RecipeDetailsFragment(Recipe r) {selected = r;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        RecipeDetailsLayoutBinding binding = RecipeDetailsLayoutBinding.inflate(getLayoutInflater());
        File file = new File(requireContext().getFilesDir(), selected.getId() + "-556x370.jpg" );
        if(file.exists()) {
            Log.d("Image Log", "Got the larger image");
            Bitmap img = BitmapFactory.decodeFile(file.getAbsolutePath());
            binding.recipeImageView.setImageBitmap(img);
        }
        binding.recipeNameText.setText(selected.recipeName);
        binding.summary.setText(selected.summary);
        binding.sourceButton.setOnClickListener(clk -> {
                    binding.sourceButton.setText("Went to source");
                }
        );
        return binding.getRoot();
    }
}
