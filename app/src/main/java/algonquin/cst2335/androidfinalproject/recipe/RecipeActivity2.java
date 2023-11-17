package algonquin.cst2335.androidfinalproject.recipe;

import android.os.Bundle;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.Activity2RecipeBinding;
import algonquin.cst2335.androidfinalproject.databinding.ActivityRecipeBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchRecipeBinding;


public class RecipeActivity2 extends AppCompatActivity {

    Activity2RecipeBinding binding;
    ArrayList<Recipe> recipes = null;
    RecipeViewModel recipeModel;
    private RecyclerView.Adapter recipeAdapter;
    RecipeDAO rDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = Activity2RecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // call onCreateOptionsMenu()
        setSupportActionBar(binding.recipeToolbar); // only one line required to initialize the toolbar

        recipeModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipes = recipeModel.recipes.getValue();

        recipeModel.selectedrecipe.observe(this, (selectedRecipe) -> {

            if (selectedRecipe != null) {
                RecipeDetailsFragment newMessage = new RecipeDetailsFragment(selectedRecipe);

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction transaction = fMgr.beginTransaction();
                transaction.addToBackStack("any string here");
                transaction.replace(R.id.searchFragmentLocation, newMessage); //first is the FrameLayout id
                transaction.commit();//loads it

            }
        });

        RecipeDatabase db = Room.databaseBuilder(getApplicationContext(),RecipeDatabase.class, "recipedb").build();
        rDAO = db.recipeDAO();

        if(recipes == null) {
            recipeModel.recipes.postValue(recipes = new ArrayList<Recipe>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                recipes.addAll(rDAO.getAllRecipes()); //Once you get the data from database

                runOnUiThread(() -> binding.recipeRecycleView.setAdapter(recipeAdapter)); //You can then load the RecyclerView
            });
        }

        binding.recipeSearchButton.setOnClickListener(clk ->{

            String recipeName = "Fried Chicken";
            String imgUrl = "";
            String summary = "I like Fried Chicken";
            String srcUrl = "";

            Recipe r = new Recipe(recipeName, imgUrl, summary, srcUrl);
            recipes.add(r);

            binding.recipeTextInput.setText("");

            recipeAdapter.notifyDataSetChanged();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                r.id = rDAO.insertRecipe(r); //Once you get the data from database
            });

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

                holder.recipeName.setText(obj.getRecipeName()+position);
   //             holder.recipeIcon.setImageURI(Uri.parse((obj.getImgUrl())));
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


                        recipeModel.selectedrecipe.postValue(selected);


//                        int position = getAbsoluteAdapterPosition();
//                        Recipe toDelete = recipes.get(position);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this);
//                        builder.setMessage("Do you want to delete the recipe of " + recipeName.getText())
//                                .setTitle("Question: ")
//                                .setPositiveButton("Yes", (dialog, cl) -> {
//                                    Executor thread = Executors.newSingleThreadExecutor();
//                                    thread.execute(() ->
//                                    {
//                                        rDAO.deleteRecipe(toDelete);
//                                    });
//
//                                    recipes.remove(position);
//                                    recipeAdapter.notifyDataSetChanged();
//
//                                    Snackbar.make(itemView, "You deleted recipe #" + (position+1), Snackbar.LENGTH_LONG)
//                                            .setAction("Undo", click ->{
//                                                Executor thread1 = Executors.newSingleThreadExecutor();
//                                                thread.execute(() ->
//                                                {
//                                                    rDAO.insertRecipe(toDelete);
//                                                });
//                                                recipes.add(position, toDelete);
//                                                recipeAdapter.notifyDataSetChanged();
//                                            })
//                                            .show();
//                                })
//                                .setNegativeButton("No", (dialog, cl) -> {
//                                })
//                                .create().show();
//
//
                    });


            recipeName = itemView.findViewById(R.id.recipeResult);
            recipeIcon = itemView.findViewById(R.id.recipeIcon);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.recipe_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()) {
            case R.id.backItem:

                break;

            case R.id.deleteItem:

                if (recipeModel.selectedrecipe != null) {
                    int position = recipes.indexOf(recipeModel.selectedrecipe.getValue());
                    if (position != -1) {
                        Recipe toDelete = recipes.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity2.this);
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
                                                RecipeDetailsFragment newMessage = new RecipeDetailsFragment(recipes.get(position));
                                                FragmentManager fMgr = getSupportFragmentManager();
                                                FragmentTransaction transaction = fMgr.beginTransaction();
                                                transaction.addToBackStack("any string here");
                                                transaction.replace(R.id.searchFragmentLocation, newMessage); //first is the FrameLayout id
                                                transaction.commit();//loads it
                                            })
                                            .show();
                                })
                                .setNegativeButton("No", (dialog, cl) -> {
                                })
                                .create().show();
                    }
                    else {
                        Toast.makeText(this, "No message selected", Toast.LENGTH_SHORT).show();
                    }
                }


                break;

            case R.id.helpItem:

                break;

            default:
                break;
        }
        return true;
    }

}