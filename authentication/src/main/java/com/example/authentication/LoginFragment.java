package com.example.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends BaseFragment {
    private FirebaseAuth mAuth;
    private AuthenticationEvent authenticationEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            authenticationEvent.onSuccessLogin();
        }
    }

    public void setAuthenticationEvent(AuthenticationEvent authenticationEvent) {
        this.authenticationEvent = authenticationEvent;
    }

    @Override
    protected int getForgotPasswordVisibility() {
        return View.VISIBLE;
    }

    @Override
    protected String getTitle() {
        return "Sign in";
    }

    @Override
    protected String getButtonText() {
        return "SIGN IN";
    }

    @Override
    protected String getBottomNavigationLink() {
        return "Sign Up";
    }

    @Override
    protected String getBottomNavigationText() {
        return "Don't have an account?";
    }

    @Override
    protected void onCLickButtonEvent(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authenticationEvent.onSuccessLogin();
                    } else {
                        Toast.makeText(getContext(), "Wrong email or password!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onClickLinkEvent() {
        authenticationEvent.onChangeToRegisterScreen();
    }
}
