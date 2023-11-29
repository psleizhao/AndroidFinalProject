package algonquin.cst2335.androidfinalproject.music;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel for managing and storing UI-related data for the music application.
 * This ViewModel is used to hold and manage UI-related data in a lifecycle-conscious way.
 * It allows data to survive configuration changes such as screen rotations.
 */
public class MusicViewModel extends ViewModel {

    /**
     * MutableLiveData containing a list of Music objects.
     * Observers can observe this data to get updates on the music list.
     */
    public MutableLiveData<ArrayList<Music>> musics = new MutableLiveData<>();

    /**
     * MutableLiveData containing the currently selected Music object.
     * Observers can observe this data to get updates on the selected music item.
     */
    public MutableLiveData<Music> selectedMusic = new MutableLiveData<>();
}