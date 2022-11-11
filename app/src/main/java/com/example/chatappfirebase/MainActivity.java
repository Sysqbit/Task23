package com.example.chatappfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.chatappfirebase.messages.MessagesAdapter;
import com.example.chatappfirebase.messages.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private final List<MessagesList> messagesLists = new ArrayList<>();
    private String name;
    private String email;
    private String mobile;
    private int unseenMessages = 0;
    private String lastMessage = "";
    private String chatKey ="";

    private boolean dataSet= false;
    private RecyclerView messageRecylerView;
    private MessagesAdapter messagesAdapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatappfirebase-522c2-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CircleImageView userProfilePic = findViewById(R.id.userProfilepic);
        messageRecylerView = findViewById(R.id.messagesRecyclerView);

        // getting data from register activity
        mobile = getIntent().getStringExtra("mobile");
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");

        messageRecylerView.setHasFixedSize(true);
        messageRecylerView.setLayoutManager(new LinearLayoutManager(this));


        messagesAdapter = new MessagesAdapter(messagesLists,MainActivity.this);
        messageRecylerView.setAdapter(messagesAdapter);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();




        //getting profile pic firebase

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String profilePicUrl = snapshot.child("users").child(mobile).child("profile_pic").getValue(String.class);
/*
                if (!profilePicUrl.isEmpty()){
                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                }*/

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKey="";


                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){
                    final String getMobile = dataSnapshot.getKey();

                    dataSet=false;

                    if (getMobile.equals(mobile)){
                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);



                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCounts= (int)snapshot.getChildrenCount();

                                if (getChatCounts>0){
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            if ((getUserOne.equals(getMobile) && getUserTwo.equals(mobile)) || (getUserOne.equals(mobile)&& getUserTwo.equals(getMobile))){
                                                for (DataSnapshot chatDataSnapshot : dataSnapshot.child("messages").getChildren()){

                                                    final Long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final Long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(MainActivity.this,getKey));
                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessage){
                                                        unseenMessages++;

                                                    }

                                                }

                                            }

                                        }

                                    }
                                }
                                if (!dataSet){
                                    dataSet=true;
                                    MessagesList messagesList = new MessagesList(getName,getMobile,lastMessage,getProfilePic,unseenMessages,chatKey);
                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}