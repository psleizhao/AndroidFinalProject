package algonquin.cst2335.androidfinalproject.dictionary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivityDictBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchDictBinding;

public class DictActivity extends AppCompatActivity {
    private ActivityDictBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "DictPrefs";
    private static final String SEARCH_TEXT_KEY = "searchText";

    private ArrayList<Dict> dicts = null;
    private DictViewModel dictModel;
    private RecyclerView.Adapter dictAdapter;
    private DictDAO dDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDictBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        dictModel = new ViewModelProvider(this).get(DictViewModel.class);
        dicts = dictModel.Dicts.getValue();

        DictDatabase db = Room.databaseBuilder(getApplicationContext(), DictDatabase.class, "dictdb").build();
        dDAO = db.DictDAO();

        if (dicts == null) {
            dictModel.Dicts.postValue(dicts = new ArrayList<Dict>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                dicts.addAll(dDAO.getAllDicts());
                runOnUiThread(() -> binding.dictRecycleView.setAdapter(dictAdapter));
            });
        }

        // Restore the previous search text from SharedPreferences
        String savedSearchText = sharedPreferences.getString(SEARCH_TEXT_KEY, "");
        binding.dictTextInput.setText(savedSearchText);

        binding.dictSearchButton.setOnClickListener(clk -> {
            String dictName = "Fabulous";
            String imgUrl = "";
            String summary = "Wonderful";
            String srcUrl = "";

            Dict d = new Dict(dictName, imgUrl, summary, srcUrl);
            dicts.add(d);

            // Save the search text to SharedPreferences
            String searchText = binding.dictTextInput.getText().toString().trim();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SEARCH_TEXT_KEY, searchText);
            editor.apply();

            binding.dictTextInput.setText("");
            dictAdapter.notifyDataSetChanged();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                d.id = dDAO.insertDict(d);
            });

            // Display a Toast message
            Toast.makeText(DictActivity.this, "Search completed!", Toast.LENGTH_SHORT).show();
        });

        binding.dictRecycleView.setAdapter(dictAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SearchDictBinding binding = SearchDictBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                Dict obj = dicts.get(position);
                holder.dictName.setText(obj.getDictName() + position);
            }

            @Override
            public int getItemCount() {
                return dicts.size();
            }
        });

        binding.dictRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView dictName;
        public ImageView dictIcon;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(
                    clk -> {
                        int position = getAbsoluteAdapterPosition();
                        Dict toDelete = dicts.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(DictActivity.this);
                        builder.setMessage("Do you want to delete the word " + dictName.getText())
                                .setTitle("Question: ")
                                .setPositiveButton("Yes", (dialog, cl) -> {
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        dDAO.deleteDict(toDelete);
                                    });

                                    dicts.remove(position);
                                    dictAdapter.notifyDataSetChanged();

                                    Snackbar.make(itemView, "You deleted word #" + (position + 1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click -> {
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread1.execute(() -> {
                                                    dDAO.insertDict(toDelete);
                                                });
                                                dicts.add(position, toDelete);
                                                dictAdapter.notifyDataSetChanged();
                                            })
                                            .show();
                                })
                                .setNegativeButton("No", (dialog, cl) -> {
                                })
                                .create().show();
                    });

            dictName = itemView.findViewById(R.id.dictResult);
            dictIcon = itemView.findViewById(R.id.dictIcon);
        }
    }
}
