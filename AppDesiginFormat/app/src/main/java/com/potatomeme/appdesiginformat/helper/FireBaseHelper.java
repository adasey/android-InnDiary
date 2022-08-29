package com.potatomeme.appdesiginformat.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.potatomeme.appdesiginformat.entity.Diary;
import com.potatomeme.appdesiginformat.entity.DiarySlot;
import com.potatomeme.appdesiginformat.entity.Todo;
import com.potatomeme.appdesiginformat.entity.TodoSlot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseHelper {

    private static String TAG = "LoginHelper";
    public static boolean isLogin;
    public static boolean isGuest;

    public static FirebaseAuth mAuth;
    public static FirebaseUser mUser;

    public static DatabaseReference userRef;

    public static String[] slotStrs = {"slot1","slot2","slot3"};

    public static void setting(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            isLogin = true;
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child("uid").child(mUser.getUid());
            Log.d(TAG, "mUser != null");
        }else{
            isLogin = false;
            Log.d(TAG, "mUser is null");
        }
    }


    public static void databaseSetting(){
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("uid").child(mUser.getUid()).child("diarySlot").child("slot1");

        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("title","testtitle");
        childUpdates.put("modDate","testDate");
        childUpdates.put("diaryList",DbHelper.findAllDiary());

        //hildUpdates.put("TodoSloat",sloatValuse);
        //List<Todo> testTodoList = DbHelper.findAllTodo();
        userRef.setValue(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "format 저장 성공.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "format 저장 실패.");
                    }
                });
    }




    public static void saveDiarySlot(int i,String title,String date) {
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("uid").child(mUser.getUid()).child("diarySlot").child(slotStrs[i]);
        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("title",title);
        childUpdates.put("modDate",date);
        childUpdates.put("diaryList",DbHelper.findAllDiary());
        userRef.setValue(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DiarySlot 저장 성공.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "DiarySlot 저장 실패.");
                    }
                });
    }

    public static void saveTodoSlot(int i,String title,String date) {
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("uid").child(mUser.getUid()).child("todoSlot").child(slotStrs[i]);
        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put("title",title);
        childUpdates.put("modDate",date);
        childUpdates.put("todoList",DbHelper.findAllTodo());
        userRef.setValue(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "TodoSlot 저장 성공.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "TodoSlot 저장 실패.");
                    }
                });
    }

    public static void removeDiarySlot(int i) {
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("uid").child(mUser.getUid()).child("diarySlot").child(slotStrs[i]);
        userRef.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DiarySlot 삭제 성공.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "DiarySlot 삭제 실패.");
                    }
                });
    }

    public static void removeTodoSlot(int i) {
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("uid").child(mUser.getUid()).child("todoSlot").child(slotStrs[i]);
        userRef.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "TodoSlot 저장 성공.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "TodoSlot 저장 실패.");
                    }
                });
    }



    public static void login(){
        isLogin = true;
        isGuest = false;
        mUser = mAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("uid").child(mUser.getUid());
    }

    public static void logout(){
        mAuth.signOut();
        isLogin = false;
        useGuest();
    }

    public static void useGuest(){
        isGuest = true;
    }

}
