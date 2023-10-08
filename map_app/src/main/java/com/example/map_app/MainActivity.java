package com.example.map_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.authentication.AuthenticationEvent;
import com.example.authentication.LoginFragment;
import com.example.authentication.RegisterFragment;
import com.example.map_app.databinding.ActivityMainBinding;
import com.example.map_app.presentation.MapFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // fragments
        LoginFragment loginFragment = new LoginFragment();
        RegisterFragment registerFragment = new RegisterFragment();
        MapFragment mapFragment = new MapFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        AuthenticationEvent authenticationEvent = new AuthenticationEvent() {
            @Override
            public void onSuccessLogin() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.mapsFragmentContainer.getId(), mapFragment);
                transaction.commit();
            }

            @Override
            public void onSuccessRegistration() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.mapsFragmentContainer.getId(), loginFragment);
                transaction.commit();
            }

            @Override
            public void onChangeToLoginScreen() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.mapsFragmentContainer.getId(), loginFragment);
                transaction.commit();
            }

            @Override
            public void onChangeToRegisterScreen() {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(binding.mapsFragmentContainer.getId(), registerFragment);
                transaction.commit();
            }
        };

        loginFragment.setAuthenticationEvent(authenticationEvent);
        registerFragment.setAuthenticationEvent(authenticationEvent);
        mapFragment.setAuthenticationEvent(authenticationEvent);

        // first-viewed fragment - Login
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(binding.mapsFragmentContainer.getId(), loginFragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}