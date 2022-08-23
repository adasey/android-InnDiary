package com.potatomeme.appdesiginformat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.helper.AppHelper;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.helper.LoginHelper;
import com.potatomeme.appdesiginformat.ui.DiaryFragment;
import com.potatomeme.appdesiginformat.ui.SettingFragment;
import com.potatomeme.appdesiginformat.ui.TodoFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9002;


    Toolbar toolbar;
    BottomNavigationView navigationView;
    FragmentManager fragmentManager;
    DiaryFragment diaryFragment;
    TodoFragment todoFragment;
    SettingFragment settingFragment;

    Dialog loginDialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);



        fragmentManager = getSupportFragmentManager();
        diaryFragment = new DiaryFragment();
        todoFragment = new TodoFragment();
        settingFragment = new SettingFragment();
        fragmentManager.beginTransaction().replace(R.id.framelayout, todoFragment).commit();

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_SHORT).show();
                switch (item.getItemId()){
                    case R.id.menu_todo:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, todoFragment).commit();
                        return true;
                    case R.id.menu_home:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, diaryFragment).commit();
                        return true;
                    case R.id.menu_setting:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, settingFragment).commit();
                        return true;
                    default:
                        return false;
                }
            }
        });

        Log.d("test","LoginHelper.isLogin : "+ LoginHelper.isLogin);
        Log.d("test","LoginHelper.isGuest : "+LoginHelper.isGuest);


        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginDialog = new Dialog(MainActivity.this);
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginDialog.setContentView(R.layout.popup_login);
        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void loginDialogShow(){
        loginDialog.show();
        loginDialog.findViewById(R.id.signin_button).setOnClickListener(view -> {
            signIn();
        });
        loginDialog.findViewById(R.id.cancel_button).setOnClickListener(view -> {
            loginDialog.dismiss();
        });
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
        LoginHelper.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            LoginHelper.login();
                            editor.putBoolean("isGuest", LoginHelper.isGuest);
                            editor.apply();
                            loginDialog.dismiss();
                            settingFragment.optionsSetting();
                            Toast.makeText(getApplicationContext(),LoginHelper.mUser.getEmail()+"님 환영합니다!",Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }








}