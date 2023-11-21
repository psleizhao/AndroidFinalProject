package algonquin.cst2335.androidfinalproject.recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.File;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.RecipeDetailsLayoutBinding;

public class RecipeDetailsFragment extends Fragment {
    Recipe selected;

    public RecipeDetailsFragment(Recipe r) {
        selected = r;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        RecipeDetailsLayoutBinding binding = RecipeDetailsLayoutBinding.inflate(getLayoutInflater());
        File file = new File(requireContext().getFilesDir(), selected.getId() + "-556x370.jpg");
        if (file.exists()) {
            Log.d("Image Log", "Got the larger image");
            Bitmap img = BitmapFactory.decodeFile(file.getAbsolutePath());
            binding.recipeImageView.setImageBitmap(img);
        }
        binding.recipeNameText.setText(selected.recipeName);

        Spanned spannedText = Html.fromHtml(selected.summary, Html.FROM_HTML_MODE_LEGACY);
        binding.summary.setText(spannedText);

        binding.sourceButton.setOnClickListener(v -> {
                    String url = selected.getSrcUrl(); // Replace with your actual URL

                    // Create an Intent to open the URL in a web browser
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    // Check if there's an app to handle the Intent
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Open the URL in the web browser
                        startActivity(intent);
                    } else {
                        // Handle the case where there is no app to handle the Intent
                        Toast.makeText(getContext(), "No app to handle URL", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        return binding.getRoot();
    }
}
