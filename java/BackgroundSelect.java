package com.example.mycseapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BackgroundSelect extends AppCompatActivity {
    Context context = this ;
    DatabaseReference Background = FirebaseDatabase.getInstance().getReference()
            .child("User2").child(MainActivity.Username).child("Background") ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_select);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        final ArrayList<String> U = new ArrayList<>() ;
        U.add("Set as groups Background") ;
        U.add("Set as events Bacground")  ;
        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.memberlist, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                ListView listView2 = (ListView) promptsView.findViewById(R.id.list_view);
                final ArrayAdapter<String> adapter1 =
                        new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, U);
                listView2.setAdapter(adapter1);
                final ArrayList<String> SelectedUser = new ArrayList<>() ;
                listView2.setOnItemClickListener(new  AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        String item = Integer.toString(position);
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
                                        String done = "" ;
                                        for(int k = 0 ; k < SelectedUser.size() ; k++)
                                        {
                                            done = done + SelectedUser.get(k) + " " ;

                                            if(SelectedUser.get(k).equals("0"))
                                            {
                                             Background.child("g").setValue(i) ;
                                            }
                                            else
                                            {
                                                Background.child("e").setValue(i) ;
                                            }
                                        }
                                        Toast.makeText(getApplicationContext(), done , Toast.LENGTH_LONG).show(); ;

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
            }
        });
    }
}
