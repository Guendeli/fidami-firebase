package com.guendeli.fidami.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.guendeli.fidami.LoginActivity;
import com.guendeli.fidami.R;
import com.guendeli.fidami.models.User;
import com.guendeli.fidami.mvp.interactors.MyCommand;

public class RegisterActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    Button registerButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // layout bindings
        emailText = (EditText) findViewById(R.id.input_username);
        passwordText = (EditText) findViewById(R.id.input_password);
        registerButton = (Button) findViewById(R.id.button_register);

        firebaseAuth = FirebaseAuth.getInstance();

        // adding registerbutton listener
        registerButton.setOnClickListener(registerButtonListener);
    }

    private View.OnClickListener registerButtonListener = new View.OnClickListener(){
        public void onClick(View v){
            String email = emailText.getText().toString();
            String passWord = passwordText.getText().toString();
            // check if both fields are fill
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(passWord)){
                Toast.makeText(getApplicationContext(), "Please fill all the required fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            // firebase creating user with email and password
            firebaseAuth.createUserWithEmailAndPassword(email, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        // Complete
                        User.getInstance().createNewUser(new MyCommand() {
                            @Override
                            public void execute(int value) {
                                Log.e("Register", "Register Complete");
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Email or password error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
}
