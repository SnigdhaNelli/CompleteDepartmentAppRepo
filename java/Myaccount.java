package com.example.mycseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by sri on 10/14/17.
 */

public class Myaccount extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myaccount, container, false);
        ListView set = rootView.findViewById(R.id.my_account) ;
        final ArrayList<String> myacc = new ArrayList<>() ;
        myacc.add("Change User name") ;
        myacc.add("Change Background") ;
        myacc.add("Interested Items") ;
        final ArrayAdapter<String> myaccad = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, myacc) ;
        set.setAdapter(myaccad);
        set.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String name = (String) adapterView.getItemAtPosition(i);
                                        if(name.equals("Interested Items"))
                                        {
                                            Intent trans = new Intent(getActivity(), Liked_items.class) ;
                                            startActivity(trans);
                                        }
                                        if(name.equals("Change Background"))
                                        {
                                            Intent go = new Intent(getActivity(), BackgroundSelect.class) ;
                                            startActivity(go);
                                        }
                                    }
                                }
        );

        return rootView;
    }
}
