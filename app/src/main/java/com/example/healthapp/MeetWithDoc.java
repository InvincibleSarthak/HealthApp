package com.example.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MeetWithDoc extends AppCompatActivity {

    TextView TxtHeading,TxtLink;
    ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_with_doc);
        getZoomLink();
    }
    private void getZoomLink() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Doctor");
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Looking for doctors...");
        progressDialog.show();
        TxtHeading=findViewById(R.id.textViewHeading);
        TxtLink=findViewById(R.id.textViewZoomLink);
        // Read from the database
        Log.e("TAG", myRef.child("1").child("Name").toString());
        myRef.orderByChild("Status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e("TAG", "onDataChange: " + snapshot.child("Name").getValue()  + " " + snapshot.child("Status").getValue());
                    if(snapshot.child("Status").getValue().toString().equals("0")){
                        Log.e("TAG", "onDataChange: Found" );
                        Log.e("TAG", "LINK for Zoom: "+ snapshot.child("Zoom").getValue().toString());
                        TxtHeading.setText("Hello! Hope you are doing well Dr "+ snapshot.child("Name").getValue().toString() +" will be ready for you");
                        TxtLink.setText(snapshot.child("Zoom").getValue().toString());
                        progressDialog.dismiss();
                        break;
                    }else{
                        Log.e("TAG", "onDataChange: Not Found");
                        /* TODO: display no doctors available */
                        progressDialog.dismiss();
                        break;
                    }
                }

                Log.e("Hello", "------------------------------------");
                String value = dataSnapshot.child("1").child("Name").getValue(String.class);
                Log.e("TAG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("TAG", "Failed to read value.");
            }
        });
    }
}