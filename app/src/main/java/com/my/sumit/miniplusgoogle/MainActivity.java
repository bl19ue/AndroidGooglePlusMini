package com.my.sumit.miniplusgoogle;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.my.sumit.singleton.MyPlusDomainAPI;
import java.io.IOException;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnClickListener {

    public static final String TAG = "LoGIN";
    private SignInButton signInBtn;
    private TextView _mStatus;
    private String _accessToken;
    GoogleCredential credential;
    String userEmail;

    final static int REQUEST_CODE_PICK_ACCOUNT = 1000;

    private final static String PROFILE_ME = "https://www.googleapis.com/auth/plus.me";
    private final static String CIRCLE_SCOPE = "https://www.googleapis.com/auth/plus.circles.read";
    private final static String PROFILE_OTHER = "https://www.googleapis.com/auth/plus.profiles.read";

    private static final String SCOPE = "oauth2:" + PROFILE_ME + " " + CIRCLE_SCOPE;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _mStatus = (TextView)findViewById(R.id.sign_in_status);
        signInBtn = (SignInButton)findViewById(R.id.btn_sign_in);
        signInBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};

        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, true, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    public void getUserName(){
        if(userEmail == null){
            pickUserAccount();
        } else {
            _accessToken = null;
            new RetrieveTokenTask().execute();
        }
    }

    /**
     * This method is a hook for background threads and async tasks that need to
     * provide the user a response UI when an exception occurs.
     */
    public void handleException(final Exception e) {
        // Because this call comes from the AsyncTask, we must ensure that the following
        // code instead executes on the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            MainActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_PICK_ACCOUNT){
            if(resultCode == RESULT_OK){
                userEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.i(TAG,"Email : "+userEmail);
                getUserName();
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Pick Account", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if(v.getId()==R.id.btn_sign_in){
            _mStatus.setText("Signing in... Please wait");
            pickUserAccount();
        }
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), userEmail, SCOPE);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                handleException(e);
                Log.i(TAG,"Error : " + e.toString());
                // startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            _accessToken = s;
            Log.i(TAG, "Access Token onPostExec : " + _accessToken);

            credential = new GoogleCredential().setAccessToken(_accessToken);
            new MyPlusDomainAPI(credential);
            startAuthorizedActivity();
        }

        public void startAuthorizedActivity(){
            Intent goToAuthorized = new Intent(MainActivity.this, AuthorizedActivity.class);
            startActivity(goToAuthorized);
        }
    }


    /*
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected");
        _mAccountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
        if(MyPlusDomainAPI.getPlusDomains() == null) {
            new RetrieveTokenTask().execute(_mAccountName);
        }
        else{
            startAuthorizedActivity();
        }
        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;

    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        if(!mGoogleApiClient.isConnecting()){
            switch (v.getId()){
                case R.id.sign_in_button:{
                    _mStatus.setText("Signing in... Please wait");
                    if(MyPlusDomainAPI.getPlusDomains() != null) {
                        Log.i(TAG, "starting 2nd Activity");
                        Intent goToDrawerIntent = new Intent(MainActivity.this, AuthorizedActivity.class);
                        startActivity(goToDrawerIntent);
                    }
                    resolveSignInError();
                    break;
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        if(connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE){
            //Google API is not available, API might not be supported
            //You may need to use a different API
        }
        else if(mSignInProgress != STATE_IN_PROGRESS){
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = connectionResult.getResolution();
            mSignInError = connectionResult.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in button
                // so we should continue processing errors until the user is signed in
                // or they click cancel.
                resolveSignInError();
            }
        }
        //If the user does not have a connection, we will sign him out
        //onSignedOut();
    }

    public void resolveSignInError(){
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
            showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            String SCOPE_PLUS_ME = " https://www.googleapis.com/auth/plus.me";
            String SCOPE_PLUS_CIRCLES_READ = " https://www.googleapis.com/auth/plus.circles.read";
            String scopes = "oauth2:" + Scopes.PROFILE + SCOPE_PLUS_ME + SCOPE_PLUS_CIRCLES_READ;
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            _accessToken = s;
            Log.i(TAG, "Access Token onPostExec : " + _accessToken);
            credential = new GoogleCredential().setAccessToken(_accessToken);

            new MyPlusDomainAPI(credential);
            startAuthorizedActivity();

        }
    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {

    }

    public void startAuthorizedActivity(){

        Intent goToAuthorized = new Intent(MainActivity.this, AuthorizedActivity.class);
        startActivity(goToAuthorized);
    }
    */
}