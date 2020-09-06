package com.example.mycseapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Event_desc extends AppCompatActivity {
     ArrayList<Item> comments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_desc);
        Intent i = getIntent() ;
        final String id = i.getStringExtra(EXTRA_MESSAGE) ;
        TextView t = findViewById(R.id.des) ;
        TextView t2 = findViewById(R.id.imported) ;

        t2.setText(Events.title);
        t.setText(Events.des);
        ListView com = findViewById(R.id.comments) ;
        final DatabaseReference mEvent = FirebaseDatabase.getInstance().getReference().child("Events").child(id) ;
        final DatabaseReference mComment = FirebaseDatabase.getInstance().getReference().child("Eventcom").child(id) ;
        FirebaseDatabase.getInstance().getReference().child("User2")
                .child(MainActivity.Username).child("Background").child("e").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConstraintLayout td = findViewById(R.id.groupb) ;
                if(dataSnapshot.getValue().toString().equals("1"))
                {
                    td.setBackgroundResource(R.drawable.group);
                }
                else
                {
                    td.setBackgroundResource(R.drawable.lk);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //////////////////Like button

        final LikeButton likeButton = findViewById(R.id.likeButton) ;
        final DatabaseReference Ie = FirebaseDatabase.getInstance().getReference().child("Users2")
                .child(MainActivity.Username).child(id) ;
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                    Ie.setValue(Events.title) ;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
              Ie.removeValue() ;
            }
        });
        //////////
        ///////// SET LIKE button
        DatabaseReference req = FirebaseDatabase.getInstance().getReference()
                .child("Users2").child(MainActivity.Username) ;
        req.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(id))
                {
                    likeButton.setLiked(true);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;

        /////////////////////////////////
        final EditText s = findViewById(R.id.comment) ;
        /////////set the post button
        FloatingActionButton post = findViewById(R.id.postcomment) ;
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mes = s.getText().toString() ;
                s.setText("");
               mComment.child("com").push().child(MainActivity.Username).setValue(mes) ;
            }
        });
        ////////////////////////
        /////////////////////Comments
        comments.clear();
        final chatadapter itemsAdapter =
                new chatadapter(this, comments);
        mComment.child("com").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String name = dataSnapshot.getKey();
                if (!(name.equals("Number"))) {
                    ArrayList<String> Comm = ChatActivity.getinfo(dataSnapshot.getValue().toString()) ;
                    Item n = new Item(Comm.get(0), Comm.get(1));
                    comments.add(n);
                    itemsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    com.setAdapter(itemsAdapter);
        try{
        StorageReference dwn = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mycseapp.appspot.com/Events" ).child(id);
        final File localFile = File.createTempFile("images", "jpg");
        dwn.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_LONG).show();

                        final Bitmap myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        mEvent.child("text").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                               String des = dataSnapshot.getValue().toString() ;
                                ImageView show = findViewById(R.id.Show) ;
                                show.setImageBitmap(myBitmap);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }) ;



                        // Successfully downloaded data to local file
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Download Failed", Toast.LENGTH_LONG).show();


                // Handle failed download
                // ...
            }
        });
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
}
