package com.example.demo3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "\n Firebase DEMO \n";
    private TextView mTextMessage;
    private String UID,fname,lname,age;
    private String orderId, prodId,quantity,price;
    private String bld="";
    // Write a message to the database

    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myRef = FirebaseDatabase.getInstance().getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Read from the database once
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value.toString());

                DataSnapshot users = dataSnapshot.child("Users");
                DataSnapshot share = dataSnapshot.child("Share");
                Log.d(TAG, "USERS is: " + users.toString());
                Log.d(TAG, "SHARE is: " + share.toString());
                for(DataSnapshot user : users.getChildren()){
                    UID = user.getKey().toString();
                    fname = user.child("UserInfo").child("fname").getValue().toString();
                    lname = user.child("UserInfo").child("lname").getValue().toString();
                    age = user.child("UserInfo").child("age").getValue().toString();
                    Log.d(TAG, " \n UID: "+UID);
                    Log.d(TAG, "fname: "+fname);
                    Log.d(TAG, "lname: "+lname);
                    Log.d(TAG, "age: "+age);
                    bld +=  "\n\nUID: "+UID +"\nfname: "+fname + "\nlname: "+lname+"\nage: "+age;

                    // get orders per user
                    for(DataSnapshot order : user.child("Orders").getChildren()){
                        orderId = order.getKey().toString();
                        prodId =  order.child("prodID").getValue().toString();
                        price =  order.child("price").getValue().toString();
                        quantity =  order.child("quantity").getValue().toString();
                        Log.d(TAG, " \n OrderID: "+orderId);
                        Log.d(TAG, "prodID: "+prodId);
                        Log.d(TAG, "price: "+price);
                        Log.d(TAG, "quantity: "+quantity);
                        bld+= "\n";
                        bld+=  "\n OrderID: "+orderId+"\nprodID: "+prodId+"\nprice: "+price+"quantity: "+quantity+"\n";

                    }

                }
                //mTextMessage.setText("DUMP OF DATA BY USER \n \n"+ bld);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
