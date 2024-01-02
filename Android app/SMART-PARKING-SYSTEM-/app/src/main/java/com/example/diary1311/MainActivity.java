package com.example.diary1311;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diary1311.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseFirestore firestore;
    private RecyclerView rvNotes;
    private FloatingActionButton btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ABC");
        firestore = FirebaseFirestore.getInstance();
       // login("test@gmail.com","123456");
        // createNewUser("newuser@gmail.com","123456");
        //postDataToRealTimeDB("Hello");
        //readDataFromRealTimeDB();
        //postDataToFirestore();
       //addPostData(new Post("Trinh Duy Tinh", "Android with Firebase"));
       // addPostData(new Post("Test post", "Android with Firebase"));
        rvNotes = findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });

    }
    public void addNote(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.add_note, null);
        mDialog.setView(mView);
        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        Button save = mView.findViewById(R.id.btn_save);
        EditText editTitle = mView.findViewById(R.id.edt_title);
        EditText edtContent = mView.findViewById(R.id.edt_content);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = myRef.push().getKey();
                String title = editTitle.getText().toString();
                String content = edtContent.getText().toString();

                myRef.child(id).setValue(new Post(id, title, content,getRandomColor())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Add note sucessful!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Add note fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void edit(String id, String bienso, String cccd, String hoten){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.edit, null);
        mDialog.setView(mView);
        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);
        dialog.show();
        Button save = mView.findViewById(R.id.btn_save);
        EditText editBienso = mView.findViewById(R.id.edt_bienso);
        EditText edtCccd= mView.findViewById(R.id.edt_cccd);
        EditText editHoten = mView.findViewById(R.id.tv_hoten);

        editBienso.setText(bienso.toString());
        edtCccd.setText(cccd.toString());
        editHoten.setText(hoten.toString());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bienso = editBienso.getText().toString();
                String cccd = edtCccd.getText().toString();
                String hoten = editHoten.getText().toString();

                myRef.child(id).setValue(new Post(id, bienso, cccd, hoten)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Add note sucessful!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Add note fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(myRef, Post.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Post, PostHolder>(options) {
            @Override
            public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notes_items, parent, false);

                return new PostHolder(view);
            }

            @Override
            protected void onBindViewHolder(PostHolder holder, int position, Post model) {
                holder.tvBienso.setText(model.getBienso());
                holder.tvCccd.setText(model.getCccd());
                holder.tvHoten.setText(model.getHoten());
                //holder.layoutNote.setBackgroundColor();

                ImageView ivAction = holder.itemView.findViewById(R.id.iv_action);
                ivAction.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupmenu = new PopupMenu(view.getContext(),view);
                        popupmenu.setGravity(Gravity.END);
                        popupmenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                String id = model.getId();
                                String bienso = model.getBienso();
                                String cccd = model.getCccd();
                                String hoten = model.getHoten();
                                edit(id, bienso, cccd, hoten);
                                return false;
                            }
                        });
                        popupmenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                String id = model.getId();
                                myRef.child(id).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(MainActivity.this,"Delete sucessful!", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(MainActivity.this,"Deletel fail!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                return false;
                            }
                        });
                        popupmenu.show();
                    }

                });
            }
        };
        rvNotes.setAdapter(adapter);
        adapter.startListening();

    }
    public static class PostHolder extends RecyclerView.ViewHolder {
        public TextView tvBienso;
        public TextView tvCccd;
        public TextView tvHoten;
        public LinearLayout layoutNote;
        public PostHolder(View view) {
            super(view);
            tvBienso = view.findViewById(R.id.tv_bienso);
            tvCccd = view.findViewById(R.id.tv_cccd);
            tvHoten = view.findViewById(R.id.tv_hoten);
            layoutNote = view.findViewById(R.id.layout_note);


        }

    }
    private String getRandomColor(){
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#fcfcf1");
        colors.add("#818a7e");
        colors.add("##c9ccc8");
        colors.add("#b6c0b3");
        colors.add("#dee1d1");
        colors.add("#939a91");
        colors.add("#fbc068");
        colors.add("#E84522");
        colors.add("#9A4634");
        colors.add("#BE9389");
        colors.add("#84DD96");
        colors.add("#84DDCA");
        colors.add("#84B7DD");
        colors.add("#E49A3C");
        colors.add("#3153DF");
        colors.add("#91A1DF");
        colors.add("#7CCE2F");
        colors.add("#E5D8B9");
        colors.add("#86D3D2");
        Random random = new Random();
        return colors.get(random.nextInt(colors.size()));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_logout:
                mAuth.signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void login(String email, String pass)
    {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("DEBUG","Login sucessful");
                        }
                        else
                        {
                            Log.d("DEBUG","Login fail");
                        }
                    }
                });
    }
    private void createNewUser(String newUserEmail, String newUserPass)
    {
        mAuth.createUserWithEmailAndPassword(newUserEmail, newUserPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("DEBUG","creat new user sucessful");
                        }
                        else
                        {
                            Log.d("DEBUG","creat new user fail");
                        }
                    }
                });
    }
    private void resetPass(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("DEBUG","creat new user sucessful");
                        }
                        else
                        {
                            Log.d("DEBUG","creat new user fail");
                        }
                    }
                });
    }
    private void signOut(){
        mAuth.signOut();
    }
    private void postDataToRealTimeDB(String data){
        myRef.setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("DEBUG","post data "+data+" sucessful");
                        }
                        else
                        {
                            Log.d("DEBUG","post data fail");
                        }
                    }
                });
    }
    private void readDataFromRealTimeDB(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = snapshot.getValue(String.class);
                Log.d("DEBUG", "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DEBUG", "Failed to read value.", error.toException());
            }
        });
    }
    private void postDataToFirestore(){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        firestore.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DEBUG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DEBUG", "Error adding document", e);
                    }
                });
    }

    public void addPostData(Post data){
        DatabaseReference myRefRoot = database.getReference();
        myRefRoot.child("posts").setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("DEBUG","post data sucessful");
                        }
                        else
                        {
                            Log.d("DEBUG","post data fail");
                        }
                    }
                });
    }
}