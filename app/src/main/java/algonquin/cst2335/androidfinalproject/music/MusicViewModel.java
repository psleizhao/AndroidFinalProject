package algonquin.cst2335.androidfinalproject.music;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MusicViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Music>> musics = new MutableLiveData<>();
    public MutableLiveData<Music> selectedMusic = new MutableLiveData<>();
}