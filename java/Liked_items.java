package com.example.mycseapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Liked_items extends AppCompatActivity {
    private DatabaseReference mDatabase  ;
    private ArrayList<Eventclass> A = new ArrayList<>() ;
    ListView set ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_items);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events") ;
        DatabaseReference req = FirebaseDatabase.getInstance().getReference()
                .child("Users2") ;
        req.child(MainActivity.Username).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       Download(dataSnapshot.getKey() , 0);
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
    }
    public void Download(final String S , final int x ) {


        //Toast.makeText(getApplicationContext(), "OK ", Toast.LENGTH_LONG).show();

        final Eventclass r = new Eventclass();
        mDatabase.child(S).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String til = dataSnapshot.child("title").getValue().toString();
                String info = dataSnapshot.child("By").getValue().toString();
                String desc = dataSnapshot.child("text").getValue().toString();
                String type = dataSnapshot.child("type").getValue().toString();
                r.type = type;
                r.s = til;
                r.info = info;
                r.des = desc;
                r.id = S;
                if (x == 1) {
                    if (A.size() == 0) {
                        A.add(r);
                    } else
                        A.add(0, r);
                }
                if (x == 0) {
                    A.add(r);
                }
                final EventAdapter itemsAdapter =
                        new EventAdapter(getApplicationContext(), A);
                set = findViewById(R.id.interested) ;
                set.setAdapter(itemsAdapter);
                itemsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


