package com.example.mycseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mData ;
    private String Username  ;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button r = (Button) findViewById(R.id.authentify);
        mAuth = FirebaseAuth.getInstance() ;



        r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText e = (EditText) findViewById(R.id.E);
                final EditText p = (EditText) findViewById(R.id.P);

                    signIn(e.getText().toString() , p.getText().toString());

            }
        });



    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;        }

        // showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final EditText e = (EditText) findViewById(R.id.E);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUsername() ;
                            Intent intent = new Intent(getApplicationContext(), Main2Activity.class) ;
                            intent.putExtra(EXTRA_MESSAGE, Username)  ;
                            startActivity(intent);

                        } else {
                            final TextView t = (TextView) findViewById(R.id.notify);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            t.setText("Login Failed") ;

                        }


                    }
                });

    }
    public void getUsername()
    {
        String U = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        mData = FirebaseDatabase.getInstance().getReference().child("Email").child(U) ;
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Username = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;
    }
    private boolean validateForm() {
        boolean valid = true;
        final EditText e = (EditText) findViewById(R.id.E);
        final EditText p = (EditText) findViewById(R.id.P);
        String email = e.getText().toString();
        if (TextUtils.isEmpty(email)) {
            e.setError("Required.");
            valid = false;
        } else {
            e.setError(null);
        }

        String password = p.getText().toString();
        if (TextUtils.isEmpty(password)) {
            p.setError("Required.");
            valid = false;
        } else {
            p.setError(null);
        }

        return valid;
    }
}
