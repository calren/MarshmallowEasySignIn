package com.example.caren.easysigninexample;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignInActiivty extends Activity
        implements
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "EasySignInExample";
    private static final int SIGN_IN_INTENT_CODE = 100;
    private EditText email;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpEasySignUp();
            }
        });
    }

    private void setUpEasySignUp() {
        GoogleApiClient googleApiClient =
                new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                        .addApi(Auth.CREDENTIALS_API).build();
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setHintPickerConfig(
                                new CredentialPickerConfig.Builder().setShowCancelButton(true)
                                        .build()).setEmailAddressIdentifierSupported(true).build();

        PendingIntent intent =
                Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), SIGN_IN_INTENT_CODE, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended:" + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                email.setText(credential.getId());
                name.setText(credential.getName());
                // Check if user id exists in database and handle accordingly
            } else {
                Toast.makeText(this, "Hint Read Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
