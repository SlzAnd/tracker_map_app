//package com.example.tracker_task.java;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//
//import com.example.authentication.AuthenticationEvent;
//import com.example.authentication.LoginFragment;
//import com.example.authentication.RegisterFragment;
//import com.example.tracker_task.databinding.ActivityMainBinding;
//import com.example.tracker_task.java.presentation.TrackerFragment;
//import com.example.tracker_task.java.presentation.TrackerViewModel;
//
//import dagger.hilt.android.AndroidEntryPoint;
//
//@AndroidEntryPoint
//public class MainActivity extends AppCompatActivity {
//
//    ActivityMainBinding binding = null;
//    TrackerViewModel viewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        viewModel = new ViewModelProvider(this).get(TrackerViewModel.class);
//        setContentView(binding.getRoot());
//
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    this,
//                    new String[]{
//                            Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.ACCESS_COARSE_LOCATION
//                    }, 0);
//        }
//
//        // fragments
//        LoginFragment loginFragment = new LoginFragment();
//        RegisterFragment registerFragment = new RegisterFragment();
//        TrackerFragment trackerFragment = new TrackerFragment();
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        AuthenticationEvent authenticationEvent = new AuthenticationEvent() {
//            @Override
//            public void onSuccessLogin() {
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(binding.trackerFragmentContainer.getId(), trackerFragment);
//                transaction.commit();
//            }
//
//            @Override
//            public void onSuccessRegistration() {
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(binding.trackerFragmentContainer.getId(), loginFragment);
//                transaction.commit();
//            }
//
//            @Override
//            public void onChangeToLoginScreen() {
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(binding.trackerFragmentContainer.getId(), loginFragment);
//                transaction.commit();
//            }
//
//            @Override
//            public void onChangeToRegisterScreen() {
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(binding.trackerFragmentContainer.getId(), registerFragment);
//                transaction.commit();
//            }
//        };
//
//        loginFragment.setAuthenticationEvent(authenticationEvent);
//        registerFragment.setAuthenticationEvent(authenticationEvent);
//        trackerFragment.setAuthenticationEvent(authenticationEvent);
//
//        // first-viewed fragment - Login
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(binding.trackerFragmentContainer.getId(), loginFragment);
//        transaction.commit();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(
//            int requestCode,
//            @NonNull String[] permissions,
//            @NonNull int[] grantResults
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0) {
//            if(requestCode == 0) {
//                viewModel.getState().set_isPermissionGranted(
//                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                        grantResults[1] == PackageManager.PERMISSION_GRANTED
//                );
//            }
//        }
//    }
//}