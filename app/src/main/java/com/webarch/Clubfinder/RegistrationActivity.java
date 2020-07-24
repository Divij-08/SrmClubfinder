package com.webarch.Clubfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    private Button mRegister;
    private EditText mEmail,mPassword,mName,mLinkedin,mDescription,mSkills;
    private RadioGroup mRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private TextView mSignin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        mSignin=findViewById(R.id.signin);
        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    Intent intent=new Intent(RegistrationActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        };

        mRegister=findViewById(R.id.register);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mName=findViewById(R.id.name);
        mRadioGroup=findViewById(R.id.radioGroup);
        mSkills=findViewById(R.id.Skills);
        mDescription= findViewById(R.id.Description);
        mLinkedin=findViewById(R.id.Linkedin);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectId = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = findViewById(selectId);
                if(radioButton.getText()==null)
                {
                   Toast.makeText(RegistrationActivity.this, "Please select one option",Toast.LENGTH_SHORT).show();
                }

                final String email=mEmail.getText().toString();
                final String password=mPassword.getText().toString();
                final String name=mName.getText().toString();
                final String linkedin=mLinkedin.getText().toString();
                final String description=mDescription.getText().toString();
                final String skills=mSkills.getText().toString();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if(!task.isSuccessful()){
                         Toast.makeText(RegistrationActivity.this,"Sign up error",Toast.LENGTH_SHORT).show();
                     }

                         String userId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                         DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                         Map<String, Object> userInfo = new HashMap<String, Object>();
                         userInfo.put("name",name);
                         userInfo.put("LinkedIn",linkedin);
                         userInfo.put("Description",description);
                         userInfo.put("Skills",skills);
                         userInfo.put("Identity",radioButton.getText().toString());
                         userInfo.put("profileImageUrl","default");

                         currentUserDb.updateChildren(userInfo);
                     }

                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
