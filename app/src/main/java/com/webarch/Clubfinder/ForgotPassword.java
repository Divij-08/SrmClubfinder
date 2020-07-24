package com.webarch.Clubfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.webarch.Clubfinder.R;

public class ForgotPassword extends AppCompatActivity {
    private EditText mEmail;
    private Button mSubmit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mEmail=findViewById(R.id.email);
        mSubmit=findViewById(R.id.submit);
        mAuth= FirebaseAuth.getInstance();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = mEmail.getText().toString();
                if(TextUtils.isEmpty(userEmail)){
                    Toast.makeText(ForgotPassword.this,"Please write your valid Email address first.",Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this,"Please check your Email Account, If you want to reset your password.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
                                startActivity(intent);
                                finish();


                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(ForgotPassword.this,"Error Occured:"+ message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


}
