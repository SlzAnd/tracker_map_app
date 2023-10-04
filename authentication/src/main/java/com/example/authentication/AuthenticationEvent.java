package com.example.authentication;

public interface AuthenticationEvent {

    public void onSuccessLogin();
    public void onSuccessRegistration();
    public void onChangeToLoginScreen();

    public void onChangeToRegisterScreen();
}
