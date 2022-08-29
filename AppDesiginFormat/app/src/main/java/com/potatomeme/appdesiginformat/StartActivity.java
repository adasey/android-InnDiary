package com.potatomeme.appdesiginformat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.helper.FireBaseHelper;
import com.potatomeme.appdesiginformat.helper.RestHelper;

public class StartActivity extends AppCompatActivity {


    private static final String TAG = "StartActivity";
    private static final int RC_SIGN_IN = 9001;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private GoogleSignInClient mGoogleSignInClient;

    RestHelper restHelper; //휴일 헬퍼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        restHelper = new RestHelper();

        dbSetting();

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();


        //test용용
        editor.putBoolean("isGuest",false);
        editor.apply();

        FireBaseHelper.setting();
        FireBaseHelper.isGuest = pref.getBoolean("isGuest", false);


        SignInButton signIn_button = findViewById(R.id.signin_button);
        Button guest_button = findViewById(R.id.guest_button);


        if (FireBaseHelper.isLogin || FireBaseHelper.isGuest) {
            signIn_button.setVisibility(View.INVISIBLE);
            guest_button.setVisibility(View.INVISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                startMain();
            }, 2500);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn_button.setOnClickListener(view -> {
            signIn();
        });
        guest_button.setOnClickListener(view -> {
            guestLogin();
        });
    }

    private void dbSetting() {
        DbHelper.dbSetting(getApplicationContext());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FireBaseHelper.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FireBaseHelper.login();
                            editor.putBoolean("isGuest", FireBaseHelper.isGuest);
                            editor.apply();
                            //LoginHelper.databaseSetting();
                            startMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"연결할수었습니다 데이터나 와이파이가 필요합니다.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void guestLogin() {
        FireBaseHelper.useGuest();
        editor.putBoolean("isGuest", FireBaseHelper.isGuest);
        editor.apply();
        startMain();
    }

    private void startMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        intent.putStringArrayListExtra("list", restHelper.getRestList());
        startActivity(intent);
        finish();
    }
}