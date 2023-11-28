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
import algonquin.cst2335.androidfinalproject.dictionary.Dict;

public class DictDetailsFragment extends Fragment {
    Dict selected;

    public DictDetailsFragment(Dict d) {
        selected = d;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DictDetailsLayoutBinding binding = DictDetailsLayoutBinding.inflate(getLayoutInflater());

        binding.dictNameText.setText(selected.dictName);

        Spanned spannedText = Html.fromHtml(selected.summary, Html.FROM_HTML_MODE_LEGACY);
        binding.timeView.setText(selected.timeSent);
        binding.summaryTitle.setText(spannedText);

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
