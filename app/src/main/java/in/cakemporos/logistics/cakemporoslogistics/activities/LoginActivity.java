package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.AuthenticationEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.AuthenticationService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.OTPResponse;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Login;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayMessage;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnWebServiceCallDoneEventListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "abc:hello", "asd:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context ctx=this;
    private TextView forgotpassword_TV;
    private EditText newpassword1,newpassword2;
    private Retrofit retrofit;
    //
    private int checkNewpasswords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

//        InputFilter filter = new InputFilter() {
//            public CharSequence filter(CharSequence source, int start, int end,
//                                       Spanned dest, int dstart, int dend) {
//                for (int i = start; i < end; i++) {
//                    if (Character.isWhitespace(source.charAt(i))) {
//                        return "";
//                    }
//                }
//                return null;
//            }
//
//        };
//        mEmailView.setFilters(new InputFilter[] { filter });

        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    mEmailView.setText(result);
                    mEmailView.setSelection(result.length());
                    // alert the user
                }
            }
        });


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        forgotpassword_TV= (TextView) findViewById(R.id.forgot_password);
        //
        forgotpassword_TV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.dialog_forgot_password, null))
                        // Add action buttons
                        .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO: Check if username is present in db

                                final AuthenticationEndPoint endPoint = retrofit.create(AuthenticationEndPoint.class);
                                Dialog f=(Dialog)dialog;
                                EditText userName = (EditText) f.findViewById(R.id.dialog_username);
                                final String uName = userName.getText().toString();
                                AuthenticationService.forgotPassword(LoginActivity.this, retrofit, endPoint, uName, new OnWebServiceCallDoneEventListener() {

                                    String TAG = OnWebServiceCallDoneEventListener.class.getName();

                                    @Override
                                    public void onDone(int message_id, int code, Object... args) {
                                        if(code == 0){
                                            //User doesn't exist
                                            Toast.makeText(ctx,"Error: Username does not exist",Toast.LENGTH_SHORT).show();
                                        } else {
                                            //User exists

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                                            LayoutInflater inflater1 = getLayoutInflater();
                                            builder1.setView(inflater1.inflate(R.layout.dialog_otp, null))
                                                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            //TODO: To verify OTP

                                                            Dialog f=(Dialog)dialog;
                                                            EditText otpText = (EditText) f.findViewById(R.id.dialog_otp);
                                                            final String input = otpText.getText().toString();

                                                            AuthenticationService.verifyOtp(LoginActivity.this, retrofit, endPoint, uName, input, new OnWebServiceCallDoneEventListener() {
                                                                @Override
                                                                public void onDone(int message_id, int code, Object... args) {
                                                                    if(code == 0){
                                                                        Toast.makeText(ctx,"Error: Invalid OTP",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else {
                                                                        final OTPResponse otpResponse = (OTPResponse) args[0];
                                                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ctx);
                                                                        LayoutInflater inflater2 = getLayoutInflater();
                                                                        builder2.setView(inflater2.inflate(R.layout.dialog_change_password, null))
                                                                                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int id) {

                                                                                        //TODO: Update the new password

                                                                                        Dialog f=(Dialog)dialog;
                                                                                        EditText passText = (EditText) f.findViewById(R.id.dialog_new_password1_et);
                                                                                        final String password = passText.getText().toString();

                                                                                        AuthenticationService.changeForgottenPassword(LoginActivity.this, retrofit, endPoint, uName, otpResponse.getSessionId(), password, new OnWebServiceCallDoneEventListener() {
                                                                                            @Override
                                                                                            public void onDone(int message_id, int code, Object... args) {
                                                                                                Toast.makeText(ctx,"Password has been changed successfully",Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                            @Override
                                                                                            public void onContingencyError(int code) {
                                                                                                Log.d(TAG, LoginActivity.this.getString(R.string.error_contingency));
                                                                                            }

                                                                                            @Override
                                                                                            public void onError(int message_id, int code, String... args) {
                                                                                                Log.d(TAG, LoginActivity.this.getString(message_id));
                                                                                            }
                                                                                        });


                                                                                    }
                                                                                })
                                                                                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                });
                                                                        AlertDialog alertDialog2=builder2.create();
                                                                        alertDialog2.show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onContingencyError(int code) {
                                                                    Log.d(TAG, LoginActivity.this.getString(R.string.error_contingency));
                                                                }

                                                                @Override
                                                                public void onError(int message_id, int code, String... args) {
                                                                    Log.d(TAG, LoginActivity.this.getString(message_id));
                                                                }
                                                            });


                                                        }
                                                    })
                                                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            AlertDialog alertDialog1=builder1.create();
                                            alertDialog1.show();
                                        }
                                    }

                                    @Override
                                    public void onContingencyError(int code) {
                                        Log.d(TAG, LoginActivity.this.getString(R.string.error_contingency));
                                    }

                                    @Override
                                    public void onError(int message_id, int code, String... args) {
                                        Log.d(TAG, LoginActivity.this.getString(message_id));
                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });



        //Web Services
        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();



    }








    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isUserValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //hit the service TODO
            AuthenticationEndPoint endPoint = retrofit.create(AuthenticationEndPoint.class);
            AuthenticationService.getTokenByPassword(this, retrofit, endPoint, email, password, this);

        }
    }
    private boolean isUserValid(String email) {
        //TODO: Replace this with your own logic
        return email.length()>2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                                                     .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onDone(int message_id, int code, Object... args) {
        displayMessage(this, getString(message_id), Snackbar.LENGTH_LONG);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        this.finish();

        String token = FirebaseInstanceId.getInstance().getToken();

        AuthenticationEndPoint endPoint = retrofit.create(AuthenticationEndPoint.class);
        AuthenticationService.updateReg(this, retrofit, endPoint, token, new OnWebServiceCallDoneEventListener() {
            String TAG = this.getClass().getName();
            @Override
            public void onDone(int message_id, int code, Object... args) {
                Log.d(TAG, LoginActivity.this.getString(message_id));
            }

            @Override
            public void onContingencyError(int code) {
                Log.d(TAG, LoginActivity.this.getString(R.string.error_contingency));
            }

            @Override
            public void onError(int message_id, int code, String... args) {
                Log.d(TAG, LoginActivity.this.getString(message_id));
            }
        });
    }

    @Override
    public void onContingencyError(int code) {
        displayContingencyError(this, Snackbar.LENGTH_LONG);
        showProgress(false);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onError(int message_id, int code, String args[]) {
        displayError(this, message_id, Snackbar.LENGTH_LONG, args);
        showProgress(false);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /*
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent=new Intent(ctx,HomeActivity.class);
                startActivity(intent);
                //finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }*/
}

