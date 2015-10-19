package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import static com.g14.ucd.fitassistant.R.*;


/**
 * Created by rodrigofarias on 10/17/15.
 */
public class LoginActivity extends Activity {
    // UI references.
    private EditText usernameEditText;
    private EditText passwordEditText;
    Installation pInst;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_login);
        // Set up the login form.
        usernameEditText = (EditText) findViewById(id.username);
        passwordEditText = (EditText) findViewById(id.password);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == id.edittext_action_login ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    login();
                    return true;
                }
                return false;
            }
        });
        super.onCreate(savedInstanceState);


        // Set up the submit button click handler
        Button actionButton = (Button) findViewById(id.login_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate the log in data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(string.error_intro));
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(string.error_blank_email));
        }
        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append(getString(string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(string.error_blank_password));
        }
        validationErrorMessage.append(getString(string.error_end));


        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getString(string.progress_login));
        dialog.show();
        // Call the Parse login method
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                boolean verified = user.getBoolean("emailVerified");
                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                        Toast.makeText(LoginActivity.this, string.invalid_login_credentials, Toast.LENGTH_LONG).show();
                        Log.d("login", e.getMessage().toString());
                }else {
                    if(!verified){
                        Toast.makeText(LoginActivity.this, string.email_not_verified, Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        pInst = new Installation();
                        pInst.install();

                        startActivity(intent);
                    }
                }


            }
        });
    }
}