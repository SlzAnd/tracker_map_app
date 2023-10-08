package com.example.authentication;

import com.google.firebase.auth.FirebaseAuth;

public abstract class AuthenticationEvent {

    abstract protected void onSuccessLogin();
    abstract protected void onSuccessRegistration();
    abstract protected void onChangeToLoginScreen();
    abstract protected void onChangeToRegisterScreen();
    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        onChangeToLoginScreen();
    }
}
