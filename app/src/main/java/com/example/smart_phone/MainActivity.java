package com.example.smart_phone;

import static android.content.ContentValues.TAG;
import static android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import androidx.biometric.*;
import androidx.biometric.BiometricPrompt;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static androidx.biometric.BiometricManager.Authenticators.*;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;




public class MainActivity extends AppCompatActivity {

    //Biometric Auth
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    //Google Auth
    private FirebaseAuth mAuth;
    private GoogleSignInClient mSignInClient;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private static final int RC_SIGN_IN = 1;

    //Email Auth
    private EditText email;
    private EditText pass;

    //Firebase firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Facebook
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this));


        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.w("FB","ERROR ",exception);
                        // App code
                    }
                });

        initializeUI();
        biometricLogin();
        initializeGoogle();
        initializeFacebook();



    }

    private void initializeUI()
    {
        email = findViewById(R.id.ETLEmail);
        pass = findViewById(R.id.ETLPass);

        //Initialize Biometric
        Button biometricLoginButton = findViewById(R.id.biometric_login);
        biometricLoginButton.setOnClickListener(view -> {

        });

        //Initialize Google Sign in
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view -> {

            googleLogin();
        });

        //Initialize FaceBook Sign In
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FB", "SUCCESS FACEBOOK LOGIN");
                handleFacebookAccessToken(loginResult.getAccessToken());
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.w("FB","ERROR",exception);
                // App code
            }
        });
        loginButton.setOnClickListener(view -> {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        });

        //Initialize Register Button
        Button registerBtn = findViewById(R.id.registerBtnL);
        registerBtn.setOnClickListener(view -> {
            goToRegister();
        });

        //Initialize Email Login Button
        Button loginBtn = findViewById(R.id.loginBtnL);
        loginBtn.setOnClickListener(view -> {

            emailLogin();
        });
    }

    private void initializeGoogle()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(this, gso);
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();
    }

    private void initializeFacebook()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    }

    private void googleLogin()
    {
        Intent intent = mSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }


    private void biometricLogin()
    {
        //Biometric
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                errorCode + " Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();
    }

    private void emailLogin()
    {
        //validate input
        if(email.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Email field must not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(pass.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Password field must not be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                DocumentReference docRef = db.collection("users").document(mAuth.getUid());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                userInfo.email = document.get("Email").toString();
                                                userInfo.name = document.get("Name").toString();
                                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Log.d(TAG, "No such document");
                                                Toast.makeText(MainActivity.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Login failed. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void goToRegister()
    {
        Intent intent = new Intent(MainActivity.this, Email_register.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //TO BE DELETE
        mAuth.signOut();
        //TO BE DELETE

        if(currentUser != null)
        {
            DocumentReference docRef = db.collection("users").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            userInfo.email = document.get("Email").toString();
                            userInfo.name = document.get("Name").toString();
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            //Log.d("TEST", currentUser.getEmail());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try{
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthGoogle(account);
                }
                catch (ApiException e)
                {
                    Log.w("TEST", "Failed", e);
                }
        }
    }

    private void firebaseAuthGoogle(GoogleSignInAccount acct)
    {
        AuthCredential cred = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(cred)
                .addOnSuccessListener(this,authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    getDataFromFirebase(user);
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getDataFromFirebase(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void getDataFromFirebase(FirebaseUser acct)
    {
        DocumentReference docRef = db.collection("users").document(acct.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TEST", "DocumentSnapshot data: " + document.getData());
                        userInfo.email = document.get("Email").toString();
                        userInfo.name = document.get("Name").toString();
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                       // userInfo.registeredTime = document.get("Email").toDate();

                    } else {
                        Log.d("TEST", "No such document");
                        Map<String, Object> user = new HashMap<>();
                        user.put("Email", acct.getEmail());
                        user.put("Name", acct.getDisplayName());
                        user.put("createdAt", FieldValue.serverTimestamp());
                        db.collection("users").document(acct.getUid())
                                .set(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            userInfo.email = document.get("Email").toString();
                                            userInfo.name = document.get("Name").toString();
                                            Toast.makeText(MainActivity.this, "Successfully Register.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                            startActivity(intent);
                                        }

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Failed to register. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Log.d("TEST", "get failed with ", task.getException());
                }
            }
        });
    }




}