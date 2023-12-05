/**
 * Name: Zhicheng He 041086226
 * Course Section: CST2335 022
 * Description: This is the final project for the course CST2335 Mobile Graphical Interface Programming.
 *
 */

package algonquin.cst2335.androidfinalproject.music;


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
import algonquin.cst2335.androidfinalproject.databinding.MusicDetailsLayoutBinding;
import algonquin.cst2335.androidfinalproject.music.Music;

/**
 * Fragment for displaying the details of a selected music item.
 */
public class MusicDetailsFragment extends Fragment {
    Music selected;
    /**
     * static final variable TAG for fragment
     * */
    public static final String TAG = "MusicDetailsFragment";
    public MusicDetailsFragment() {    }

    /**
     * Constructor for creating a fragment with a specific music item.
     *
     * @param m The Music object whose details are to be displayed.
     */
    public MusicDetailsFragment(Music m) {
        selected = m;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        MusicDetailsLayoutBinding binding = MusicDetailsLayoutBinding.inflate(getLayoutInflater());
        File file = new File(requireContext().getFilesDir(), selected.getFileName());
        if (file.exists()) {
            Log.d("Image Log", "Got the larger image");
            Bitmap img = BitmapFactory.decodeFile(file.getAbsolutePath());
            binding.musicImageView.setImageBitmap(img);
        }
        binding.songTitle.setText(selected.songTitle);
        binding.duration.setText(getString(R.string.music_duration) + ": " + selected.duration + "s");
        binding.albumName.setText(selected.albumName);
        return binding.getRoot();
    }
}
