package com.example.map_app.java.presentation;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.authentication.AuthenticationEvent;
import com.example.map_app.R;
import com.example.map_app.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.atomic.AtomicReference;


public class MapFragment extends Fragment implements CalendarDialog.DatePickerEventListener {

    private AuthenticationEvent authenticationEvent;
    private MapViewModel viewModel;
    private MapState state;
    FragmentMapsBinding binding = null;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            viewModel.getPath(viewModel.state.getSelectedDate().getValue());
            AtomicReference<Polyline> polyline = new AtomicReference<>();

            state.getPolyline().observe(getViewLifecycleOwner(), polylineOptions -> {
                if (polylineOptions != null) {
                    polyline.set(googleMap.addPolyline(polylineOptions));
                    if (!polylineOptions.getPoints().isEmpty()) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(polylineOptions.getPoints().get(0)));
                    }
                } else {
                    Polyline p = polyline.get();
                    if (p != null) {
                        p.remove();
                    }
                }
            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        state = viewModel.state;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        // Log out
        binding.toolbarExitIcon.setOnClickListener(v -> {
            authenticationEvent.logOut();
        });

        // Open calendar dialog
        binding.toolbarCalendarIcon.setOnClickListener(v -> {
            if (!state.isShownDialog()) {
                binding.calendarDialog.setVisibility(View.VISIBLE);
                state.setShownDialog(true);
            } else {
                binding.calendarDialog.setVisibility(View.GONE);
                state.setShownDialog(false);
            }
        });

        //bottom message -> selected date
        state.getSelectedDate().observe(getViewLifecycleOwner(), selectedDate -> {
            if (selectedDate.isEqual(LocalDate.now())) {
                binding.timeTextView.setText(R.string.last_24_h);
            } else {
                DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
                String formattedDate = selectedDate.format(pattern);
                binding.timeTextView.setText(formattedDate);
            }
        });
    }

    public void setAuthenticationEvent(AuthenticationEvent event) {
        this.authenticationEvent = event;
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }

    @Override
    public void onCloseDialog() {
        binding.calendarDialog.setVisibility(View.GONE);
    }

    @Override
    public void onSelectDate(LocalDate date) {
        state.setDate(date);
        viewModel.getPath(date);
    }
}