package com.hextech.trainingsignalapp.ui.chatroom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hextech.trainingsignalapp.R;
import com.hextech.trainingsignalapp.util.ChatMessage;
import com.hextech.trainingsignalapp.util.GenericAdapter;
import com.hextech.trainingsignalapp.util.NetworkAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoomFragment extends Fragment {

    Button sendMessageButton;
    EditText chatMessageEditText;
    RecyclerView chatsRecyclerView;
    GenericAdapter adapter;
    ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    boolean isUpdated = false;

    private FirebaseAuth mAuth;

    public ChatRoomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_chat_room, container, false);

        chatsRecyclerView = root.findViewById(R.id.chatRecyclerView);
        sendMessageButton = root.findViewById(R.id.sendMessageButton);
        chatMessageEditText = root.findViewById(R.id.chatMessageEditText);

        mAuth = FirebaseAuth.getInstance();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chatMessageEditText.getText().toString();

                if (message != null && !message.equals("")) {
                    String author = mAuth.getCurrentUser().getDisplayName();

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();

                    ChatMessage chatMessage = new ChatMessage(author, message, formatter.format(date));

                    NetworkAdapter.sendChatMessage(chatMessage);

                    chatMessageEditText.setText("");
                }
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatroom");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);

                    if (!chatMessages.contains(chatMessage)) {
                        chatMessages.add(chatMessage);
                        isUpdated = true;
                    }
                }

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isUpdated) {
                                chatsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter = new GenericAdapter<ChatMessage>(root.getContext(), chatMessages) {
                                    @Override
                                    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                                        View view = LayoutInflater.from(root.getContext()).inflate(R.layout.chatmessage_recyclerview_row, parent, false);
                                        ViewHolder viewHolder = new ViewHolder(view);
                                        return viewHolder;
                                    }

                                    @Override
                                    public void onBindData(RecyclerView.ViewHolder holder, ChatMessage item) {
                                        ((ViewHolder)holder).nameTextView.setText(item.author);
                                        ((ViewHolder)holder).messageTextView.setText(item.message);
                                        ((ViewHolder)holder).dateTextView.setText(item.dateTime);
                                    }
                                };

                                chatsRecyclerView.setAdapter(adapter);
                                chatsRecyclerView.scrollToPosition(chatMessages.size() - 1);
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(), "Error Retrieving Data!", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, messageTextView, dateTextView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.chatMessageNameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}