package algonquin.cst2335.androidfinalproject.sun;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class SunViewModel {
    public MutableLiveData<ArrayList<Sun>> suns = new MutableLiveData<>();
    public MutableLiveData<Sun> selectedSuns = new MutableLiveData<>();

}
