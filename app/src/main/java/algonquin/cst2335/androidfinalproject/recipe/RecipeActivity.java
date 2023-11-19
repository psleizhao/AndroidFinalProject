package algonquin.cst2335.androidfinalproject.recipe;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivityRecipeBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchRecipeBinding;


public class RecipeActivity extends AppCompatActivity {

    ActivityRecipeBinding binding;
    ArrayList<Recipe> recipes = null;
    RecipeViewModel recipeModel;
    private RecyclerView.Adapter recipeAdapter;
    RecipeDAO rDAO;

    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        binding.recipeTextInput.setText(prefs.getString("recipeName", ""));

        // call onCreateOptionsMenu()
        setSupportActionBar(binding.recipeToolbar); // only one line required to initialize the toolbar

        recipeModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipes = recipeModel.recipes.getValue();

        recipeModel.selectedrecipe.observe(this, (selectedRecipe) -> {

            if (selectedRecipe != null) {
                RecipeDetailsFragment newRecipe = new RecipeDetailsFragment(selectedRecipe);


                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction transaction = fMgr.beginTransaction();
                transaction.addToBackStack("any string here");
                transaction.replace(R.id.searchFragmentLocation, newRecipe); //first is the FrameLayout id
                transaction.commit();//loads it
            }

        });

        RecipeDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "recipedb").build();
        rDAO = db.recipeDAO();

        if (recipes == null) {
            recipeModel.recipes.postValue(recipes = new ArrayList<Recipe>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                recipes.addAll(rDAO.getAllRecipes()); //Once you get the data from database

                runOnUiThread(() -> binding.recipeRecycleView.setAdapter(recipeAdapter)); //You can then load the RecyclerView
            });
        }

        binding.recipeSearchButton.setOnClickListener(clk -> {
            recipes.clear();
            String recipeTextInput = binding.recipeTextInput.getText().toString();
            binding.recipeTitleText.setText("Try One?");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("recipeName", recipeTextInput);
            editor.apply();

            String url = "";
            try {
                url = "https://api.spoonacular.com/recipes/complexSearch?query="
                        + URLEncoder.encode(recipeTextInput, "UTF-8")
                        + "&apiKey=aee190b735d046eea12abceaf17ac29c";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            Log.d("Recipe", "Request URL: " + url);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONArray resultsArray = response.getJSONArray("results");

                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject result = resultsArray.getJSONObject(i);
                                long id = result.getInt("id");
                                String title = result.getString("title");
                                String imageUrl = result.getString("image");
                                String imgType = result.getString("imageType");
                                String summary = "summary";
                                String srcUrl = "url";

                                String fileName = id + "-312x231.jpg";


//                                String urlSummary = "https://api.spoonacular.com/recipes/"
//                                        + id
//                                        + "/information?apiKey=b9c6c4f327f846fbb4dd19b2be4fc887";
//
//                                JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, urlSummary, null, response1 -> {
//                                    String summary;
//                                    String srcUrl;
//                                    try {
//                                         summary = response.getString("summary");
////                                        selected.setSummary(summary);
//
//                                    } catch (JSONException e) {
//                                        throw new RuntimeException(e);
//                                    }
//
//
//                                    try {
//                                        srcUrl = response.getString("sourceUrl");
////                                        selected.setSrcUrl(srcUrl);
//                                    } catch (JSONException e) {
//                                        throw new RuntimeException(e);
//                                    }
//
//                                    String imageUrl1;
//                                    try {
//                                        imageUrl1 = response.getString("image");
//                                    } catch (JSONException e) {
//                                        throw new RuntimeException(e);
//                                    }
//
//                                    String fileName1 = id + "-556x370.jpg";
//
//                                    File file = new File( getFilesDir(), fileName1);
//
//                                    if (file.exists()) {
//                                        Log.d("Recipe App", "File path: " + file.getAbsolutePath());
//                                    } else {
//                                        Log.d("Recipe App", "got in else " + imageUrl1);
//                                        ImageRequest imgReq = new ImageRequest(imageUrl1, bitmap -> {
//                                            // Do something with loaded bitmap...
//                                            FileOutputStream fOut = null;
//                                            try {
//                                                fOut = openFileOutput(fileName1, Context.MODE_PRIVATE);
//
//                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                                                fOut.flush();
//                                                fOut.close();
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//
//                                            }
//                                        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
//
//                                        });
//
//                                        queue.add(imgReq);
//
//                                    }
//                                    Recipe recipe = new Recipe(title, fileName, summary, srcUrl, id);
//                                    recipes.add(recipe);
//                                }, error -> {});
//
//                                queue.add(request1);

                                Recipe recipe = new Recipe(title, fileName, summary, srcUrl, id);
                                recipes.add(recipe);

                                File file = new File(getFilesDir(), fileName);
                                Log.d("Recipe App", "File path: " + file.getAbsolutePath());
                                if (file.exists()) {
                                    Log.d("Recipe App", "File path: " + file.getAbsolutePath());
                                } else {
                                    Log.d("Recipe App", "got in else " + imageUrl);
                                    ImageRequest imgReq = new ImageRequest(imageUrl, bitmap -> {
                                        // Do something with loaded bitmap...
                                        FileOutputStream fOut = null;
                                        try {
                                            fOut = openFileOutput(id + "-312x231.jpg", Context.MODE_PRIVATE);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                            fOut.flush();
                                            fOut.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                                    });
                                    queue.add(imgReq);
                                }
                            }
                            recipeAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {
                    });
            queue.add(request);

//            binding.recipeTextInput.setText("");
        });

        binding.recipeRecycleView.setAdapter(recipeAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SearchRecipeBinding binding = SearchRecipeBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                Recipe obj = recipes.get(position);
                File file = new File(getFilesDir(), obj.getImgUrl());
                Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                holder.recipeName.setText(obj.getRecipeName());
                holder.recipeIcon.setImageBitmap(theImage);
            }

            @Override
            public int getItemCount() {
                return recipes.size();
            }
        });

        binding.recipeRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView recipeName;
        public ImageView recipeIcon;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(
                    clk -> {
                        int position = getAbsoluteAdapterPosition();
                        Recipe selected = recipes.get(position);

                        String url = "https://api.spoonacular.com/recipes/"
                                + selected.getId()
                                + "/information?apiKey=aee190b735d046eea12abceaf17ac29c";

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                            String summary = null;
                            String srcUrl = null;
                            try {
                                summary = response.getString("summary");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            selected.setSummary(summary);
                            try {
                                srcUrl = response.getString("sourceUrl");
                                selected.setSrcUrl(srcUrl);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            selected.setSrcUrl(srcUrl);

                            Executor thread1 = Executors.newSingleThreadExecutor();
                            thread1.execute(() -> {
                                rDAO.updateRecipe(selected);
                            });

                            String imageUrl = null;
                            try {
                                imageUrl = response.getString("image");
                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
                                runOnUiThread(() ->
                                        Toast.makeText(RecipeActivity.this, "This recipe is not available for now", Toast.LENGTH_SHORT).show()
                                );

                            }

                            String fileName = selected.getId() + "-556x370.jpg";

                            File file = new File( getFilesDir(), fileName);

                            if (file.exists()) {
                                Log.d("Recipe App", "File path: " + file.getAbsolutePath());
                                recipeModel.selectedrecipe.postValue(selected);
                            } else {
                                Log.d("Recipe App", "got in else " + imageUrl);
                                ImageRequest imgReq = new ImageRequest(imageUrl, bitmap -> {
                                    // Do something with loaded bitmap...
                                    FileOutputStream fOut = null;
                                    try {
                                        fOut = openFileOutput(fileName, Context.MODE_PRIVATE);

                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                        fOut.flush();
                                        fOut.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();

                                    }
                                    recipeModel.selectedrecipe.postValue(selected);
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                                });

                                queue.add(imgReq);

                            }
//                            recipeModel.selectedrecipe.postValue(selected);

                        }, error -> {});
                        queue.add(request);
                    });

            recipeName = itemView.findViewById(R.id.recipeResult);
            recipeIcon = itemView.findViewById(R.id.recipeIcon);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favoriteItem:
                Intent nextPage = new Intent(RecipeActivity.this, RecipeActivity.class);
                startActivity(nextPage);
                break;

            case R.id.addItem:
                if (recipeModel.selectedrecipe != null) {
                    int position = recipes.indexOf(recipeModel.selectedrecipe.getValue());
                    if (position != -1) {
                        Recipe toSave = recipes.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this);
//                        builder.setMessage("Do you want to save the recipe of " + toSave.getRecipeName())
//                                .setTitle("Question: ")
//                                .setPositiveButton("Yes", (dialog, cl) -> {
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            try {
                                Log.d("Recipe", "try insert existing record");
                                rDAO.insertRecipe(toSave);
                                runOnUiThread(() -> {
                                    Log.d("Recipe", "Recipe saved successfully");
                                    Toast.makeText(RecipeActivity.this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
                                });
                            } catch (Exception e) {
                                Log.d("Recipe", "catch exception");
                                runOnUiThread(() -> Toast.makeText(RecipeActivity.this, "Recipe already in your list", Toast.LENGTH_SHORT).show());
                            }
                        });
