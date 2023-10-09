package com.example.tracker_task.presentation;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            hideNeedPermissionMessage();
        } else {
            showNeedPermissionMessage();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.startStopButton.setOnClickListener(v -> {
            if (state.isTracking()) {
                viewModel.onEvent(new StopTracking());
                showTrackerOffUI();
            } else {
                viewModel.onEvent(new StartTracking());
                showTrackerOnUI();
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
        binding.startStopButton.setEnabled(false);
        binding.startStopButton.setBackgroundColor(Color.GRAY);
        binding.startStopButton.setStrokeColor(ColorStateList.valueOf(Color.RED));
        binding.permissionMessage.setVisibility(View.VISIBLE);
    }

    private void hideNeedPermissionMessage() {
        binding.startStopButton.setEnabled(true);
        binding.startStopButton.setBackgroundColor(getResources().getColor(R.color.web_orange));
        binding.startStopButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.web_orange)));
        binding.permissionMessage.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}