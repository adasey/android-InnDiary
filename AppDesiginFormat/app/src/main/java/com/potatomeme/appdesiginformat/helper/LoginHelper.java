package com.potatomeme.appdesiginformat.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginHelper {
    public static boolean isLogin;
    public static boolean isGuest;

    public static FirebaseAuth mAuth;
    public static FirebaseUser mUser;

    public static DatabaseReference userRef;

    public static void setting(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            isLogin = true;
            //DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            //userRef = mRootRef.child(mUser.getUid());
        }else{
            isLogin = false;
        }
    }

    public static void login(){
        isLogin = true;
        isGuest = false;
        mUser = mAuth.getCurrentUser();
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
