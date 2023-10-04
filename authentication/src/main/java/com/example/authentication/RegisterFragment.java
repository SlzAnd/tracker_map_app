package com.example.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends BaseFragment {

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
        return View.GONE;
    }

    @Override
    protected String getTitle() {
        return "Sign up";
    }

    @Override
    protected String getButtonText() {
        return "SIGN UP";
    }

    @Override
    protected String getBottomNavigationLink() {
        return "Sign in";
    }

    @Override
    protected String getBottomNavigationText() {
        return "Already have an account?";
    }

    @Override
    protected void onCLickButtonEvent(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authenticationEvent.onSuccessRegistration();
                    } else {
                        Toast.makeText(getContext(), "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onClickLinkEvent() {
        authenticationEvent.onChangeToLoginScreen();
    }
}
