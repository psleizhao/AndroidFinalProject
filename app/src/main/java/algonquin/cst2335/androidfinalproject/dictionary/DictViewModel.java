package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DictViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Dict>> Dicts = new MutableLiveData<>();
    public MutableLiveData<Dict> selectedDicts = new MutableLiveData< >();

}