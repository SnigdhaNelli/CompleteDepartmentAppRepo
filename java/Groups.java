package com.example.mycseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by sri on 10/14/17.
 */

public class Groups extends Fragment {
    View rootView ;
    private DatabaseReference groupbase , Duser  ;

    private String Username ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.groups, container, false);
        setHasOptionsMenu(true);
        ListView l = rootView.findViewById(R.id.mobile_list) ;
        TextView G = rootView.findViewById(R.id.Grouptext) ;
        G.setText("Your Groups");
       //getUsername();
       Username = MainActivity.Username;
         //////
        groupbase = FirebaseDatabase.getInstance().getReference().child("Users").child(Username) ;
        final ArrayList<String> group = new ArrayList<>() ;
        final ArrayAdapter<String> for_group = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, group) ;
        final ListView Gs = rootView.findViewById(R.id.mobile_list2) ;
        Gs.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position , long id){
                String item = adapter.getItemAtPosition(position).toString();
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                String[] A = {item, Username} ;
                //based on item add info to intent
                intent.putExtra(EXTRA_MESSAGE, A) ;
                startActivity(intent);
            }
        });
        Gs.setAdapter(for_group);
        groupbase.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(!(dataSnapshot.getKey().equals("event_no"))) {
                            String g = dataSnapshot.getKey();
                            group.add(g);
                            for_group.notifyDataSetChanged();
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
        ) ;

        return rootView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        final TextView t = (TextView) rootView.findViewById(R.id.textView);
        switch (item.getItemId()) {
            case R.id.New: {
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.newgroup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String N = userInput.getText().toString();
                                        //// add group to user
                                        groupbase.child(N).setValue("true");
                                        ///// add user to the group.
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Groups").child(N).child(Username).setValue("true") ;
                                        //// add number to group.
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Group_Chat").child(N).child("Number").child("number").setValue("0");
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
            return true;
            case R.id.help:
                t.setText("help");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
        //respond to menu item selection

    }


}
