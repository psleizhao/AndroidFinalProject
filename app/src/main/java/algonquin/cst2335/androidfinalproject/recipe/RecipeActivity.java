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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidfinalproject.R;
import algonquin.cst2335.androidfinalproject.databinding.ActivityRecipeBinding;
import algonquin.cst2335.androidfinalproject.databinding.ReceiveMessageBinding;
import algonquin.cst2335.androidfinalproject.recipe.data.RecipeViewModel;
import algonquin.cst2335.androidfinalproject.databinding.SentMessageBinding;

public class RecipeActivity extends AppCompatActivity {

    ActivityRecipeBinding binding;
    ArrayList<ChatMessage> messages = null;

    RecipeViewModel recipeModel;
    private RecyclerView.Adapter myAdapter;

    ChatMessageDAO mDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipeModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        messages = recipeModel.messages.getValue();

        recipeModel.selectedMessage.observe(this, (selectedMessage) -> {

            if(selectedMessage != null) {
                MessageDetailsFragment newMessage = new MessageDetailsFragment(selectedMessage);

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction transaction = fMgr.beginTransaction();
                transaction.addToBackStack("any string here");
                transaction.replace(R.id.fragmentLocation, newMessage); //first is the FrameLayout id
                transaction.commit();//loads it

            }
        });

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        if (messages == null) {
            recipeModel.messages.postValue(messages = new ArrayList<ChatMessage>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll(mDAO.getAllMessages()); //Once you get the data from database

                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter)); //You can then load the RecyclerView
            });
        }

        binding.sendButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            String inputMessage = binding.textInput.getText().toString();
            boolean sentButton = true;

            ChatMessage m = new ChatMessage(inputMessage, currentDateandTime, sentButton);
            messages.add(m);

            // clear teh previous text
            binding.textInput.setText("");

            myAdapter.notifyDataSetChanged();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                m.id = mDAO.insertMessage(m); //Once you get the data from database
                Log.d("TAG", "The id created is" + m.id);
            });

        });


        binding.receiveButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            String inputMessage = binding.textInput.getText().toString();
            boolean sentButton = false;

            ChatMessage m = new ChatMessage(inputMessage, currentDateAndTime, sentButton);
            messages.add(m);

            myAdapter.notifyDataSetChanged();

            // clear teh previous text
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                m.id = mDAO.insertMessage(m); //Once you get the data from database
                Log.d("TAG", "The id created is" + m.id);
            });

        });

        // will draw the recycle view.
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @Override
            public int getItemViewType(int position) {
                // determine which layout to load at row position
                if (messages.get(position).isSentButton() == true) // for the first 5 rows
                {
                    return 0;
                } else return 1;
            }

            @NonNull
            @Override                                                       // which layout to load?
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // viewType will either be 0 or 1

                if (viewType == 0) {
                   //  1. load a XML layout
                    SentMessageBinding binding =                            // parent is incase matchparent
                            SentMessageBinding.inflate(getLayoutInflater(), parent, false);

                    // 2. call our constructor below
                    return new MyRowHolder(binding.getRoot()); // getRoot returns a ConstraintLayout with TextViews inside
                }
                else {
                    // 1. load a XML layout
                    ReceiveMessageBinding binding =                            // parent is incase matchparent
                            ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);

                    // 2. call our constructor below
                    return new MyRowHolder(binding.getRoot()); // getRoot returns a ConstraintLayout with TextViews inside

                }
            }


            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);

                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }


    class MyRowHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(
                    clk -> {

                        int position = getAbsoluteAdapterPosition();
                        ChatMessage selected = messages.get(position);


                        recipeModel.selectedMessage.postValue(selected);

//
//                        int position = getAbsoluteAdapterPosition();
//                        ChatMessage toDelete = messages.get(position);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
//                        builder.setMessage("Do you want to delete the message: " + messageText.getText())
//                                .setTitle("Question: ")
//                                .setPositiveButton("Yes", (dialog, cl) -> {
//                                    Executor thread = Executors.newSingleThreadExecutor();
//                                    thread.execute(() ->
//                                    {
//                                        mDAO.deleteMessage(toDelete);
//                                    });
//
//                                    messages.remove(position);
//                                    myAdapter.notifyDataSetChanged();
//
//                                    Snackbar.make(itemView, "You deleted message #" + (position+1), Snackbar.LENGTH_LONG)
//                                            .setAction("Undo", click ->{
//                                                Executor thread1 = Executors.newSingleThreadExecutor();
//                                                thread.execute(() ->
//                                                {
//                                                    mDAO.insertMessage(toDelete);
//                                                });
//                                                messages.add(position, toDelete);
//                                                myAdapter.notifyDataSetChanged();
//                                            })
//                                            .show();
//                                })
//                                .setNegativeButton("No", (dialog, cl) -> {
//                                })
//                                .create().show();


                    });

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}