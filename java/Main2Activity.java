package com.example.mycseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mycseapp.MyFirebase.Main4Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        t.setText(MainActivity.enumber);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final TextView j = (TextView) findViewById(R.id.textView2);
        j.setText(message);
        final DatabaseReference name1 = mDatabase.child("Email").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Username = MainActivity.Username ;


        Button web = (Button) findViewById(R.id.button4) ;
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent track = new Intent(getApplicationContext(), Main6Activity.class);
                startActivity(track);
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
                Intent intent = new Intent(getApplicationContext(), Home.class) ;
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




}
