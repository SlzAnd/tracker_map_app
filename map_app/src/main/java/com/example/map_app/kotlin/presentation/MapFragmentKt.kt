package com.example.map_app.kotlin.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.map_app.R
import com.example.map_app.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.atomic.AtomicReference

@AndroidEntryPoint
class MapFragmentKt : Fragment(), CalendarDialogKt.DatePickerEventListener {
    private var binding: FragmentMapsBinding? = null
    private val viewModel: MapViewModelKt by viewModels()
    private var isShownDialog = false
    private val polyline = AtomicReference<Polyline>()

    companion object {
        private var authenticationEvent: AuthenticationEventKt? = null
        fun setAuthenticationEvent(event: AuthenticationEventKt) {
            authenticationEvent = event
        }
    }

    private fun createOnMapReadyCallback(path: Flow<PolylineOptions>): OnMapReadyCallback {
        return OnMapReadyCallback { googleMap ->
            lifecycleScope.launch {
                path.collect {
                    if (it.points.isNotEmpty()) {
                        polyline.get()?.remove()
                        polyline.set(googleMap.addPolyline(it))
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLng(
                                it.points[0]
                            )
                        )
                    } else {
                        polyline.get()?.remove()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()

        // Log out
        binding!!.toolbarExitIcon.setOnClickListener {
            authenticationEvent?.logOut()
        }

        // Open calendar dialog
        binding!!.toolbarCalendarIcon.setOnClickListener {
            if (!isShownDialog) {
                lifecycleScope.launch {
                    viewModel.mapIntent.send(MapIntent.ShowDialog)
                }
            } else {
                lifecycleScope.launch {
                    viewModel.mapIntent.send(MapIntent.HideDialog)
                }
            }
        }
    }

    private fun observeViewModelState() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    MapStateKt.ShowDialog -> {
                        binding!!.calendarDialog.visibility = View.VISIBLE
                        isShownDialog = true
                    }

                    MapStateKt.HideDialog -> {
                        binding!!.calendarDialog.visibility = View.GONE
                        isShownDialog = false
                    }

                    is MapStateKt.ShowPath -> {
                        val mapFragment =
                            getChildFragmentManager().findFragmentById(R.id.map) as SupportMapFragment?
                        mapFragment?.getMapAsync(createOnMapReadyCallback(it.flow))
                        setupBottomMessage(it.date)
                    }
                }
            }
        }
    }

    private fun setupBottomMessage(selectedDate: LocalDate) {
        if (selectedDate.isEqual(LocalDate.now())) {
            binding!!.timeTextView.setText(R.string.last_24_h)
        } else {
            val pattern =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
            val formattedDate = selectedDate.format(pattern)
            binding!!.timeTextView.text = formattedDate
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCloseDialog() {
        lifecycleScope.launch {
            viewModel.mapIntent.send(MapIntent.HideDialog)
        }
    }

    override fun onSelectDate(date: LocalDate) {
        lifecycleScope.launch {
            viewModel.mapIntent.send(MapIntent.GetPath(date))
        }
    }
}