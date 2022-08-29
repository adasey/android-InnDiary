package com.potatomeme.appdesiginformat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.entity.DiarySlot;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.entity.TodoSlot;
import com.potatomeme.appdesiginformat.helper.AppHelper;
import com.potatomeme.appdesiginformat.helper.DbHelper;
import com.potatomeme.appdesiginformat.helper.FireBaseHelper;
import com.potatomeme.appdesiginformat.ui.DiaryFragment;
import com.potatomeme.appdesiginformat.ui.SettingFragment;
import com.potatomeme.appdesiginformat.ui.TodoFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9002;


    Toolbar toolbar;
    BottomNavigationView navigationView;

    FragmentManager fragmentManager;
    DiaryFragment diaryFragment;
    TodoFragment todoFragment;
    SettingFragment settingFragment;

    Dialog loginDialog, selectDialog, slotDialog, uploadDialog, downloadDialog,deleteDialog;
    ProgressDialog progressDialog;

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
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
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

        Log.d("test", "LoginHelper.isLogin : " + FireBaseHelper.isLogin);
        Log.d("test", "LoginHelper.isGuest : " + FireBaseHelper.isGuest);

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        //login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        loginDialog = new Dialog(MainActivity.this);
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginDialog.setContentView(R.layout.popup_login);
        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        selectDialog = new Dialog(MainActivity.this);
        selectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectDialog.setContentView(R.layout.popup_select);
        selectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        slotDialog = new Dialog(MainActivity.this);
        slotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        slotDialog.setContentView(R.layout.popup_slot);
        slotDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        uploadDialog = new Dialog(MainActivity.this);
        uploadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        uploadDialog.setContentView(R.layout.popup_upload);
        uploadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        downloadDialog = new Dialog(MainActivity.this);
        downloadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        downloadDialog.setContentView(R.layout.popup_download);
        downloadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        deleteDialog = new Dialog(MainActivity.this);
        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.setContentView(R.layout.popup_delete);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void loginDialogShow() {
        loginDialog.show();
        loginDialog.findViewById(R.id.signin_button).setOnClickListener(view -> {
            signIn();
        });
        loginDialog.findViewById(R.id.cancel_button).setOnClickListener(view -> {
            loginDialog.dismiss();
        });
    }

    DiarySlot[] diarySlots = new DiarySlot[3];
    TodoSlot[] todoSlots = new TodoSlot[3];

    public void selectDialogShow() {
        selectDialog.show();
        selectDialog.findViewById(R.id.select_diary).setOnClickListener(view -> {
            //get Data
            getDiaryData();
        });
        selectDialog.findViewById(R.id.select_todo).setOnClickListener(view -> {
            //get Data
            getTodoData();
        });
        selectDialog.findViewById(R.id.cancel_button).setOnClickListener(view -> {
            selectDialog.dismiss();
        });
    }

    private void getTodoData() {
        progressDialog.setMessage("save data 불러오는중");
        progressDialog.show();
        FireBaseHelper.userRef.child("todoSlot").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.getResult().getValue() == null) {
                            Log.d(TAG, "task.getResult().getValue() == null");
                        } else {
                            for (int i = 0; i < 3; i++) {
                                todoSlots[i] = task.getResult().child(FireBaseHelper.slotStrs[i]).getValue(TodoSlot.class);

                            }
                            for (TodoSlot test : todoSlots) {
                                if (test == null) {
                                    Log.d(TAG, "test == null");
                                } else {
                                    for (Todo todo : test.getTodoList()) {
                                        Log.d(TAG, "todo : " + todo.toString());
                                    }
                                    Log.d(TAG, "todo size : " + test.getTodoList().size());
                                }
                            }
                        }
                        progressDialog.dismiss();
                        slotDialogShow(DbHelper.TODO_TAG);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "네트워크를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getDiaryData() {
        progressDialog.setMessage("save data 불러오는중");
        progressDialog.show();
        FireBaseHelper.userRef.child("diarySlot").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.getResult().getValue() == null) {
                            Log.d(TAG, "task.getResult().getValue() == null");
                        } else {
                            for (int i = 0; i < 3; i++) {
                                diarySlots[i] = task.getResult().child(FireBaseHelper.slotStrs[i]).getValue(DiarySlot.class);
                            }
                            for (DiarySlot test : diarySlots) {
                                if (test == null) {
                                    Log.d(TAG, "test == null");
                                } else {
                                    for (Diary diary : test.getDiaryList()) {
                                        Log.d(TAG, "diary : " + diary.toString());
                                    }
                                    Log.d(TAG, "diary size : " + test.getDiaryList().size());
                                }
                            }
                        }
                        progressDialog.dismiss();
                        slotDialogShow(DbHelper.DIARY_TAG);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "네트워크를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void slotDialogShow(int tag) {
        slotDialog.show();
        TextView tiltleText = slotDialog.findViewById(R.id.title);

        RelativeLayout[] slotLayouts = new RelativeLayout[3];
        slotLayouts[0] = slotDialog.findViewById(R.id.slot1_layout);
        slotLayouts[0].setVisibility(View.VISIBLE);
        slotLayouts[1] = slotDialog.findViewById(R.id.slot2_layout);
        slotLayouts[1].setVisibility(View.VISIBLE);
        slotLayouts[2] = slotDialog.findViewById(R.id.slot3_layout);
        slotLayouts[2].setVisibility(View.VISIBLE);

        RelativeLayout[] emptyLayouts = new RelativeLayout[3];
        emptyLayouts[0] = slotDialog.findViewById(R.id.slot1_empty);
        emptyLayouts[1] = slotDialog.findViewById(R.id.slot2_empty);
        emptyLayouts[2] = slotDialog.findViewById(R.id.slot3_empty);

        TextView[] titles = new TextView[3];
        titles[0] = slotDialog.findViewById(R.id.slot1_title);
        titles[1] = slotDialog.findViewById(R.id.slot2_title);
        titles[2] = slotDialog.findViewById(R.id.slot3_title);

        TextView[] modeDates = new TextView[3];
        modeDates[0] = slotDialog.findViewById(R.id.slot1_moddate);
        modeDates[1] = slotDialog.findViewById(R.id.slot2_moddate);
        modeDates[2] = slotDialog.findViewById(R.id.slot3_moddate);

        if (tag == DbHelper.DIARY_TAG) {
            tiltleText.setText("upload and download : Diary");
            for (int i = 0; i < 3; i++) {
                int finalI = i;
                Log.d(TAG, "diarytest : " + (diarySlots[i] == null ? "null" : diarySlots[i].toString()));
                if (diarySlots[i] == null) {
                    slotLayouts[i].setVisibility(View.INVISIBLE);
                    emptyLayouts[i].setOnClickListener(view -> {
                        uploadDialogShow(tag, finalI);
                    });
                } else {
                    titles[i].setText(diarySlots[i].getTitle());
                    modeDates[i].setText(AppHelper.parsingDate(diarySlots[i].getModDate()));

                    slotLayouts[i].setOnClickListener(view -> {
                        downloadDialogShow(tag, finalI);
                    });
                }
            }
        } else if (tag == DbHelper.TODO_TAG) {
            tiltleText.setText("upload and download : Todo");
            for (int i = 0; i < 3; i++) {
                int finalI = i;
                if (todoSlots[i] == null) {
                    slotLayouts[i].setVisibility(View.INVISIBLE);
                    emptyLayouts[i].setOnClickListener(view -> {
                        uploadDialogShow(tag, finalI);
                    });
                } else {
                    titles[i].setText(todoSlots[i].getTitle());
                    modeDates[i].setText(AppHelper.parsingDate(todoSlots[i].getModDate()));

                    slotLayouts[i].setOnClickListener(view -> {
                        downloadDialogShow(tag, finalI);
                    });
                }
            }
        }


        slotDialog.findViewById(R.id.cancel_button).setOnClickListener(view -> {
            slotDialog.dismiss();
        });
    }

    public void uploadDialogShow(int tag, int i) {
        uploadDialog.show();
        TextView upload_title = uploadDialog.findViewById(R.id.title);
        upload_title.setText("upload : slot" + (i + 1));
        EditText edit_title = uploadDialog.findViewById(R.id.upload_title);
        uploadDialog.findViewById(R.id.ok_button).setOnClickListener(view -> {
            if (tag == DbHelper.DIARY_TAG) {
                saveDiaryData(i, edit_title.getText().toString());
            } else if (tag == DbHelper.TODO_TAG) {
                saveTodoData(i, edit_title.getText().toString());
            }
        });
        uploadDialog.findViewById(R.id.cancel_button).setOnClickListener(view -> {
            uploadDialog.dismiss();
        });
    }

    private void saveDiaryData(int i, String title) {
        progressDialog.setMessage("data 저장중");
        progressDialog.show();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("title", title);
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        childUpdates.put("modDate", simpleDateFormat.format(nowDate));
        childUpdates.put("diaryList", DbHelper.findAllDiary());
        FireBaseHelper.userRef.child("diarySlot").child(FireBaseHelper.slotStrs[i]).setValue(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        uploadDialog.dismiss();
                        downloadDialog.dismiss();
                        getDiaryData();
                        Log.d(TAG, "DiarySlot 저장 성공.");
                        Toast.makeText(MainActivity.this, "저장 성공!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.e(TAG, "DiarySlot 저장 실패.");
                        Toast.makeText(MainActivity.this, "저장 실패,네트워크를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveTodoData(int i, String title) {
        progressDialog.setMessage("data 저장중");
        progressDialog.show();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("title", title);
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        childUpdates.put("modDate", simpleDateFormat.format(nowDate));
        childUpdates.put("todoList", DbHelper.findAllTodo());
        FireBaseHelper.userRef.child("todoSlot").child(FireBaseHelper.slotStrs[i]).setValue(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        uploadDialog.dismiss();
                        downloadDialog.dismiss();
                        getTodoData();
                        Log.d(TAG, "TodoSlot 저장 성공.");
                        Toast.makeText(MainActivity.this, "저장 성공!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.e(TAG, "TodoSlot 저장 실패.");
                        Toast.makeText(MainActivity.this, "저장 실패,네트워크를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void downloadDialogShow(int tag, int i) {
        downloadDialog.show();

        EditText text_title = downloadDialog.findViewById(R.id.slot_title);
        if (tag == DbHelper.DIARY_TAG){
            text_title.setText(diarySlots[i].getTitle());
        } else {
            text_title.setText(todoSlots[i].getTitle());
        }

        TextView text_slot_title = downloadDialog.findViewById(R.id.slot_title);
        downloadDialog.findViewById(R.id.delete).setOnClickListener(view -> {
            deleteDialogShow(tag,i);
        });

        downloadDialog.findViewById(R.id.ok_button).setOnClickListener(view -> {
            //upload,갱신
            if (tag == DbHelper.DIARY_TAG){
                saveDiaryData(i,text_title.getText().toString());
            } else {
                saveTodoData(i,text_title.getText().toString());
            }
        });
        downloadDialog.findViewById(R.id.cancel_button).setOnClickListener(view -> {
            //download
            if (tag == DbHelper.DIARY_TAG){
                DbHelper.deleteAllDiary();
                for (Diary diary : diarySlots[i].getDiaryList()){
                    DbHelper.insertDiary(diary);
                }
            } else {
                DbHelper.deleteAllTodo();
                for (Todo todo : todoSlots[i].getTodoList()){
                    DbHelper.insertTodo(todo);
                }
            }
            Toast.makeText(MainActivity.this, "불러오기 완료!!", Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteDialogShow(int tag, int i) {
        deleteDialog.show();
        deleteDialog.findViewById(R.id.ok_button).setOnClickListener(view -> {removeData(tag,i);});
        deleteDialog.findViewById(R.id.cancel_button).setOnClickListener(view -> {deleteDialog.dismiss();});
    }

    private void removeData(int tag,int i) {
        progressDialog.setMessage("data 삭제중");
        progressDialog.show();

        FireBaseHelper.userRef.child(tag == DbHelper.DIARY_TAG?"diarySlot":"todoSlot").child(FireBaseHelper.slotStrs[i]).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        deleteDialog.dismiss();
                        downloadDialog.dismiss();
                        if(tag==DbHelper.DIARY_TAG){
                            getDiaryData();
                            Log.d(TAG, "DiarySlot 삭제 성공.");
                        }else{
                            getTodoData();
                            Log.d(TAG, "TodoSlot 삭제 성공.");
                        }
                        Toast.makeText(MainActivity.this, "삭제성공하셨습니다", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        if(tag==DbHelper.DIARY_TAG){
                            Log.e(TAG, "DiarySlot 삭제 실패.");
                        }else{
                            Log.e(TAG, "TodoSlot 삭제 실패.");
                        }
                        Toast.makeText(MainActivity.this, "저장 실패,네트워크를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
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
                            loginDialog.dismiss();
                            settingFragment.optionsSetting();
                            Toast.makeText(getApplicationContext(), FireBaseHelper.mUser.getEmail() + "님 환영합니다!", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    
}