////                                    recipeAdapter.notifyDataSetChanged();
////                                    getSupportFragmentManager().popBackStack(); // go back to message list
//                                })
//                                .setNegativeButton("No", (dialog, cl) -> {
//                                })
//                                .create().show();
                    } else {
                        Toast.makeText(this, "No recipe selected", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.deleteItem:

                if (recipeModel.selectedrecipe != null) {
                    int position = recipes.indexOf(recipeModel.selectedrecipe.getValue());
                    if (position != -1) {
                        Recipe toDelete = recipes.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this);
                        builder.setMessage("Do you want to delete the recipe of " + toDelete.getRecipeName())
                                .setTitle("Question: ")
                                .setPositiveButton("Yes", (dialog, cl) -> {
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        rDAO.deleteRecipe(toDelete);
                                    });

                                    recipes.remove(position);
                                    recipeAdapter.notifyDataSetChanged();
                                    getSupportFragmentManager().popBackStack(); // go back to message list

                                    Snackbar.make(binding.recipeRecycleView, "You deleted recipe #" + (position + 1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click -> {
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread1.execute(() -> {
                                                    rDAO.insertRecipe(toDelete);
                                                });
                                                recipes.add(position, toDelete);
                                                recipeAdapter.notifyDataSetChanged();

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
                                .setNegativeButton("No", (dialog, cl) -> {
                                })
                                .create().show();
                    } else {
                        Toast.makeText(this, "No Recipe selected", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.helpItem:
                Toast.makeText(this, "❤️Go To Saved Recipes\n\uD83D\uDCE5Save This Recipe\n\uD83D\uDDD1️Delete This Recipe", Toast.LENGTH_LONG).show();
                break;

            case R.id.aboutRecipe:
                Toast.makeText(this, "RecipeRover created by Lei Zhao", Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }
        return true;
    }
}