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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.RecipeDetailsLayoutBinding;
/**
 * Fragment class for displaying details of a selected recipe.
 *
 * This fragment displays the name, image, summary, and a link to the source of the selected recipe.
 */
public class RecipeDetailsFragment extends Fragment {
    /** The selected Recipe object to display details. */
    Recipe selected;
    public static final String TAG = "RecipeDetailsFragment";

    /** Default constructor for RecipeDetailsFragment. */
    public RecipeDetailsFragment() {     }

    /**
     * Constructor for RecipeDetailsFragment with a Recipe parameter.
     *
     * @param r The Recipe object to display details.
     */
    public RecipeDetailsFragment(Recipe r) {
        selected = r;
    }

    /**
     * Creates and returns the View associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        RecipeDetailsLayoutBinding binding = RecipeDetailsLayoutBinding.inflate(getLayoutInflater());
        binding.sourceButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.recipe_btn));

        File file = new File(requireContext().getFilesDir(), selected.getId() + "-556x370.jpg");
        // Load the larger image if it exists
        if (file.exists()) {
            Log.d("Image Log", "Got the larger image");
            Bitmap img = BitmapFactory.decodeFile(file.getAbsolutePath());
            binding.recipeImageView.setImageBitmap(img);
        }
        binding.recipeNameText.setText(selected.recipeName);
        binding.recipeNameText.setGravity(Gravity.CENTER);
        // Convert HTML-formatted summary to Spanned text and set it
        Spanned spannedText = Html.fromHtml(selected.summary, Html.FROM_HTML_MODE_LEGACY);
        binding.summary.setText(spannedText);

        // Set up a click listener for the source button to open the recipe source in a web browser
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
                        Toast.makeText(getContext(), R.string.recipe_noBrowserToast, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        return binding.getRoot();
    }
}
