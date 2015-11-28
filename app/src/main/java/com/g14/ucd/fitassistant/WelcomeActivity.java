package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import static com.g14.ucd.fitassistant.R.*;

/**
 * Created by Rodrigo on 17/10/2015.
 */

/**
 * First Activity seen by the user, that offers 3 options
 * Login
 * SignUp
 * and Login using Facebook
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        // Log in button click handler
        Button loginButton = (Button) findViewById(id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent of the log in activity
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });

        // Sign up button click handler
        Button signupButton = (Button) findViewById(id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Starts an intent for the sign up activity
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
            }
        });
        Button loginWithFacebookButton = (Button) findViewById(id.login_facebook_button);
        loginWithFacebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginWithFB();
            }

        });



    }

    /**
     * Login with Facebook
     */
    private void loginWithFB() {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, null, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    Intent intent = new Intent(WelcomeActivity.this, DispatchActivity.class);
                    Installation pInst = new Installation();
                    pInst.install();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    Intent intent = new Intent(WelcomeActivity.this, DispatchActivity.class);
                    Installation pInst = new Installation();
                    pInst.install();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}