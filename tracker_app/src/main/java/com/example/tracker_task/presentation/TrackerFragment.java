package com.example.tracker_task.presentation;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.authentication.AuthenticationEvent;
import com.example.tracker_task.R;
import com.example.tracker_task.databinding.FragmentTrackerBinding;

public class TrackerFragment extends Fragment {
    FragmentTrackerBinding binding = null;

    TrackerViewModel viewModel;

    TrackerState state;

    AuthenticationEvent authenticationEvent;

    public void setAuthenticationEvent(AuthenticationEvent authenticationEvent) {
        this.authenticationEvent = authenticationEvent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(TrackerViewModel.class);
        state = viewModel.getState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrackerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            state.set_isPermissionGranted(true);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            state.set_isPermissionGranted(false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.startStopButton.setOnClickListener(v -> {
            if (state.isPermissionGranted().getValue()) {
                if (state.isTracking()) {
                    viewModel.onEvent(new StopTracking());
                    showTrackerOffUI();
                } else {
                    viewModel.onEvent(new StartTracking());
                    showTrackerOnUI();
                }
            } else {
                askUserForOpeningAppSettings();
            }
        });

        binding.toolbarExitIcon.setOnClickListener(v -> {
            authenticationEvent.logOut();
        });

        state.getGpsStatusListener().observe(getViewLifecycleOwner(), isGpsEnabled -> {
            if (!isGpsEnabled) {
                showGPSOffUI();
            } else {
                if (state.isTracking()) {
                    showTrackerOnUI();
                } else {
                    showTrackerOffUI();
                }
            }
        });

        state.isPermissionGranted().observe(getViewLifecycleOwner(), isPermissionGranted -> {
            if (isPermissionGranted) {
                hideNeedPermissionMessage();
            } else {
                showNeedPermissionMessage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showTrackerOffUI() {
        binding.trackerOffInclude.getRoot().setVisibility(View.VISIBLE);
        binding.trackerWorkingInclude.getRoot().setVisibility(View.GONE);
        binding.trackerGpsOffInclude.getRoot().setVisibility(View.GONE);
        binding.startStopButton.setText(R.string.start_button_text);
        binding.startStopButton.setTextColor(0xffFFFFFF);
        binding.startStopButton.setBackgroundTintList(ColorStateList.valueOf(0xffFFAA00));
        binding.startStopButton.setEnabled(true);
    }

    private void showTrackerOnUI() {
        binding.trackerOffInclude.getRoot().setVisibility(View.GONE);
        binding.trackerWorkingInclude.getRoot().setVisibility(View.VISIBLE);
        binding.trackerGpsOffInclude.getRoot().setVisibility(View.GONE);
        binding.startStopButton.setText(R.string.stop_button_text);
        binding.startStopButton.setTextColor(0xffFFAA00);
        binding.startStopButton.setBackgroundTintList(ColorStateList.valueOf(0xffFFFFFF));
        binding.startStopButton.setEnabled(true);
    }

    private void showGPSOffUI() {
        binding.trackerOffInclude.getRoot().setVisibility(View.GONE);
        binding.trackerWorkingInclude.getRoot().setVisibility(View.GONE);
        binding.trackerGpsOffInclude.getRoot().setVisibility(View.VISIBLE);
        binding.startStopButton.setText(R.string.start_button_text);
        binding.startStopButton.setTextColor(0xffFFFFFF);
        binding.startStopButton.setBackgroundTintList(ColorStateList.valueOf(0xffFFAA00));
        binding.startStopButton.setEnabled(false);
    }

    private void showNeedPermissionMessage() {
        binding.startStopButton.setBackgroundColor(Color.GRAY);
        binding.startStopButton.setStrokeColor(ColorStateList.valueOf(Color.RED));
        binding.permissionMessage.setVisibility(View.VISIBLE);
    }

    private void hideNeedPermissionMessage() {
        binding.startStopButton.setBackgroundColor(getResources().getColor(R.color.web_orange));
        binding.startStopButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.web_orange)));
        binding.permissionMessage.setVisibility(View.GONE);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    hideNeedPermissionMessage();
                    state.set_isPermissionGranted(true);
                } else {
                    showNeedPermissionMessage();
                    state.set_isPermissionGranted(false);
                }
            });

    private void askUserForOpeningAppSettings() {
        Intent settingsIntent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getContext().getPackageName(), null)
        );

        if (requireContext().getPackageManager().resolveActivity(settingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            Toast.makeText(requireContext(), "Permission are denied forever, please check your settings!", Toast.LENGTH_LONG).show();
        } else {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Sorry, we can't start tracking your location!")
                    .setMessage("""
                            You have denied location permission forever.
                            But this is mandatory for using the application.
                            Be sure, we don't send your location to other companies or people (except my mentor Danil)!
                            You can change your decision in app settings.
                                              
                                                                
                            Would you like to open the app settings?
                            """
                    )
                    .setPositiveButton("Open", (dialog, which) -> {
                        startActivity(settingsIntent);
                    })
                    .create()
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}