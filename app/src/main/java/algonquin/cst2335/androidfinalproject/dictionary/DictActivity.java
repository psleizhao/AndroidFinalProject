package algonquin.cst2335.androidfinalproject.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivityDictBinding;
import algonquin.cst2335.androidfinalproject.databinding.SearchDictBinding;

public class DictActivity extends AppCompatActivity {
    ActivityDictBinding binding;

    ArrayList<Dict> dicts = null;
    DictViewModel dictModel;

    private RecyclerView.Adapter dictAdapter;

    DictDAO dDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDictBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dictModel = new ViewModelProvider(this).get(DictViewModel.class);
        dicts = dictModel.Dicts.getValue();

        DictDatabase db = Room.databaseBuilder(getApplicationContext(),DictDatabase.class, "dictdb").build();
        dDAO = db.DictDAO();

        if(dicts == null) {
            dictModel.Dicts.postValue(dicts = new ArrayList<Dict>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                dicts.addAll(dDAO.getAllDicts()); //Once you get the data from database

                runOnUiThread(() -> binding.dictRecycleView.setAdapter(dictAdapter)); //You can then load the RecyclerView
            });
        }

        binding.dictSearchButton.setOnClickListener(clk ->{

            String dictName = "Fabulous";
            String imgUrl = "";
            String summary = "Wonderful";
            String srcUrl = "";

            Dict d = new Dict(dictName, imgUrl, summary, srcUrl);
            dicts.add(d);

            binding.dictTextInput.setText("");

            dictAdapter.notifyDataSetChanged();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                d.id = dDAO.insertDict(d); //Once you get the data from database
            });

        });

        binding.dictRecycleView.setAdapter(dictAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SearchDictBinding binding = SearchDictBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull DictActivity.MyRowHolder holder, int position) {
                Dict obj = dicts.get(position);

                holder.dictName.setText(obj.getDictName()+position);
                //             holder.recipeIcon.setImageURI(Uri.parse((obj.getImgUrl())));
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
                                    thread.execute(() ->
                                    {
                                        dDAO.deleteDict(toDelete);
                                    });

                                    dicts.remove(position);
                                    dictAdapter.notifyDataSetChanged();

                                    Snackbar.make(itemView, "You deleted word #" + (position+1), Snackbar.LENGTH_LONG)
                                            .setAction("Undo", click ->{
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread.execute(() ->
                                                {
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