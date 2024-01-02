package com.example.diary1311;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Packing extends AppCompatActivity {
    Button btn_open, btn_open2;
    ImageView img1, img2, img3;
    FirebaseDatabase database;
    String value = null;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packing2);
        btn_open = findViewById(R.id.btn_cuavao);
        btn_open2 = findViewById(R.id.btn_cuara);
        img1 = (ImageView) findViewById(R.id.img_1);
        img2 = findViewById(R.id.img_2);
        img3 = (ImageView) findViewById(R.id.img_3);

        mDatabase = database.getInstance().getReference();
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String temp = snapshot.getValue().tos
//                mDatabase.child("cong").child("congra").setValue(0);

                mDatabase.child("cong").child("congra").setValue(1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Packing.this,"Mở thành công!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(Packing.this,"Mở không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        btn_open2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("cong").child("congvao").setValue(1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Packing.this,"Mở thành công!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(Packing.this,"Mở không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String value = dataSnapshot.getValue(String.class);
                // ..
                Log.d("DEBUG", "Value is: " + value);
                if(value.equals("1"))
                {
                    Log.d("DEBUG", "ok " + value);
                    img1.setImageResource(R.drawable.a4);
                }
                else
                    img1.setImageResource(R.drawable.a3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("baixe").child("bai1").addValueEventListener(postListener);
        ValueEventListener postListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String value = dataSnapshot.getValue(String.class);
                // ..
                Log.d("DEBUG", "Value is: " + value);
                if(value.equals("1"))
                {
                    Log.d("DEBUG", "ok " + value);
                    img2.setImageResource(R.drawable.a4);
                }
                else
                    img2.setImageResource(R.drawable.a3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("baixe").child("bai2").addValueEventListener(postListener2);
        ValueEventListener postListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String value = dataSnapshot.getValue(String.class);
                // ..
                Log.d("DEBUG", "Value is: " + value);
                if(value.equals("1"))
                {
                    Log.d("DEBUG", "ok " + value);
                    img3.setImageResource(R.drawable.a4);
                }
                else
                    img3.setImageResource(R.drawable.a3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("DEBUG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("baixe").child("bai3").addValueEventListener(postListener3);



    }

}
