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
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.MainActivity;
import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivityRecipeBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchRecipeBinding;
import algonquin.cst2335.androidfinalproject.dictionary.DictActivity;
import algonquin.cst2335.androidfinalproject.music.MusicActivity;
import algonquin.cst2335.androidfinalproject.sun.SunActivity;

/**
 * The main activity for managing and displaying recipes.
 * Allows users to search for recipes, view details, and perform various actions.
 * <p>
 * This activity integrates with Spoonacular API for recipe data and uses Room
 * database for local storage of recipes.
 */
public class RecipeActivity extends AppCompatActivity {
    /**
     * Data binding for the activity layout
     */
    ActivityRecipeBinding binding;
    /**
     * List of recipes displayed in the RecyclerView
     */
    ArrayList<Recipe> recipes = null;
    /**
     * ViewModel for managing recipe data
     */
    RecipeViewModel recipeModel;
    /**
     * RecyclerView Object for displaying elements in a list
     */
    private RecyclerView.Adapter recipeAdapter;
    /**
     * Data Access Object for interacting with the Room database
     */
    RecipeDAO rDAO;
    /**
     * RequestQueue for handling network requests using Volley library
     */
    protected RequestQueue queue = null;

    /**
     * Called when the activity is first created. Initializes UI, sets up listeners,
     * and fetches data from the API and local database.
     *
     * @param savedInstanceState The saved state of the activity, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this); // Set the queue of API request by Volley
        // binding the layout variables
        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // SharedPreferences for saving the data from last launch
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        binding.recipeTextInput.setText(prefs.getString("recipeName", ""));

        // call onCreateOptionsMenu() to initialize the toolbar
        setSupportActionBar(binding.recipeToolbar); // only one line required to initialize the toolbar

        // ViewModel for saving the screen when rotating
        recipeModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipes = recipeModel.recipes.getValue();

        // Selected recipe observer to initialize the fragment when select a recipe
        recipeModel.selectedrecipe.observe(this, (selectedRecipe) -> {

            if (selectedRecipe != null) {
                // create a new fragment to display the selected recipe details
                RecipeDetailsFragment newRecipe = new RecipeDetailsFragment(selectedRecipe);

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction transaction = fMgr.beginTransaction();
                transaction.addToBackStack("any string here");
                transaction.replace(R.id.searchFragmentLocation, newRecipe); //first is the FrameLayout id
                transaction.commit();//loads it
            }
        });

        // Create/call the database, call the Data Access object
        RecipeDatabase db = Room.databaseBuilder(getApplicationContext(), RecipeDatabase.class, "recipedb").build();
        rDAO = db.recipeDAO();

        if (recipes == null) {
            recipeModel.recipes.postValue(recipes = new ArrayList<Recipe>());

            // Load data from database
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                recipes.addAll(rDAO.getAllRecipes()); //Once you get the data from database
                runOnUiThread(() -> binding.recipeRecycleView.setAdapter(recipeAdapter)); //You can then load the RecyclerView
            });
        }

        // Set onClickListener
        binding.recipeSearchButton.setOnClickListener(clk -> {

            // Get input
            String recipeTextInput = binding.recipeTextInput.getText().toString();
            // Save input to SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("recipeName", recipeTextInput);
            editor.apply();

            // Create URL
            String url = "";
            try {
                url = "https://api.spoonacular.com/recipes/complexSearch?query="
                        + URLEncoder.encode(recipeTextInput, "UTF-8")       // encode recipe name
                        + "&apiKey=b9c6c4f327f846fbb4dd19b2be4fc887";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            // Request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> { // Get response
                        try {
                            JSONArray resultsArray = response.getJSONArray("results"); // get JSONArray
                            if (resultsArray.length() == 0) {
                                Toast.makeText(this, R.string.recipe_notFoundToast, Toast.LENGTH_SHORT).show();
                            } else {
                                recipes.clear(); // Clear the recycler view
                                // Load recipes from the JSON Array
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject result = resultsArray.getJSONObject(i);
                                    long id = result.getInt("id"); // Use API id as DB id since it's a number
                                    String title = result.getString("title");
                                    String imageUrl = result.getString("image");
                                    String imgType = result.getString("imageType");
                                    String summary = "summary";
                                    String srcUrl = "url";

                                    // Small image for recipe icon in recycler view
                                    String fileName = id + "-312x231.jpg";

                                    // Create recipe objects with summary and srcUrl default value
                                    // If not clicked on recycler view, no need the second API request
                                    Recipe recipe = new Recipe(title, fileName, summary, srcUrl, id);
                                    recipes.add(recipe);

                                    // Check whether the image exists
                                    File file = new File(getFilesDir(), fileName);
                                    if (file.exists()) {
                                        recipeAdapter.notifyDataSetChanged(); // If image exists, notify dataset change without a imgReq
                                    } else {
                                        // Image not exists, make an image request
                                        ImageRequest imgReq = new ImageRequest(imageUrl, bitmap -> {
                                            // Save image on device
                                            FileOutputStream fOut = null;
                                            try {
                                                fOut = openFileOutput(id + "-312x231.jpg", Context.MODE_PRIVATE);
                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                                fOut.flush();
                                                fOut.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            recipeAdapter.notifyDataSetChanged();
                                        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                                        });

                                        queue.add(imgReq); // Add image request to queue
                                    }

                                    // Change the page title to search result's title after images loaded
                                    // to prevent the asynchronous of the text and the images
                                    binding.recipeTitleText.setText(R.string.recipe_frgTitle);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {
                    });
            queue.add(request); // Add request to queue

            // Clear search bar
            binding.recipeTextInput.setText("");
        });

        binding.recipeRecycleView.setAdapter(recipeAdapter = new RecyclerView.Adapter<MyRowHolder>() { // recycler view adapter

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // set row's view
                SearchRecipeBinding binding = SearchRecipeBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                // Set row content
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

    /**
     * Inner class representing a row in the RecyclerView.
     * Handles item click events to display details about a selected recipe.
     */
    public class MyRowHolder extends RecyclerView.ViewHolder {
        /**
         * TextView for displaying the name of the recipe.
         */
        public TextView recipeName;
        /**
         * ImageView for displaying the icon associated with the recipe.
         */
        public ImageView recipeIcon;

