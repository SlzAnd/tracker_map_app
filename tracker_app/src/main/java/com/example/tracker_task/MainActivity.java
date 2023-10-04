package com.example.tracker_task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.os.Bundle;

import com.example.authentication.AuthenticationEvent;
import com.example.authentication.LoginFragment;
import com.example.authentication.RegisterFragment;
import com.example.tracker_task.databinding.ActivityMainBinding;
import com.example.tracker_task.presentation.TrackerFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                0
        );

        // fragments
        LoginFragment loginFragment = new LoginFragment();
        RegisterFragment registerFragment = new RegisterFragment();
        TrackerFragment trackerFragment = new TrackerFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        AuthenticationEvent authenticationEvent = new AuthenticationEvent() {
            @Override
            public void onSuccessLogin() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.trackerFragmentContainer.getId(), trackerFragment);
                transaction.commit();
            }

            @Override
            public void onSuccessRegistration() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.trackerFragmentContainer.getId(), loginFragment);
                transaction.commit();
            }

            @Override
            public void onChangeToLoginScreen() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.trackerFragmentContainer.getId(), loginFragment);
                transaction.commit();
            }

            @Override
            public void onChangeToRegisterScreen() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.trackerFragmentContainer.getId(), registerFragment);
                transaction.commit();
            }
        };

        loginFragment.setAuthenticationEvent(authenticationEvent);
        registerFragment.setAuthenticationEvent(authenticationEvent);

        // first-viewed fragment - Login
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.trackerFragmentContainer.getId(), loginFragment);
        transaction.commit();
    }
}