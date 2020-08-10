package com.zoothii.parseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;

public class SignInActivity extends AppCompatActivity {

    EditText textUserName, textPassword;
    Button buttonSignIn;
    Boolean newAccount;
    //WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        parseInitialize();

        textUserName = findViewById(R.id.textUserName);
        textPassword = findViewById(R.id.textPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        /*myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadUrl("http://www.thisiscolossal.com/wp-content/uploads/2018/04/agif2opt.gif");
*/

       /* ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null && !newAccount){
            Intent intentToFeed = new Intent(SignInActivity.this, FeedActivity.class);
            startActivity(intentToFeed);
            finish();
        }*/

    }

    protected void parseInitialize(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gxRLSzkAiTAd7dJkGbcgNFULcYXAvdM785cEARZK")
                .clientKey("BtiktKhK6vGHX6VLnenaEF321TLWDi6EPy8xH16z")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }

    public void openCreateAccountActivity(View view){
        newAccount = true;
        Intent intentToCreateAccount = new Intent(SignInActivity.this, CreateAccountActivity.class);
        startActivity(intentToCreateAccount);
        finish();
    }

    public void signIn(){
        String userName = textUserName.getText().toString();
        String password = textPassword.getText().toString();
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    Toast.makeText(SignInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignInActivity.this, "Welcome "+user.getUsername(), Toast.LENGTH_SHORT).show();
                    Intent intentToFeed = new Intent(SignInActivity.this, FeedActivity.class);
                    startActivity(intentToFeed);
                    finish();
                }
            }
        });
    }

}