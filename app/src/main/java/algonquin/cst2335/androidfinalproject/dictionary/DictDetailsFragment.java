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
