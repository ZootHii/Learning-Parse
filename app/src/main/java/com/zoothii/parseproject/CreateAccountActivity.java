package com.zoothii.parseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {

    EditText textUserName, textPassword, textEmailAddress;
    Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        textUserName = findViewById(R.id.textUserName);
        textPassword = findViewById(R.id.textPassword);
        textEmailAddress = findViewById(R.id.textEmailAddress);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    public void createAccount(){
        String userName = textUserName.getText().toString();
        String password = textPassword.getText().toString();
        String emailAddress = textEmailAddress.getText().toString();

        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(userName);
        parseUser.setPassword(password);
        parseUser.setEmail(emailAddress);

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Toast.makeText(CreateAccountActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Check your Email address to verification", Toast.LENGTH_LONG).show();
                    Intent intentToSignIn = new Intent(CreateAccountActivity.this, SignInActivity.class);
                    startActivity(intentToSignIn);
                    finish();
                }
            }
        });

    }
}