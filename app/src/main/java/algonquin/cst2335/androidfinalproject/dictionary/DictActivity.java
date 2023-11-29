package algonquin.cst2335.androidfinalproject.dictionary;

import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.MainActivity;
import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivityDictBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchDictBinding;
import algonquin.cst2335.androidfinalproject.music.MusicActivity;
import algonquin.cst2335.androidfinalproject.recipe.RecipeActivity;
import algonquin.cst2335.androidfinalproject.sun.SunActivity;

public class DictActivity extends AppCompatActivity {
    private ActivityDictBinding binding;

    private ArrayList<Dict> dicts = null;
    private DictViewModel dictModel;
    private RecyclerView.Adapter dictAdapter;
    private DictDAO dDAO;
    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        binding = ActivityDictBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MyData,", Context.MODE_PRIVATE);
        binding.dictTextInput.setText(prefs.getString("DictName", ""));

        setSupportActionBar(binding.dictToolbar);
        dictModel = new ViewModelProvider(this).get(DictViewModel.class);
        dicts = dictModel.Dicts.getValue();

        dictModel.selectedDicts.observe(this, (selectedDicts) -> {
            if (selectedDicts != null) {
                DictDetailsFragment newDict = new DictDetailsFragment(selectedDicts);
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction transaction = fMgr.beginTransaction();
                transaction.addToBackStack("any string here");
                transaction.replace(R.id.searchFragmentLocation, newDict);
                transaction.commit();
            }
        });

        DictDatabase db = Room.databaseBuilder(getApplicationContext(), DictDatabase.class, "dictdb").build();
        dDAO = db.DictDAO();

        if (dicts == null) {
            dictModel.Dicts.postValue(dicts = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                dicts.addAll(dDAO.getAllDicts());
                runOnUiThread(() -> binding.dictRecycleView.setAdapter(dictAdapter));
            });
        }

        binding.dictSearchButton.setOnClickListener(clk -> {
            String dictTextInput = binding.dictTextInput.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("dictName", dictTextInput);
            editor.apply();

            String url = "https://api.dictionaryapi.dev/api/v2/entries/en/";

            try {
                url += URLEncoder.encode(dictTextInput, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            dicts.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject wordObject = response.getJSONObject(i);
                                String word = wordObject.getString("word");
                                JSONArray meanings = wordObject.getJSONArray("meanings");
                                for (int j = 0; j < meanings.length(); j++) {
                                    JSONObject aMeaning = meanings.getJSONObject(j);
                                    JSONArray definitions = aMeaning.getJSONArray("definitions");
                                    for (int k = 0; k < definitions.length(); k++) {
                                        String def = definitions.getJSONObject(k).getString("definition");
                                        Dict dict = new Dict(i, word, def, "url");
                                        dicts.add(dict);
                                    }
                                }
                            }

                            binding.dictTitleText.setText(R.string.dict_frgTitle);
                            dictAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, R.string.dict_notAvailableToast, Toast.LENGTH_SHORT).show();
                        }
                    },
                    (error) -> {
                        Log.d("joling", error.toString());
                        Toast.makeText(this, R.string.dict_notFoundToast, Toast.LENGTH_SHORT).show();
                    });

            queue.add(request);
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
                        Dict selected = dicts.get(position);

                        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/"
                                + selected.getId();

                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                                (response) -> {
                                    try {
                                        JSONArray resultsArray = response.getJSONArray(0);
                                        if (resultsArray.length() == 0) {
                                            Toast.makeText(itemView.getContext(), R.string.dict_notFoundToast, Toast.LENGTH_SHORT).show();
                                        } else {
                                            dicts.clear();
                                            for (int i = 0; i < resultsArray.length(); i++) {
                                                JSONObject result = resultsArray.getJSONObject(i);
                                                long id = result.getInt("id");
                                                String title = result.getString("title");
                                                String summary = "summary";
                                                String srcUrl = "url";

                                                Dict dict = new Dict(id, title, summary, srcUrl);
                                                dicts.add(dict);
                                            }
                                            binding.dictTitleText.setText(R.string.dict_frgTitle);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                },
                                (error) -> {
                                    Log.d("joling", error.toString());
                                });

                        queue.add(request);
                    });

            dictName = itemView.findViewById(R.id.dictResult);
            dictIcon = itemView.findViewById(R.id.dictIcon);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.dict_menu, menu);
        return true;
    }
    @Override
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

            case R.id.dictBackToMainItem:
                Intent nextPage1 = new Intent(DictActivity.this, MainActivity.class);
                startActivity(nextPage1);
                break;

            case R.id.dictGotoSunItem:
                Intent nextPage2 = new Intent(DictActivity.this, SunActivity.class);
                startActivity(nextPage2);
                break;

            case R.id.dictGotoMusicItem:
                Intent nextPage3 = new Intent(DictActivity.this, MusicActivity.class);
                startActivity(nextPage3);
                break;

            case R.id.dictGotoRecipeItem:
                Intent nextPage4 = new Intent(DictActivity.this, RecipeActivity.class);
                startActivity(nextPage4);
                break;

            case R.id.helpItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(DictActivity.this);
                builder.setMessage(R.string.dict_helpAlert)
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
