package algonquin.cst2335.androidfinalproject.dictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;
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
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * Dict selectedDict = // Obtain a selected dictionary entry.
 * DictDetailsFragment detailsFragment = new DictDetailsFragment(selectedDict);
 * // Use a FragmentManager to add this fragment to the UI.
 * }
 * </pre>
 *
 * @author Yuling Guo
 * @version 1.0
 * @since 2023-11-29
 */

public class DictDetailsFragment extends Fragment {
    Dict selected;

    public DictDetailsFragment(Dict d) {
        selected = d;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DictDetailsLayoutBinding binding = DictDetailsLayoutBinding.inflate(getLayoutInflater());

        binding.dictNameText.setText(selected.dictName);

        Spanned spannedText = Html.fromHtml(selected.summary, Html.FROM_HTML_MODE_LEGACY);

        binding.summaryTitle.setText(spannedText);

        return binding.getRoot();
    }
}
