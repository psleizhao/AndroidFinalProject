package algonquin.cst2335.androidfinalproject.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivityMusicBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchMusicBinding;


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

        musicModel = new ViewModelProvider(this).get(MusicViewModel.class);
        music1 = musicModel.musics.getValue();

        MusicDatabase db = Room.databaseBuilder(getApplicationContext(),MusicDatabase.class, "musicdb").build();
        mDAO = db.musicDAO();

        if(music1 == null) {
            musicModel.musics.postValue(music1 = new ArrayList<Music>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                music1.addAll(mDAO.getAllMusics()); //Once you get the data from database

                runOnUiThread(() -> binding.musicRecycleView.setAdapter(musicAdapter)); //You can then load the RecyclerView
            });
        }

        binding.musicSearchButton.setOnClickListener(clk ->{
            String musicTextInput = binding.musicTextInput.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("musicName", musicTextInput);
            editor.apply();
            String musicName = "In the end";
            String imgUrl = "";
            String summary = "Linkin Park";
            String srcUrl = "";

            Music m = new Music(musicName, imgUrl, summary, srcUrl);
            music1.add(m);

            binding.musicTextInput.setText("");

            musicAdapter.notifyDataSetChanged();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                m.id = mDAO.insertMusic(m); //Once you get the data from database
            });

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
                Music obj = music1.get(position);

                holder.musicName.setText(obj.getTitle()+position);
                //             holder.musicIcon.setImageURI(Uri.parse((obj.getImgUrl())));
            }

            @Override
            public int getItemCount() {
                return music1.size();
            }
        });

        binding.musicRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView musicName;
        public ImageView musicIcon;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(
                    clk -> {

                        int position = getAbsoluteAdapterPosition();
                        Music toDelete = music1.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MusicActivity.this);
                        builder.setMessage("Do you want to delete the music of " + musicName.getText())
                                .setTitle("Question: ")
                                .setPositiveButton("Yes", (dialog, cl) -> {
                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() ->
                                    {
                                        mDAO.deleteMusic(toDelete);
                                    });

                                    music1.remove(position);
                                    musicAdapter.notifyDataSetChanged();

                                    Snackbar.make(itemView, "You deleted music #" + (position+1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click ->{
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread.execute(() ->
                                                {
                                                    mDAO.insertMusic(toDelete);
                                                });
                                                music1.add(position, toDelete);
                                                musicAdapter.notifyDataSetChanged();
                                                runOnUiThread(() ->
                                                        Toast.makeText(MusicActivity.this, "This Song add back to your list", Toast.LENGTH_SHORT).show()
                                                );
                                            })
                                            .show();
                                })
                                .setNegativeButton("No", (dialog, cl) -> {
                                })
                                .create().show();


                    });


            musicName = itemView.findViewById(R.id.musicResult);
            musicIcon = itemView.findViewById(R.id.musicIcon);
        }

    }
}