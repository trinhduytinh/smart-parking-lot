package com.example.diary1311;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity2 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtEmail;
    private EditText edtPass;
    private Button btnLogin;
    private Button btnSign;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnSign = findViewById(R.id.btn_sinup);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(LoginActivity2.this, MainActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(LoginActivity2.this, "Login fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity2.this, Signup.class);
                startActivity(intent);
            }
        });

    }
}