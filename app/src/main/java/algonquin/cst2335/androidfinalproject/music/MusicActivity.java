package algonquin.cst2335.androidfinalproject.music;

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
import android.net.Uri;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import algonquin.cst2335.androidfinalproject.databinding.ActivityMusicBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchMusicBinding;
import algonquin.cst2335.androidfinalproject.music.Music;
import algonquin.cst2335.androidfinalproject.music.MusicDatabase;
import algonquin.cst2335.androidfinalproject.music.MusicDetailsFragment;
import algonquin.cst2335.androidfinalproject.recipe.Recipe;
import algonquin.cst2335.androidfinalproject.recipe.RecipeActivity;


public class MusicActivity extends AppCompatActivity {

    ActivityMusicBinding binding;

    ArrayList<Music> music1 = null;
    MusicViewModel musicModel;

    private RecyclerView.Adapter musicAdapter;

    MusicDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        binding.musicTextInput.setText(prefs.getString("musicName", ""));

        setSupportActionBar(binding.musicToolbar);

        musicModel = new ViewModelProvider(this).get(MusicViewModel.class);
        music1 = musicModel.musics.getValue();

        musicModel.selectedMusic.observe(this, (selectedMusic) -> {

            if (selectedMusic != null) {
                MusicDetailsFragment newMusic = new MusicDetailsFragment(selectedMusic);

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction transaction = fMgr.beginTransaction();
                transaction.addToBackStack("any string here");
                transaction.replace(R.id.searchFragmentLocation, newMusic); //first is the FrameLayout id
                transaction.commit();//loads it
            }
        });

        MusicDatabase db = Room.databaseBuilder(getApplicationContext(), MusicDatabase.class, "musicdb").build();
        mDAO = db.musicDAO();

        if (music1 == null) {
            musicModel.musics.postValue(music1 = new ArrayList<Music>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                music1.addAll(mDAO.getAllMusics()); //Once you get the data from database

                runOnUiThread(() -> binding.musicRecycleView.setAdapter(musicAdapter)); //You can then load the RecyclerView
            });
        }

        binding.musicSearchButton.setOnClickListener(clk -> {

            String musicTextInput = binding.musicTextInput.getText().toString();
//            binding.musicTitleText.setText("Try One?");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("musicName", musicTextInput);
            editor.apply();

            String url = "";
            try {
                url = "https://api.deezer.com/search/artist/?q="
                        + URLEncoder.encode(musicTextInput, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            Log.d("Music", "Request URL: " + url);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONArray resultsArray = response.getJSONArray("results");
                            if (resultsArray.length() == 0) {
                                Toast.makeText(this, "Sorry, found nothing", Toast.LENGTH_SHORT).show();
                            } else {
                                music1.clear();
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject result = resultsArray.getJSONObject(i);
                                    long id = result.getInt("id");
                                    String title = result.getString("title");
                                    String imageUrl = result.getString("image");
                                    String imgType = result.getString("imageType");
                                    String summary = "summary";
                                    String srcUrl = "url";

                                    String fileName = id + "-312x231.jpg";

                                    Music music = new Music(title, fileName, summary, srcUrl, id);
                                    music1.add(music);

                                    File file = new File(getFilesDir(), fileName);
                                    Log.d("Music App", "File path: " + file.getAbsolutePath());
                                    if (file.exists()) {
                                        Log.d("Music App", "File path: " + file.getAbsolutePath());
                                        musicAdapter.notifyDataSetChanged();
                                    } else {
                                        Log.d("Music App", "got in else " + imageUrl);
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
                                            }musicAdapter.notifyDataSetChanged();
                                        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                                        });

                                        queue.add(imgReq);
                                    }

                                    binding.musicTitleText.setText("Try One?");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {
                    });
            queue.add(request);

//            binding.musicTextInput.setText("");
        });

        binding.musicRecycleView.setAdapter(musicAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SearchMusicBinding binding = SearchMusicBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                File file = new File(getFilesDir(), obj.getImgUrl());
                Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                holder.musicName.setText(obj.getRecipeName());
                holder.musicIcon.setImageBitmap(theImage);

            }

            @Override
            public int getItemCount() {
                return music1.size();
            }
        });

        binding.musicRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

   public class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView musicName;
        public ImageView musicIcon;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(
                    clk -> {

                        int position = getAbsoluteAdapterPosition();
                        Music toDelete = music1.get(position);

                        Music selected = null;
                        String url = "https://api.deezer.com/search/artist/?q="
                                + selected.getId();

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
                                mDAO.updateMusic(selected);
                            });

                            String imageUrl = null;
                            try {
                                imageUrl = response.getString("image");
                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
                                runOnUiThread(() ->
                                        Toast.makeText(MusicActivity.this, "This music is not available for now", Toast.LENGTH_SHORT).show()
                                );

                            }

                            String fileName = selected.getId() + "-556x370.jpg";

                            File file = new File(getFilesDir(), fileName);

                            if (file.exists()) {
                                Log.d("Music App", "File path: " + file.getAbsolutePath());
                                musicModel.selectedMusic.postValue(selected);
                            } else {
                                Log.d("Music App", "got in else " + imageUrl);
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
                                    musicModel.selectedMusic.postValue(selected);
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {

                                });

                                queue.add(imgReq);

                            }
