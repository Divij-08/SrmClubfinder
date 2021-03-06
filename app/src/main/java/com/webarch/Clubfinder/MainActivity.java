package com.webarch.Clubfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.webarch.Clubfinder.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private cards cards_data[];

    private arrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth mAuth;
    private String currentId;
    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;
    @Override
    public void onBackPressed() {

            System.exit(1);
            super.onBackPressed();


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usersDb=FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentId=mAuth.getCurrentUser().getUid();

        checkUserSex();
        rowItems = new ArrayList<>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer=findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                cards obj1=(cards) dataObject;
                String userId=obj1.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentId).setValue(true);
                Toast.makeText(MainActivity.this ,"left",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj1=(cards) dataObject;
                String userId=obj1.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentId).setValue(true);
                isConnectionMatch(userId);
                Toast.makeText(MainActivity.this,"Right",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

            }

            @Override
            public void onScroll(float scrollProgressPercent) {


            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatch(String userId) {

        DatabaseReference currentUserConnectionsDb=usersDb.child(currentId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this,"CONGRATULATIONS,ITS A MATCH!", Toast.LENGTH_LONG).show();
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(Objects.requireNonNull(dataSnapshot.getKey())).child("connections").child("matches").child(currentId).child("ChatId").setValue(key);

                    usersDb.child(currentId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String userSex;
    private String oppoUserSex;
    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("Identity").getValue()!= null) {
                        userSex = dataSnapshot.child("Identity").getValue().toString();
                        switch (userSex) {
                            case "Club":
                                oppoUserSex = "Student";
                                break;
                            case "Student":
                                oppoUserSex = "Club";
                                break;
                        }
                        getOppositeSexUsers();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getOppositeSexUsers(){

        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentId) && !dataSnapshot.child("connections").child("yeps").hasChild(currentId) && dataSnapshot.child("Identity").getValue().toString().equals(oppoUserSex)) {
                    String itmStr = "default";
                    if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                        itmStr = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }



// cick on setttings <---- there is no profileImageUrl in the firebase document  thats why its crasshing
                    if (dataSnapshot.child("profileImageUrl").getValue() == null) {
                        Log.i("Image", "No profile Image uploaded yet for user " + dataSnapshot.child("name").getValue().toString());
                        //return;
                    } else {
                        itmStr = dataSnapshot.child("profileImageUrl").getValue().toString();

                    }
                    String cardStr0 = dataSnapshot.child("name").getValue().toString();
                    cards Item = new cards(dataSnapshot.getKey(), cardStr0, itmStr,dataSnapshot.child("LinkedIn").getValue().toString(),dataSnapshot.child("Description").getValue().toString(),dataSnapshot.child("Skills").getValue().toString());
                    rowItems.add(Item);
                    arrayAdapter.notifyDataSetChanged();
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


    public void goToSettings(View view) {
        Intent intent=new Intent(MainActivity.this, Settings.class);

        startActivity(intent);
        finish();
    }

    public void goToMatches(View view) {
        Intent intent=new Intent(MainActivity.this, MatchesActivity.class);

        startActivity(intent);
        finish();
    }
}