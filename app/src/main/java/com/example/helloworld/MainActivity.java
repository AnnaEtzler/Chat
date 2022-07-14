package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText editUsername, editPasswort, editEmail;
    private Button btnSubmit;
    private TextView txtLoginInfo;

    private boolean isSigninUp = true;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        editEmail = findViewById(R.id.editEmail);
        editPasswort = findViewById(R.id.editPasswort);
        editUsername = findViewById(R.id.editUsername);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            finish();

        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editEmail.getText().toString().isEmpty() || editPasswort.getText().toString().isEmpty()  ){
                    if(isSigninUp && editUsername.getText().toString().isEmpty()){

                        Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(isSigninUp){
                    createUser();
                } else {
                    handleLogin();

                }
            }
        });

        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSigninUp){
                    isSigninUp = false;
                    editUsername.setVisibility(View.GONE);
                    btnSubmit.setText("Log in");
                    txtLoginInfo.setText("Don't have an account?");
                } else {
                    isSigninUp = true;
                    editUsername.setVisibility(View.VISIBLE);
                    btnSubmit.setText("Sign up");
                    txtLoginInfo.setText("Alresdy have an account? Log in");
                }
            }
        });
    }

    private void createUser() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editEmail.getText().toString(),editPasswort.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance("https://helloworld-a6ce6-default-rtdb.europe-west1.firebasedatabase.app").getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(editUsername.getText().toString(), editEmail.getText().toString(), ""));
                    startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                    Toast.makeText(MainActivity.this, "successfuly", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void handleLogin() {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(editEmail.getText().toString(), editPasswort.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                startActivity(new Intent(MainActivity.this, FriendsActivity.class));
                Toast.makeText(MainActivity.this, "logged successfuly", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    });





    }

}