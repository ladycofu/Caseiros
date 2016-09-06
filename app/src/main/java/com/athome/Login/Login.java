package com.athome.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.athome.Principal.ActividadPrincipal;
import com.athome.R;
import com.athome.Tools.ValidateNetworkLocationService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lady on 30/12/2015.
 */
public class Login extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener { private static final String TAG = Login.class.getSimpleName();




    /* *************************************
     *              GENERAL                *
     ***************************************/
    private Firebase myFirebaseRef;
    private AuthData mAuthData;
    private ProgressDialog mAuthProgressDialog;
    private Firebase.AuthStateListener mAuthStateListener;

    /* *************************************
     *              FACEBOOK               *
     ***************************************/
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    /* *************************************
    *              GOOGLE                 *
    ***************************************/

    public static final int RC_GOOGLE_LOGIN = 1;
    private GoogleApiClient mGoogleApiClient;
    private boolean mGoogleIntentInProgress;
    private boolean mGoogleLoginClicked;
    private ConnectionResult mGoogleConnectionResult;
    private SignInButton mGoogleLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Firebase.setAndroidContext(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.login_activity);
        myFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url)).child("usuarios");
        /* Setup the progress dialog that is displayed later when authenticating with Firebase */

        /* *************************************
        *              FACEBOOK               *
        ***************************************/
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onFacebookAccessTokenChange(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
        /* *************************************
        *              GOOGLE               *
        ***************************************/
        mGoogleLoginButton = (SignInButton) findViewById(R.id.btn_sign_in);
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleLoginClicked = true;
                if (!mGoogleApiClient.isConnecting()) {
                    if (mGoogleConnectionResult != null) {
                        resolveSignInError();
                    } else if (mGoogleApiClient.isConnected()) {
                        getGoogleOAuthTokenAndLogin();
                    } else {
                    /* connect API now */
                        Log.d(TAG, "Trying to connect to Google API");
                        mGoogleApiClient.connect();
                    }
                }
            }
        });
        /* Setup the Google API object to allow Google+ logins */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();


    }



    //Metodo de Facebook
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            myFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(final AuthData authData) {

                    final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setTitle("Cocinando Tus Datos");
                    progressDialog.setMessage("Espera un momento...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Thread.sleep(4000);

                                String nombre = (String) authData.getProviderData().get("displayName");
                                String url = (String) authData.getProviderData().get("profileImageURL");
                                SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("id_Usuario", authData.getUid());
                                editor.putString("Nombre", nombre);
                                editor.putString("urlFoto", url);
                                editor.commit();

                                Firebase registroFB;
                                registroFB = myFirebaseRef.child(authData.getUid());
                                registroFB.child("correo_Usuario").setValue(authData.getProviderData().get("email"));
                                registroFB.child("nombre_Usuario").setValue(authData.getProviderData().get("displayName"));
                                registroFB.child("imagen_Usuario").setValue(authData.getProviderData().get("profileImageURL"));

                                Intent intent = new Intent(Login.this, ActividadPrincipal.class);
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                LoginManager.getInstance().logOut();


                            } catch (Exception e) {

                            }
                            progressDialog.dismiss();

                        }
                    }).start();
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                }
            });
        } else {
        /* Logged out of Facebook so do a logout from the Firebase app */
            //myFirebaseRef.unauth();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_LOGIN) {
            /* This was a request by the Google API */
            if (resultCode != RESULT_OK) {
                mGoogleLoginClicked = false;
            }
            mGoogleIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }

        }
        else {
            /* Otherwise, it's probably the request by the Facebook login button, keep track of the session */
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    /* ************************************
    *              GOOGLE                *
    **************************************
    */
    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getGoogleOAuthTokenAndLogin() {
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(Login.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                mGoogleLoginClicked = false;
                if (token != null) {

                    /* Successfully got OAuth token, now login with Google */
                    myFirebaseRef.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(final AuthData authData) {

                            final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                            progressDialog.setTitle("Cocinando Tus Datos");
                            progressDialog.setMessage("Espera un momento...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        Thread.sleep(4000);
                                        String nombre = (String) authData.getProviderData().get("displayName");
                                        String url = (String) authData.getProviderData().get("profileImageURL");
                                        SharedPreferences preferences = getSharedPreferences("datosUsuario", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("id_Usuario",authData.getUid());
                                        editor.putString("Nombre", nombre);
                                        editor.putString("urlFoto", url);
                                        editor.commit();

                                        Firebase registroFB;
                                        registroFB = myFirebaseRef.child(authData.getUid());
                                        registroFB.child("correo_Usuario").setValue(authData.getProviderData().get("email"));
                                        registroFB.child("nombre_Usuario").setValue(authData.getProviderData().get("displayName"));
                                        registroFB.child("imagen_Usuario").setValue(authData.getProviderData().get("profileImageURL"));

                                        Intent intentGoogle = new Intent(Login.this, ActividadPrincipal.class);
                                        startActivity(intentGoogle.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                                    } catch (Exception e) {

                                    }
                                    progressDialog.dismiss();

                                }
                            }).start();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {

                        }
                    });
                } else if (errorMessage != null) {
                    mAuthProgressDialog.hide();
                    showErrorDialog(errorMessage);
                }
            }
        };
        task.execute();
    }

    @Override
    public void onConnected(final Bundle bundle) {
        /* Connected with Google API, use this to authenticate with Firebase */
        getGoogleOAuthTokenAndLogin();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mGoogleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            mGoogleConnectionResult = result;

            if (mGoogleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                resolveSignInError();
            } else {
                Log.e(TAG, result.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ignore
    }

    public   void onRegistroClicked(View view) {
        Intent act = new Intent(this, Registro.class);
        startActivity(act);
    }
    public   void onInciosesionClicked(View view) {
        Intent act = new Intent(this, Inicio_Sesion.class);
        startActivity(act);
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