//                            musicModel.selectedMusic.postValue(selected);

                        }, error -> {
                        });
                        queue.add(request);
                    });


            musicName = itemView.findViewById(R.id.musicResult);
            musicIcon = itemView.findViewById(R.id.musicIcon);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.music_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favoriteItem:
                Intent nextPage = new Intent(MusicActivity.this, MusicActivity.class);
                startActivity(nextPage);
                break;

            case R.id.addItem:
                if (musicModel.selectedMusic != null) {
                    int position = music1.indexOf(musicModel.selectedMusic.getValue());
                    if (position != -1) {
                        Music toSave = music1.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MusicActivity.this);
//                        builder.setMessage("Do you want to save the music of " + toSave.getMusicName())
//                                .setTitle("Question: ")
//                                .setPositiveButton("Yes", (dialog, cl) -> {
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            try {
                                Log.d("Music", "try insert existing record");
                                mDAO.insertMusic(toSave);
                                runOnUiThread(() -> {
                                    Log.d("Music", "Music saved successfully");
                                    Toast.makeText(MusicActivity.this, "Music saved successfully", Toast.LENGTH_SHORT).show();
                                });
                            } catch (Exception e) {
                                Log.d("Music", "catch exception");
                                runOnUiThread(() -> Toast.makeText(MusicActivity.this, "Music already in your list", Toast.LENGTH_SHORT).show());
                            }
                        });
////                                    musicAdapter.notifyDataSetChanged();
////                                    getSupportFragmentManager().popBackStack(); // go back to message list
//                                })
//                                .setNegativeButton("No", (dialog, cl) -> {
//                                })
//                                .create().show();
                    } else {
                        Toast.makeText(this, "No music selected", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.deleteItem:

                if (musicModel.selectedMusic != null) {
                    int position = music1.indexOf(musicModel.selectedMusic.getValue());
                    if (position != -1) {
                        Music toDelete = music1.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MusicActivity.this);
                        builder.setMessage("Do you want to delete the music of " + toDelete.getMusicName())
                                .setTitle("Question: ")
                                .setPositiveButton("Yes", (dialog, cl) -> {
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        mDAO.deleteMusic(toDelete);
                                    });

                                    music1.remove(position);
                                    musicAdapter.notifyDataSetChanged();
                                    getSupportFragmentManager().popBackStack(); // go back to message list

                                    Snackbar.make(binding.musicRecycleView, "You deleted music #" + (position + 1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click -> {
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread1.execute(() -> {
                                                    mDAO.insertMusic(toDelete);
                                                });
                                                music1.add(position, toDelete);
                                                musicAdapter.notifyDataSetChanged();

                                                // after undo, go back to the fragment
//                                                MusicDetailsFragment newMessage = new MusicDetailsFragment(music1.get(position));
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
                        Toast.makeText(this, "No Music selected", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.backItem:
                Intent nextPage1 = new Intent(MusicActivity.this, MainActivity.class);
                startActivity(nextPage1);
                break;

            case R.id.helpItem:
//                Toast.makeText(this, "❤️Go To Saved Music\n\uD83D\uDCE5Save This Music\n\uD83D\uDDD1️Delete This Music", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MusicActivity.this);
                builder.setMessage("❤️ Go To Saved Music\n\n\uD83D\uDCE5 Save This Musc"
                                + "\n\n\uD83D\uDDD1 ️Delete This Music\n\nFind a music from search bar")
                        .setTitle("How to use me: ")
                        .setPositiveButton("OK", (dialog, cl) -> {
                        }).create().show();

                break;

            case R.id.aboutMusic:
                Toast.makeText(this, "MusicRover created by Zhicheng He", Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }
        return true;
    }


}