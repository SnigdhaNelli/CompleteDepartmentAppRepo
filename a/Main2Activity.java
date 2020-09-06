package com.example.mycseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mycseapp.MyFirebase.Main4Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Main2Activity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String Username ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent i = getIntent();
        String message = i.getStringExtra(EXTRA_MESSAGE);
        final TextView t = (TextView) findViewById(R.id.textView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final TextView j = (TextView) findViewById(R.id.textView2);
        j.setText(message);
        final DatabaseReference name1 = mDatabase.child("Email").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        name1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Username = dataSnapshot.getValue().toString();
                t.setText(Username) ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                t.setText("Sorry, Unavailable");
                // ...
            }

        });

        //String u = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final EditText newword = (EditText)findViewById(R.id.E);
        Button add = (Button) findViewById(R.id.a);
        add.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
               String r =  newword.getText().toString() ;
                name1.setValue(r) ;
            }
        });
        Button mess = (Button) findViewById(R.id.button2) ;
        mess.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main5Activity.class) ;
                intent.putExtra(EXTRA_MESSAGE,Username);
                startActivity(intent); ;

            }}) ;
        Button share = (Button) findViewById(R.id.Share) ;
        share.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main4Activity.class) ;
                startActivity(intent); ;

            }}) ;
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        final TextView t = (TextView) findViewById(R.id.textView);
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
        //respond to menu item selection

    }



}
