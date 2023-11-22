package algonquin.cst2335.androidfinalproject.sun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivitySunBinding;
import algonquin.cst2335.androidfinalproject.databinding.SunRecordBinding;

public class SunActivity extends AppCompatActivity {

    ActivitySunBinding binding;
    ArrayList<Sun> suns = null; // At the beginning, there are no messages; initialize in SunViewModel.java
    SunViewModel sunModel; // use a ViewModel to make sure data survive the rotation change
    private RecyclerView.Adapter sunAdapter; // to hold the object below
    SunDAO sDAO;
    int selectedRow; // to hold the "position", find which row this is"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySunBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("sunSharedData", Context.MODE_PRIVATE);
        binding.latInput.setText(prefs.getString("latitude",""));
        binding.lngInput.setText(prefs.getString("longitude",""));

        // onCreateOptionMenu
        setSupportActionBar(binding.sunToolbar);// initialize the toolbar
        getSupportActionBar().setTitle(getString(R.string.sun_toolbar_title));


        sunModel = new ViewModelProvider(this).get(SunViewModel.class);
        suns = sunModel.suns.getValue(); //get the array list from ViewModelProvider, might be NULL

        //listener to the MutableLiveData object
        sunModel.selectedSun.observe(this,(selectedSun) ->{
            if(selectedSun != null) {
                //create a Sun fragment
                SunDetailsFragment sunFragment = new SunDetailsFragment(selectedSun);

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction transaction = fMgr.beginTransaction();
                transaction.addToBackStack("Add to back stack"); // adds to the history
                transaction.replace(R.id.sunFragmentLocation, sunFragment);//The add() function needs the id of the FrameLayout where it will load the fragment
                transaction.commit();// This line actually loads the fragment into the specified FrameLayout
            }
        });

        //load sun records from the database:
        SunDatabase db = Room.databaseBuilder(getApplicationContext(),SunDatabase.class, "sunDatabase").build();
        //initialize the variable
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

        binding.sunSearchButton.setOnClickListener( cli ->{

            String sunLatitude = binding.latInput.getText().toString(); //todo: add a number filter, latitude range -90 to +90; consider illegal and null input
            String sunLongitude = binding.lngInput.getText().toString(); //todo: add a number filter, longitude range -180 to +180; consider illegal and null input
            String sunrise = "sunrise";
            String sunset = "sunset";
            String solar_noon = "noon";
            String golden_hour = "golden hour";
            String timezone = "-300";

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("latitude", sunLatitude);
            editor.putString("longitude", sunLongitude);
            editor.apply();

            Sun s = new Sun(sunLatitude, sunLongitude, sunrise, sunset, solar_noon, golden_hour, timezone);
            suns.add(s);

            // tell the recycle view that there is new data SetChanged()
            sunAdapter.notifyDataSetChanged(); //redraw the screen

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                sDAO.insertSun(s); //Once you get the data from database
//                s.sunId = sDAO.insertSun(s); //Once you get the data from database
            });

            //create a Sun fragment
            SunDetailsFragment sunFragment = new SunDetailsFragment(suns.get(selectedRow));

            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction transaction = fMgr.beginTransaction();
            transaction.addToBackStack("Add to back stack"); // adds to the history
            transaction.replace(R.id.sunFragmentLocation, sunFragment);//The add() function needs the id of the FrameLayout where it will load the fragment
            transaction.commit();// This line actually loads the fragment into the specified FrameLayout