        /**
         * Constructor for MyRowHolder class.
         *
         * @param itemView The view representing a row in the RecyclerView.
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk -> { // Set on row click listener
                int position = getAbsoluteAdapterPosition(); // Get selected recipe position
                Recipe selected = recipes.get(position); // Get selected recipe object

                // Create url
                String url = "https://api.spoonacular.com/recipes/"
                        + selected.getId()
                        + "/information?apiKey=b9c6c4f327f846fbb4dd19b2be4fc887";

                // Request
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                    String summary = null;
                    String srcUrl = null;
                    try {
                        summary = response.getString("summary");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    selected.setSummary(summary); // set selected recipe's summary
                    try {
                        srcUrl = response.getString("sourceUrl");
                        selected.setSrcUrl(srcUrl);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    selected.setSrcUrl(srcUrl); // set selected recipe's source url.

                    Executor thread1 = Executors.newSingleThreadExecutor();
                    thread1.execute(() -> {
                        rDAO.updateRecipe(selected); // update selected recipe
                    });

                    String imageUrl = null;
                    try {
                        imageUrl = response.getString("image"); // Get larger image url
                    } catch (JSONException e) {
//                                throw new RuntimeException(e);
                        runOnUiThread(() ->
                                Toast.makeText(RecipeActivity.this, R.string.recipe_notAvailableToast, Toast.LENGTH_SHORT).show()
                        );

                    }

                    // set image name
                    String fileName = selected.getId() + "-556x370.jpg";

                    File file = new File(getFilesDir(), fileName);

                    if (file.exists()) { // file exists
                        recipeModel.selectedrecipe.postValue(selected);
                    } else {
                        // Image request
                        ImageRequest imgReq = new ImageRequest(imageUrl, bitmap -> {
                            // Save image
                            FileOutputStream fOut = null;
                            try {
                                fOut = openFileOutput(fileName, Context.MODE_PRIVATE);

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                            recipeModel.selectedrecipe.postValue(selected); // Post value to view model
                        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                        });
                        queue.add(imgReq); // add image request to queue
                    }
                }, error -> {
                });
                queue.add(request); // add request to queue
            });

            recipeName = itemView.findViewById(R.id.recipeResult);
            recipeIcon = itemView.findViewById(R.id.recipeIcon);
        }
    }

    /**
     * Initializes the options menu in the toolbar.
     *
     * @param menu The menu to be inflated.
     * @return True if the menu is created successfully.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    /**
     * Handles item selection in the options menu.
     *
     * @param item The selected menu item.
     * @return True if the item selection is handled successfully.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favoriteItem: // Go to saved recipes
                Intent nextPage = new Intent(RecipeActivity.this, RecipeActivity.class);
                startActivity(nextPage);
                break;

            case R.id.addItem: // Add the selected recipe to favorites
                if (recipeModel.selectedrecipe != null) {
                    int position = recipes.indexOf(recipeModel.selectedrecipe.getValue());
                    if (position != -1) {
                        Recipe toSave = recipes.get(position);

                        // Insert selected recipe to database
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            try {
                                rDAO.insertRecipe(toSave);
                                runOnUiThread(() -> {
                                    Toast.makeText(RecipeActivity.this, R.string.recipe_insertSucceedToast, Toast.LENGTH_SHORT).show();
                                });
                            } catch (Exception e) {
                                runOnUiThread(() -> Toast.makeText(RecipeActivity.this, R.string.recipe_alreadyInToast, Toast.LENGTH_SHORT).show());
                            }
                        });
                    } else {
                        Toast.makeText(this, R.string.recipe_noSelectedToast, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.deleteItem: // delete selected recipe from the recycler view and the database

                if (recipeModel.selectedrecipe != null) {
                    int position = recipes.indexOf(recipeModel.selectedrecipe.getValue());
                    if (position != -1) {
                        Recipe toDelete = recipes.get(position);

                        // Alert user about deletion
                        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this);
                        builder.setMessage(R.string.recipe_deleteAlert + toDelete.getRecipeName() + "?")
                                .setTitle("Question: ")
                                .setPositiveButton(R.string.recipe_yes, (dialog, cl) -> { // Yes to delete
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        rDAO.deleteRecipe(toDelete);
                                    });

                                    recipes.remove(position);
                                    recipeAdapter.notifyDataSetChanged();
                                    getSupportFragmentManager().popBackStack(); // go back to message list

                                    Snackbar.make(binding.recipeRecycleView, R.string.recipe_deletedSnackbar + (position + 1), Snackbar.LENGTH_LONG)
                                            .setAction(R.string.recipe_undo, click -> {
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
                                .setNegativeButton(R.string.recipe_no, (dialog, cl) -> { // No to do nothing
                                })
                                .create().show();
                    } else {
                        Toast.makeText(this, R.string.recipe_noSelectedToast, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.recipeBackToMainItem: // Go back to lading page
                Intent nextPage1 = new Intent(RecipeActivity.this, MainActivity.class);
                startActivity(nextPage1);
                break;

            case R.id.recipeGotoSunItem: // Go to SunSeeker
                Intent nextPage2 = new Intent(RecipeActivity.this, SunActivity.class);
                startActivity(nextPage2);
                break;

            case R.id.recipeGotoMusicItem: // Go to DeezerDiscover
                Intent nextPage3 = new Intent(RecipeActivity.this, MusicActivity.class);
                startActivity(nextPage3);
                break;

            case R.id.recipeGotoDictItem: // Go to WordWiz
                Intent nextPage4 = new Intent(RecipeActivity.this, DictActivity.class);
                startActivity(nextPage4);
                break;

            case R.id.helpItem: // Help infos
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this);
                builder.setMessage(R.string.recipe_helpAlert)
                        .setTitle(R.string.recipe_helpTitle)
                        .setPositiveButton(R.string.recipe_ok, (dialog, cl) -> {
                        }).create().show();
                break;

            case R.id.aboutRecipe: // About RecipeRover
                Toast.makeText(this, R.string.recipe_aboutToast, Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }
        return true;
    }
}