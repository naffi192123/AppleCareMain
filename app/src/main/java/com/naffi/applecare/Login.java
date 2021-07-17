package com.naffi.applecare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login  extends AppCompatActivity {
    TextView toMain;
    TextInputEditText textInputEditTextUsername,textInputEditTextPassword;
    Button buttonLogin;
    TextView textViewSignup;
    ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }
    private void checkSession(){
        //Check if user is loggedin
        // if user is loggedin move to main avtivity
        SessionManagement sessionManagement = new SessionManagement(Login.this);
        int user_id = sessionManagement.getSession();

        // if user is loggedin to the main activity
        if (user_id!= -1){
            moveToMainActivity();
        }
        else {
            // do nothing
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toMain = findViewById(R.id.toMain);
       //gotomainactiitydirectly
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
    public void login(View view){
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);
        User user;
        String username,password;
        username = textInputEditTextUsername.getText().toString();
        password = textInputEditTextPassword.getText().toString();
        user = new User(1,username);

        if (!username.equals("") && !password.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            //writing Sessions start
            SessionManagement sessionManagement = new SessionManagement(Login.this);
            sessionManagement.saveSession(user);
            //session end
            //Start ProgressBar first (Set visibility VISIBLE)
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[2];

                    field[0] = "username";
                    field[1] = "password";

                    //Creating array for data
                    String[] data = new String[2];

                    data[0] = username;
                    data[1] = password;

                    PutData putData = new PutData("http://192.168.78.73:5000/userLogin", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar.setVisibility(View.GONE);
                            String result = putData.getResult();
                            //End ProgressBar (Set visibility to GONE)
                            // Log.i("PutData", result);
                            if (result.equals("Login Success")){
                                Toast.makeText(Login.this, result, Toast.LENGTH_SHORT).show();
                                moveToMainActivity();
                            }
                            else {
                                Toast.makeText(Login.this, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    //End Write and Read data with URL
                }
            });
        }
        else {
            Toast.makeText(Login.this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveToMainActivity(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void moveToSignup(View view){
        textViewSignup = findViewById(R.id.signUpText);
        Intent intent = new Intent(getApplicationContext(),Signup.class);
        startActivity(intent);
        finish();
    }
}