            //clear the previous text
            binding.latInput.setText("");
            binding.lngInput.setText("");

        });

        // Will draw the recycle view
        binding.sunRecycleView.setAdapter(sunAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                SunDetailsLayoutBinding binding = SunDetailsLayoutBinding.inflate(getLayoutInflater(), parent, false);

                SunRecordBinding binding2 = SunRecordBinding.inflate(getLayoutInflater(),parent,false);
                return new MyRowHolder(binding2.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                // where to overwrite default text:
                Sun obj = suns.get(position);

                holder.sunLatitudeView.setText(obj.getSunLatitude());
                holder.sunLongitudeView.setText(obj.getSunLongitude());


//                holder.sunriseView.setText(obj.getSunrise());
//                holder.sunsetView.setText(obj.getSunset());
//                holder.solar_noonView.setText(obj.getSolar_noon());
//                holder.golden_hourView.setText(obj.getGolder_hour());
//                holder.timezoneView.setText(obj.getTimezone());
            }


            @Override
            public int getItemCount() {
                return suns.size();
            }
        });

        // To specify a single column scrolling in a Vertical direction
        binding.sunRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    // This class represents one row
    public class MyRowHolder extends RecyclerView.ViewHolder {

        public TextView sunLatitudeView; // maybe not needed?
        public TextView sunLongitudeView; // maybe not needed?
        public TextView sunriseView;
        public TextView sunsetView;
        public TextView solar_noonView;
        public TextView golden_hourView;
        public TextView timezoneView;

        public MyRowHolder(@NonNull View theRootConstraintLayout){
            super(theRootConstraintLayout);

            // Feature: deleting a message from the RecyclerView
            theRootConstraintLayout.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();//find which row this is
                Sun selected = suns.get(position);

                //starts the loading
                sunModel.selectedSun.postValue(selected);

                selectedRow = position; // pass position to the whole class scope variable to use in another class
            });

            // theRootConstraintLayout.findViewById
            sunLatitudeView = theRootConstraintLayout.findViewById(R.id.lat_detail);// maybe not needed?
            sunLongitudeView = theRootConstraintLayout.findViewById(R.id.lng_detail);// maybe not needed?
            sunriseView= theRootConstraintLayout.findViewById(R.id.sun_sunrise_detail);
            sunsetView= theRootConstraintLayout.findViewById(R.id.sun_sunset_detail);
            solar_noonView= theRootConstraintLayout.findViewById(R.id.sun_noon_detail);
            golden_hourView= theRootConstraintLayout.findViewById(R.id.sun_golden_detail);
            timezoneView= theRootConstraintLayout.findViewById(R.id.sun_timezone_detail);

        }
    }

    //load a Menu layout file,
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //inflate a menu into the toolbar
        getMenuInflater().inflate(R.menu.sun_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch( item.getItemId() ){
            case R.id.deleteSunLocation:

                //put your Sun deletion code here. If you select this item, you should show the alert dialog
                //asking if the user wants to delete this message

                int position = suns.indexOf(sunModel.selectedSun.getValue());
                if(position != RecyclerView.NO_POSITION) {
                    // temporarily stores the sun location before it is removed from the ArrayList
                    Sun toDelete = suns.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SunActivity.this);

//                    builder.setMessage("Do you want to delete this record: " + toDelete.getSunLatitude() + ", " + toDelete.getSunLongitude());
//                    builder.setTitle("Question: ");

                    builder.setMessage(getString(R.string.sun_del_warning_text) + toDelete.getSunLatitude() + ", " + toDelete.getSunLongitude());
                    builder.setTitle(getString(R.string.sun_del_warning_title));

                    builder.setNegativeButton(getString(R.string.sun_no), (btn, obj) -> { /* if no is clicked */ });
                    builder.setPositiveButton(getString(R.string.sun_yes), (btn, obj) -> { /* if yes is clicked */
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            //delete from database
                            sDAO.deleteSun(toDelete); //which sun location to delete?
                        });
                        suns.remove(position); //remove from the array list

                        sunAdapter.notifyDataSetChanged(); //redraw the list
                        getSupportFragmentManager().popBackStack(); // go back to message list

                        Snackbar.make(binding.sunRecycleView, getString(R.string.sun_del_after) + position, Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.sun_undo), click -> {
                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                    thread2.execute(() -> {
                                        sDAO.insertSun(toDelete);
                                    });

                                    suns.add(position, toDelete); // add the toDelete back to ArrayList to undo delete action
                                    sunAdapter.notifyDataSetChanged(); //redraw the list

                                    // after undo, can go back to the fragment
//                                    SunDetailsFragment sunFragment = new SunDetailsFragment(selectedSun);
//
//                                      FragmentManager fMgr = getSupportFragmentManager();
//                                      FragmentTransaction transaction = fMgr.beginTransaction();
//                                      transaction.addToBackStack("Add to back stack"); // adds to the history
//                                      transaction.replace(R.id.sunFragmentLocation, sunFragment);//The add() function needs the id of the FrameLayout where it will load the fragment
//                                      transaction.commit();// This line actually loads the fragment into the specified FrameLayout
                                }).show();
                    });
                    builder.create().show(); //this has to be last
                }
                break;
            case R.id.aboutSun:
                Toast.makeText(this,"Sunrise & Sunset, Version 1.0, created by Yu Song", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }// Handle other menu items if needed
        return true;
    }

    //Todo: write a function to check the input range: -90 to +90, 6 decimal places
//    public static void setupDecimalInput(final EditText editText) {
//        // Set an InputFilter to limit the decimal places
//        InputFilter decimalFilter = (source, start, end, dest, dstart, dend) -> {
//            String text = dest.toString();
//            if (text.contains(".") && text.substring(text.indexOf(".")).length() > 6) {
//                return "";
//            }
//            return null;
//        };
//
//        // Set a TextWatcher to check the range
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//                // No action needed
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                // No action needed
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                try {
//                    // Parse the input to a double
//                    double input = Double.parseDouble(editable.toString());
//
//                    // Check the range
//                    if (input < -90 || input > 90) {
//                        // If out of range, set the text to the limit
//                        editText.setText(String.valueOf(Math.max(-90, Math.min(90, input))));
//                        editText.setSelection(editText.getText().length()); // Move cursor to the end
//                    }
//                } catch (NumberFormatException ignored) {
//                    // Ignore if the input cannot be parsed to a double
//                }
//            }
//        });

}