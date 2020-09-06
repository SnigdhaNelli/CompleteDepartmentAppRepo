package com.example.mycseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class Events extends Fragment {
    View rootView ;
    StorageReference mStorageRef;
    static String des ;
    static String title ;
    ListView set ;
    private DatabaseReference mDatabase  ;
    String number , enumber , Username;
    int i  = 0 ;
    private static final int GALLERY_INTENT = 1;
    private ArrayList<Eventclass> A = new ArrayList<>() ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        A.clear();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events") ;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        rootView = inflater.inflate(R.layout.events, container, false);
        Username = MainActivity.Username ;
        enumber = MainActivity.enumber ;
        set = rootView.findViewById(R.id.eventview) ;
        set.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Eventclass name  = (Eventclass) adapterView.getItemAtPosition(i) ;
                Intent ia = new Intent(getActivity(), Event_desc.class) ;
                String n = name.id ;
                des = name.des ;
                title = name.s ;
                ia.putExtra(EXTRA_MESSAGE , n) ;
                startActivity(ia);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( getActivity() , Event_posting.class) ;
                startActivity(i);


            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            DownloadMore();


            }
        });
        FirebaseDatabase.getInstance().getReference().child("User2")
                .child(MainActivity.Username).child("Background").child("e").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RelativeLayout td = rootView.findViewById(R.id.groupb) ;
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
        /// get number
        mDatabase.child("Number").child("number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number = dataSnapshot.getValue().toString() ;
                i = Integer.parseInt(number) ;
                DownloadNew() ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); ;

        return rootView;
    }
    public void DownloadMore()
    {
        for(int k = 0 ; k < 5 ; k++)
        {
            while(i > 0) {
                Download(Integer.toString(i) , 0);
                i--;
            }
        }
    }
    public void DownloadNew()
    {
        int  i = Integer.parseInt(MainActivity.enumber) ;
        int ta = Integer.parseInt(number) ;
        while(  i < ta )
        {
            i++  ;
            Download(Integer.toString(i) , 1);
        }
       MainActivity.enumber = Integer.toString(i) ;
        FirebaseDatabase.getInstance().getReference().child("Users").child(Username)
                .child("event_no").setValue(MainActivity.enumber) ;

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
                           new EventAdapter(getContext(), A);

                   set.setAdapter(itemsAdapter);
                   itemsAdapter.notifyDataSetChanged();

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
       }
}
//  @Override Events upload preliminary.
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && null != data) {
//            final Uri selectedImage = data.getData();
//
//            ////////////////////////////////
//
//
//            StorageReference filepath = mStorageRef.child("photos").child("123");
//            filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
//                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                    mDatabase.push().setValue(selectedImage.getLastPathSegment());
//                }}) ;
//            /////////////////////////////////////////
//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//
//            Bitmap N = BitmapFactory.decodeFile(picturePath);
//            A.add(Bitmap.createScaledBitmap(N , 120, 120 , false)) ;
//            final EventAdapter itemsAdapter =
//                    new EventAdapter(getContext(), A);
//            final ListView set = (ListView) rootView.findViewById(R.id.listview4
//            );
//            set.setAdapter(itemsAdapter);
//            itemsAdapter.notifyDataSetChanged();
//        }
//        else  super.onActivityResult(requestCode, resultCode, data) ;
//    }




