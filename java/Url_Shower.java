package com.example.mycseapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.mycseapp.MainActivity.Username;

public class Url_Shower extends AppCompatActivity {
    Context con = this ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url__shower);
        String groupname = "Ragnarok" ;
        DatabaseReference urlbase = FirebaseDatabase.getInstance().getReference().child(groupname).child("Urls");
        final ArrayList<String> group = new ArrayList<>() ;
        final ArrayAdapter<String> for_group = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, group) ;
        final ListView Gs = findViewById(R.id.url_list) ;
        Gs.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position , long id){
                String item = adapter.getItemAtPosition(position).toString();
                Intent intent = new Intent(con,ChatActivity.class);
                String[] A = {item, Username} ;
                //based on item add info to intent
                intent.putExtra(EXTRA_MESSAGE, A) ;
                startActivity(intent);
            }
        });
        Gs.setAdapter(for_group);
        urlbase.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String g = dataSnapshot.getKey() ;
                        group.add(g) ;
                        for_group.notifyDataSetChanged();
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
                }
        ) ;
    }
}
