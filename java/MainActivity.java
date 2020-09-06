package com.example.mycseapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static DatabaseReference mData ;
    Context con = this ;
    public static String Username ="" ;
    public static String enumber = "" ;
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
                if(isNetworkAvailable()) {
                    signIn(e.getText().toString(), p.getText().toString());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Internet Not connected" , Toast.LENGTH_LONG).show();
                }

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
//                            final Myasynk a = new Myasynk();
//                            a.execute() ;
                            getUsername();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    // yourMethod();
                                   // gete();
                                    Intent intent = new Intent(con, Home.class);
                                    startActivity(intent);

                                }
                            }, 4000);   //



                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login Failed " , Toast.LENGTH_LONG).show();  ;

                        }


                    }
                });

    }
    public void getUsername()
    {
        String U = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("Email").child(U) ;
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Username = dataSnapshot.getValue().toString();
                getenumber(Username);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;
    }

public void gete() {
    getenumber(Username);
    Handler h = new Handler();
    h.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          Intent intent = new Intent(con, Main2Activity.class);
                          startActivity(intent);
                      }
                  } , 2000 ) ;

}
        public  void getenumber(String User)
    {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(User).child("event_no") ;
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                enumber = dataSnapshot.getValue().toString() ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
    public class Myasynk extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(final Void... params) {

            String U = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("Email").child(U);
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Username = dataSnapshot.getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return "abc";
        }

        @Override
        public void onPostExecute(final String result) {
            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
            startActivity(intent);

        }
    }
    public class Myasynk2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(final Void... params) {
            DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Username).child("event_no") ;
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    enumber = dataSnapshot.getValue().toString() ;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }) ;
           return null ;
        }
        @Override
        public void onPostExecute(final Void result)
        {

            Intent intent = new Intent(getApplicationContext(), Main2Activity.class) ;
            startActivity(intent);

        }


    }
}

