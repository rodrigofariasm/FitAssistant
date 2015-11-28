package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class reprensenting the Sign up Activity, the screen where the user
 * can register him/her self in order to use the app
 */
public class SignUpActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener{

    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    static TextView dateEdit;
    private Installation pInst;

	/**
	 * Method called everytime the activity is created.
	 * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
		initialize();
	}
	
	/**
	 * method that initialize all the values
	 * */
    private void initialize(){
        // Set up the signup form.
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        firstnameEditText= (EditText) findViewById(R.id.first_name_text);
        lastnameEditText= (EditText) findViewById(R.id.last_name_text);
        dateEdit = (TextView) findViewById(R.id.birth_text_view);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        passwordAgainEditText = (EditText) findViewById(R.id.password_again_edit_text);
        passwordAgainEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.edittext_action_signup ||
                        actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    signup();
                    return true;
                }
                return false;
            }
        });

        // Set up the submit button click handler
        Button mActionButton = (Button) findViewById(R.id.signup_button);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                signup();
            }
        });
    }

	/**
	 * method that sign up the user, validates and register her/his
	 * information
	 * */
    private void signup() {
        String username = firstnameEditText.getText().toString().trim()+ " " + lastnameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordAgain = passwordAgainEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        TextView dateBirth = (TextView) findViewById(R.id.birth_text_view);
        Date birthday = new Date(dateBirth.getText().toString().trim());
        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder();
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_email));
        }
        if (password.length() < 6) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            if(password.length()== 0){
                validationErrorMessage.append(getString(R.string.error_blank_password));
            }else{
                validationErrorMessage.append(getString(R.string.error_size_password));
            }
            validationError = true;

        }
        if (!password.equals(passwordAgain)) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage(getString(R.string.progress_signup));
        dialog.show();

        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(password);
        user.put("date_of_birth", birthday);
        user.put("name", username);
        final boolean finalValidationError = validationError;
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                dialog.dismiss();
                if (e != null || finalValidationError) {
                    // Show the error message

                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    Toast.makeText(SignUpActivity.this, R.string.signup_verification, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    pInst = new Installation();
                    pInst.install();

                    startActivity(intent);
                }
            }
        });
    }
    
    /**
	 * method shows the date picker for the date of birth
	 * */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datepicker");
    }

	/**
	 * method called when the date is selected
	 * */
    public void onDateSet(DatePicker view, int year, int month, int day) {
    }

	/**
	 * class that represents the date picker dialog and its functionalities
	 * */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dateEdit.setText((month+1) + "/" + day + "/" + year);
        }
    }


}
