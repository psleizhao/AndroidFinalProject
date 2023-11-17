package algonquin.cst2335.androidfinalproject.sun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivitySunBinding;

public class SunActivity extends AppCompatActivity {

    ActivitySunBinding binding;
    ArrayList<Sun> suns = null; // At the beginning, there are no messages; initialize in SunViewModel.java
    SunViewModel sunModel; // use a ViewModel to make sure data survive the rotation change
    private RecyclerView.Adapter sunAdapter; // to hold the object below
    SunDAO sDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySunBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // onCreateOptionMenu
        setSupportActionBar(binding.sunToolbar);// initialize the toolbar

        sunModel = new ViewModelProvider(this).get(SunViewModel.class);
        suns = sunModel.suns.getValue(); //get the array list from ViewModelProvider, might be NULL

        //listener to the MutableLiveData object
        sunModel.selectedSun.observe(this,(selectedSun) ->{
            if(selectedSun != null) {
                //create a Sun fragment

            }
        });


        SunDatabase db = Room.databaseBuilder(getApplicationContext(),SunDatabase.class, "sunDatabase").build();
        sDAO = db.sunDAO();

        if (suns == null) {
            sunModel.suns.postValue(suns = new ArrayList<Sun>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                suns.addAll(sDAO.getAllSuns()); //Once you get the data from database

                runOnUiThread(() -> binding.sunRecycleView.setAdapter(sunAdapter)); //You can then load the RecyclerView
            });

        }


    }
}