package com.onevest.dev.tulung.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.main.activity.MainActivity;
import com.onevest.dev.tulung.utils.Constants;
import com.onevest.dev.tulung.utils.PrefsManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements
        iLoginView,
        GoogleApiClient.OnConnectionFailedListener{

    //region BindView
    @BindView(R.id.fb_login_button)
    LoginButton fbLoginButton;

    @BindView(R.id.google_login_button)
    SignInButton googleLoginButton;

    @OnClick(R.id.google_login_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //endregion

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;

    private PrefsManager prefsManager;
    private DatabaseReference databaseReference;
    private String uuid;
    private String name;
    private String email;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        prefsManager = new PrefsManager(this);
        callbackManager = new CallbackManager.Factory().create();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USERS);

        if (prefsManager.getLoginStatus()) {
            navigateToMainActivity();
        }

        fbLoginButton.setReadPermissions("email", "public_profile");
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, loginResult.toString());
                handleAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, error.getLocalizedMessage());
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_auth_web))
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login));
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        Log.d(TAG, "navigateToMainActivity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.d(TAG, "Auth Failed");
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleAccessToken(AccessToken token) {
        Log.d(TAG, token.toString());
        showLoginDialog(true);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Auth Success");
                    uuid = mAuth.getCurrentUser().getUid().toString();
                    name = mAuth.getCurrentUser().getDisplayName().toString();
                    email = mAuth.getCurrentUser().getEmail().toString();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(uuid)) {
                                prefsManager.setLoginStatus(true);
                                prefsManager.setUuid(uuid);
                                prefsManager.setName(name);
                                prefsManager.setEmail(email);
                                showLoginDialog(false);
                                navigateToMainActivity();
                            } else {
                                databaseReference.child(uuid).child(Constants.NAME).setValue(name);
                                databaseReference.child(uuid).child(Constants.EMAIL).setValue(email)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                prefsManager.setLoginStatus(true);
                                                prefsManager.setUuid(uuid);
                                                prefsManager.setName(name);
                                                prefsManager.setEmail(email);
                                                showLoginDialog(false);
                                                navigateToMainActivity();
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, databaseError.getMessage());
                        }
                    });
                } else {
                    Log.d(TAG, "Auth Failed");
                    showLoginDialog(false);
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, account.getDisplayName());
        showLoginDialog(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Auth Success");
                            uuid = mAuth.getCurrentUser().getUid().toString();
                            name = mAuth.getCurrentUser().getDisplayName().toString();
                            email = mAuth.getCurrentUser().getEmail().toString();
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(uuid)) {
                                        Log.d(TAG, "hasChild");
                                        prefsManager.setLoginStatus(true);
                                        prefsManager.setUuid(uuid);
                                        prefsManager.setName(name);
                                        prefsManager.setEmail(email);
                                        showLoginDialog(false);
                                        navigateToMainActivity();
                                    } else {
                                        databaseReference.child(uuid).child(Constants.NAME).setValue(name);
                                        databaseReference.child(uuid).child(Constants.EMAIL).setValue(email)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        prefsManager.setLoginStatus(true);
                                                        prefsManager.setUuid(uuid);
                                                        prefsManager.setName(name);
                                                        prefsManager.setEmail(email);
                                                        showLoginDialog(false);
                                                        navigateToMainActivity();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d(TAG, databaseError.getMessage());
                                }
                            });
                        } else {
                            Log.d(TAG, "Auth Failed");
                            showLoginDialog(false);
                        }
                    }
                });
    }

    @Override
    public void showLoginDialog(boolean status) {
        if (status) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }
}
