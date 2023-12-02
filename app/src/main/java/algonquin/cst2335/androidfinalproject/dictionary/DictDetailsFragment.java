package algonquin.cst2335.androidfinalproject.dictionary;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.DictDetailsLayoutBinding;

/**
 * The {@code DictDetailsFragment} class represents a fragment for displaying details
 * of a selected dictionary entry in the Android final project. It extends
 * {@link Fragment} and is responsible for rendering the selected dictionary entry's
 * name and summary in a user interface.
 *
 * @author Yuling Guo
 * @version 1.0
 * @since 2023-11-29
 */
public class DictDetailsFragment extends Fragment {
    /**
     * The selected dictionary entry to display details for.
     */
    Dict selected;

    /**
     * Constructor for creating a {@code DictDetailsFragment} instance with a selected dictionary entry.
     *
     * @param d The selected dictionary entry.
     */
    public DictDetailsFragment(Dict d) {
        selected = d;
    }

    /**
     * Inflates the view for the fragment and sets up the user interface with selected dictionary entry details.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DictDetailsLayoutBinding binding = DictDetailsLayoutBinding.inflate(getLayoutInflater());

        // Set the dictionary name in the UI.
        binding.dictNameText.setText(selected.dictName);

        // Convert HTML formatted summary to Spanned and set it in the UI.
        Spanned spannedText = Html.fromHtml(selected.summary, Html.FROM_HTML_MODE_LEGACY);
        binding.summaryTitle.setText(spannedText);

        return binding.getRoot();
    }
}
