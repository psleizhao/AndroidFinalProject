package algonquin.cst2335.androidfinalproject.dictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.MainActivity;
import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivityDictBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchDictBinding;
import algonquin.cst2335.androidfinalproject.music.MusicActivity;
import algonquin.cst2335.androidfinalproject.recipe.Recipe;
import algonquin.cst2335.androidfinalproject.recipe.RecipeActivity;
import algonquin.cst2335.androidfinalproject.sun.SunActivity;

public class DictActivity extends AppCompatActivity {
    private ActivityDictBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "DictPrefs";
    private static final String SEARCH_TEXT_KEY = "searchText";

    private ArrayList<Dict> dicts = null;
    private DictViewModel dictModel;
    private RecyclerView.Adapter dictAdapter;
    private DictDAO dDAO;

    protected RequestQueue queue = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDictBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        setSupportActionBar(binding.dictToolbar);
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
            String dictName = binding.dictTextInput.getText().toString().trim(); // Use user input
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
                holder.dictName.setText(obj.getDictName());
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
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.dict_menu, menu);
            return true;
        }
    }    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favoriteItem:
                Intent nextPage = new Intent(DictActivity.this, DictActivity.class);
                startActivity(nextPage);
                break;

            case R.id.addItem:
                if (dictModel.selectedDicts != null) {
                    int position = dicts.indexOf(dictModel.selectedDicts.getValue());
                    if (position != -1) {
                        Dict toSave = dicts.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DictActivity.this);
//                        builder.setMessage("Do you want to save this word " + toSave.getRecipeName())
//                                .setTitle("Question: ")
//                                .setPositiveButton("Yes", (dialog, cl) -> {
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            try {
                                Log.d("Dict", "try insert existing record");
                                dDAO.insertDict(toSave);
                                runOnUiThread(() -> {
                                    Log.d("Dict", "Dict saved successfully");
                                    Toast.makeText(DictActivity.this, R.string.dict_insertSucceedToast, Toast.LENGTH_SHORT).show();
                                });
                            } catch (Exception e) {
                                Log.d("Dict", "catch exception");
                                runOnUiThread(() -> Toast.makeText(DictActivity.this, R.string.dict_alreadyInToast, Toast.LENGTH_SHORT).show());
                            }
                        });
////                                   dictAdapter.notifyDataSetChanged();
////                                    getSupportFragmentManager().popBackStack(); // go back to message list
//                                })
//                                .setNegativeButton("No", (dialog, cl) -> {
//                                })
//                                .create().show();
                    } else {
                        Toast.makeText(this, R.string.dict_noSelectedToast, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.deleteItem:

                if (dictModel.selectedDicts != null) {
                    int position = dicts.indexOf(dictModel.selectedDicts.getValue());
                    if (position != -1) {
                        Dict toDelete = dicts.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DictActivity.this);
                        builder.setMessage(R.string.dict_deleteAlert + toDelete.getDictName() + "?")
                                .setTitle("Question: ")
                                .setPositiveButton(R.string.recipe_yes, (dialog, cl) -> {
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        dDAO.deleteDict(toDelete);
                                    });

                                    dicts.remove(position);
                                    dictAdapter.notifyDataSetChanged();
                                    getSupportFragmentManager().popBackStack(); // go back to message list

                                    Snackbar.make(binding.dictRecycleView,R.string.dict_deletedSnackbar + (position + 1), Snackbar.LENGTH_LONG)
                                            .setAction(R.string.recipe_undo, click -> {
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread1.execute(() -> {
                                                    dDAO.insertDict(toDelete);
                                                });
                                             dicts.add(position, toDelete);
                                                dictAdapter.notifyDataSetChanged();

                                                // after undo, go back to the fragment
//                                                RecipeDetailsFragment newMessage = new RecipeDetailsFragment(recipes.get(position));
//                                                FragmentManager fMgr = getSupportFragmentManager();
//                                                FragmentTransaction transaction = fMgr.beginTransaction();
//                                                transaction.addToBackStack("any string here");
//                                                transaction.replace(R.id.searchFragmentLocation, newMessage); //first is the FrameLayout id
//                                                transaction.commit();//loads it
                                            })
                                            .show();
                                })
                                .setNegativeButton(R.string.dict_no, (dialog, cl) -> {
                                })
                                .create().show();
                    } else {
                        Toast.makeText(this, R.string.dict_noSelectedToast, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.recipeBackToMainItem:
                Intent nextPage1 = new Intent(DictActivity.this, MainActivity.class);
                startActivity(nextPage1);
                break;

            case R.id.recipeGotoSunItem:
                Intent nextPage2 = new Intent(DictActivity.this, SunActivity.class);
                startActivity(nextPage2);
                break;

            case R.id.recipeGotoMusicItem:
                Intent nextPage3 = new Intent(DictActivity.this, MusicActivity.class);
                startActivity(nextPage3);
                break;

            case R.id.recipeGotoDictItem:
                Intent nextPage4 = new Intent(DictActivity.this, DictActivity.class);
                startActivity(nextPage4);
                break;

            case R.id.helpItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(DictActivity.this);
                builder.setMessage(R.string.recipe_helpAlert)
                        .setTitle(R.string.recipe_helpTitle)
                        .setPositiveButton(R.string.recipe_ok, (dialog, cl) -> {
                        }).create().show();
                break;

            case R.id.aboutDict:
                Toast.makeText(this, R.string.dict_aboutToast, Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }
        return true;
    }
}
