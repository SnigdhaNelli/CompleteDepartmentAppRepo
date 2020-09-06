package com.example.mycseapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ChatActivity extends AppCompatActivity {
    DatabaseReference forUsers, ftUsers , forUrls;
    private com.firebase.ui.database.FirebaseListAdapter<String> adap;
    private DatabaseReference mDatabase;

    String Username;
    String[] S ;
    Context context = this ;
    String number ;
    final ArrayList<String> U = new ArrayList<>();
    ArrayList<String> tU = new ArrayList<>() ;
    //////////////get background



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Intent i = getIntent();
        S = i.getStringArrayExtra(EXTRA_MESSAGE);
        String token = FirebaseInstanceId.getInstance().getToken();
    /////setBackground
        FirebaseDatabase.getInstance().getReference().child("User2")
                .child(MainActivity.Username).child("Background").child("g").addValueEventListener(new ValueEventListener() {
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

        //set group name
        final TextView t = findViewById(R.id.textView3);
        t.setText(S[0]);

        //set user name
        Username = S[1];


        final ArrayList<Item> h = new ArrayList<>();
        final chatadapter itemsAdapter =
                new chatadapter(this, h);
        final ListView set = (ListView) findViewById(R.id.mobile_list);

        final EditText get = findViewById(R.id.E2);
        get.requestFocus();
        get.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (m != null) {
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                    get.requestFocus();
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.argb(255,0,0,200));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = get.getText().toString();
                if( message.length() != 0) {
                    Map<String, String> A = new HashMap<String, String>();
                    A.put(Username, message);
                    int n = Integer.parseInt(number);
                    n++;
                    number = Integer.toString(n);
                    mDatabase.child(number).setValue(A);
                    mDatabase.child("Number").child("number").setValue(number);
                    get.setText("");
                }
                // FAB Action goes here
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Group_Chat").child(S[0]);
        mDatabase.child("Number").child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        set.setAdapter(itemsAdapter);
        /// connect to chat ;
        mDatabase.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String message = dataSnapshot.getValue().toString();
                        String name = dataSnapshot.getKey();
                        if (!(name.equals("Number"))) {
                            ArrayList<String> fm = getinfo(message);
                            Item n = new Item(fm.get(0), fm.get(1));
                            h.add(n);
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
                }
        );
        /// get users ;
        ftUsers = FirebaseDatabase.getInstance().getReference().child("Groups").child(S[0]);
        ftUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.getKey();
                tU.add(name);
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
        forUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        forUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = dataSnapshot.getKey();
                U.add(name);
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




    }




    static ArrayList<String> getinfo(String S) {
        String name = "";
        S = S.substring(1);
        while (!((S.substring(0, 1)).equals("="))) {
            name = name + S.substring(0, 1);
            S = S.substring(1);
        }
        int j = S.length();
        String M = S.substring(1, j - 1);
        ArrayList<String> m = new ArrayList<>();
        m.add(name);
        m.add(M);
        return m;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity3_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        final TextView t = (TextView) findViewById(R.id.textView);
        switch (item.getItemId()) {
            case R.id.new_member:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.memberlist, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
            {
                for (int j = 0; j < U.size(); j++) {
                    if (tU.contains(U.get(j))) {
                        U.remove(j);
                    }

                }
            }
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                ListView listView2 = (ListView) promptsView.findViewById(R.id.list_view);
                final ArrayAdapter<String> adapter1 =
                        new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, U);
                listView2.setAdapter(adapter1);
               final  ArrayList<String> SelectedUser = new ArrayList<>() ;
                listView2.setOnItemClickListener(new  AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        String item = adapter.getItemAtPosition(position).toString();
                        if(SelectedUser.contains(item))
                        {
                            v.setBackgroundColor(0xFFFFFF);
                            SelectedUser.remove(item) ;
                        }
                        else
                        {
                            v.setBackgroundColor(0xFF00FF00);
                            SelectedUser.add(item);
                        }
                                                     }
                                                 });
            //     set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                    for(int i = 0 ; i < SelectedUser.size(); i++)
                                    {
                                        String Us = SelectedUser.get(i) ;
                                        U.remove(Us) ;
                                        forUsers.child(Us).child(S[0]).setValue("true");
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Groups").child(S[0]).child(Us).setValue("true") ;
                                    }
                                    adapter1.notifyDataSetChanged();
                                    SelectedUser.clear(); ;
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                //
                return true;
            case R.id.users:
                LayoutInflater li2 = LayoutInflater.from(context);
                View promptsView2 = li2.inflate(R.layout.memberlist, null);

                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder2.setView(promptsView2);
                ListView listView3 = (ListView) promptsView2.findViewById(R.id.list_view);
                ArrayAdapter<String> adapter2 =
                        new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, tU);
                listView3.setAdapter(adapter2);
                final  ArrayList<String> SelectedUser2 = new ArrayList<>() ;
                //     set dialog message
                alertDialogBuilder2
                        .setCancelable(false)

                        .setNegativeButton("Back",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog2 = alertDialogBuilder2.create();

                // show it
                alertDialog2.show();
                return true ;
//            case R.id.Add_Url:
//                LayoutInflater li3 = LayoutInflater.from(getApplicationContext());
//                View promptsView3 = li3.inflate(R.layout.newgroup, null);
//
//                AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(
//                        getApplicationContext());
//                // set prompts.xml to alertdialog builder
//                alertDialogBuilder3.setView(promptsView3);
//
//                final EditText userInput = (EditText) promptsView3
//                        .findViewById(R.id.addurl);
//                ListView listView4 = (ListView) promptsView3.findViewById(R.id.list_view);
//                ArrayAdapter<String> adapter3 =
//                        new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, tU);
//                listView4.setAdapter(adapter3);
//                // set dialog message
//                alertDialogBuilder3
//                        .setCancelable(false)
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        String N = userInput.getText().toString();
//                                        //// add group to user
//                                        groupbase.child(N).setValue("true");
//                                        ///// add user to the group.
//                                        FirebaseDatabase.getInstance().getReference().
//                                                child("Groups").child(N).child(Username).setValue("true") ;
//                                        //// add number to group.
//                                        FirebaseDatabase.getInstance().getReference().
//                                                child("Group_Chat").child(N).child("Number").child("number").setValue("0");
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                // create alert dialog
//                AlertDialog alertDialog3 = alertDialogBuilder3.create();
//                // show it
//                alertDialog3.show();
//
//                return  true ;
            case R.id.back :

                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public static void sendPushToSingleInstance(final Context activity, final HashMap dataValue /*your data from the activity*/, final String instanceIdToken /*firebase instance token you will find in documentation that how to get this*/ ) {


        final String url = "https://fcm.googleapis.com/fcm/send";
        StringRequest myReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(activity, "Bingo Success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Oops error", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                Map<String, Object> rawParameters = new Hashtable();

                rawParameters.put("notification", new JSONObject(dataValue));
                rawParameters.put("to", instanceIdToken);
                return new JSONObject(rawParameters).toString().getBytes();

            };

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key="+"" + "AIzaSyCeMe4YK7cySHxQOSSmqa4-73Df8TkIdjs");
                return headers;
            }

        };

        Volley.newRequestQueue(activity).add(myReq);
    }
}

//
//    Button send = findViewById(R.id.button3);
//        send.setOnClickListener(new android.view.View.OnClickListener() {
//public void onClick(View v) {
//        String message = get.getText().toString();
//        Map<String, String> A = new HashMap<String, String>();
//        A.put(Username, message);
//        int n = Integer.parseInt(number);
//        n++;
//        number = Integer.toString(n);
//        mDatabase.child(number).setValue(A);
//        mDatabase.child("Number").child("number").setValue(number);
//        get.setText("");
//        }
//        });
