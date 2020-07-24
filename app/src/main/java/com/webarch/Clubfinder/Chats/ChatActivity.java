package com.webarch.Clubfinder.Chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.webarch.Clubfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<ChatViewHolder> mChatAdapter;
    private RecyclerView.LayoutManager mChatlayoutManager;
    private EditText mSendEditText;
    private Button mSendButton;
    private String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private String matchId,chatId;
    DatabaseReference mDatabaseUser,mDatabaseChats;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId = Objects.requireNonNull(getIntent().getExtras()).getString("matchId");


        mDatabaseChats = FirebaseDatabase.getInstance().getReference().child("Chat");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchId).child("ChatId");

        getChatId();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mChatlayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatlayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);
        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }


        });
    }
        private void sendMessage() {
         String sendMessageText = mSendEditText.getText().toString();
         if(!sendMessageText.isEmpty()){
             DatabaseReference newMessageDb = mDatabaseChats.push();
             Map<String, String> newMessage =new HashMap<String, String>();
             newMessage.put("createByUser",currentUserId);
             newMessage.put("text",sendMessageText);
             newMessageDb.setValue(newMessage);
         }
         mSendEditText.setText(null);
        }

        private void getChatId()
        {
            mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        chatId = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        mDatabaseChats=mDatabaseChats.child(chatId);
                        mgetChatMessages();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    private void mgetChatMessages() {
        mDatabaseChats.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String createdByUser= null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message=dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createByUser").getValue()!=null){
                        createdByUser=dataSnapshot.child("createByUser").getValue().toString();
                    }
                    if(message!=null&&createdByUser!=null)
                    {
                      Boolean currentUserBoolean = false;
                      if(createdByUser.equals(currentUserId))
                      {
                          currentUserBoolean=true;
                      }
                      ChatObject newMessage = new ChatObject(message,currentUserBoolean);
                      resultsChat.add(newMessage);
                      mChatAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();
    private List<ChatObject> getDataSetChat() {
        return resultsChat;
    }

}
