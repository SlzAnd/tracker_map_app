package com.example.map_app.kotlin.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.map_app.R
import com.example.map_app.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class MapFragmentKt : Fragment(), CalendarDialogKt.DatePickerEventListener {
    private var binding: FragmentMapsBinding? = null
    private val viewModel: MapViewModelKt by viewModels()
    private var isShownDialog = false

    companion object {
        private var authenticationEvent: AuthenticationEventKt? = null
        fun setAuthenticationEvent(event: AuthenticationEventKt) {
            authenticationEvent = event
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

        binding!!.composeMapFragment.setContent {
            val last24h = stringResource(id = R.string.last_24_h)
            var selectedDateText by rememberSaveable { mutableStateOf(last24h) }
            val cameraPositionState = rememberCameraPositionState()
            var points: List<LatLng>? = null

            fun setupBottomMessage(selectedDate: LocalDate) {
                selectedDateText = if (selectedDate.isEqual(LocalDate.now())) {
                    last24h
                } else {
                    val pattern =
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                    val formattedDate = selectedDate.format(pattern)
                    formattedDate
                }
            }

            fun observeViewModelState() {
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
                                it.flow.collect { polylineOptions ->
                                    val currentPoints = polylineOptions.points
                                    if (currentPoints.isNotEmpty()) {
                                        points = currentPoints
                                        cameraPositionState.move(
                                            CameraUpdateFactory.newLatLng(
                                                currentPoints[0]
                                            )
                                        )
                                    } else {
                                        points = null
                                    }
                                }
                                setupBottomMessage(it.date)
                            }
                        }
                    }
                }
            }

            observeViewModelState()

            Scaffold(
                topBar = {
                    AppBar(
                        modifier = Modifier,
                        exitButtonEvent = { authenticationEvent?.logOut() },
                        calendarButtonEvent = {
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
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f),
                        contentAlignment = Alignment.Center
                    ) {
                        GoogleMap(
                            cameraPositionState = cameraPositionState
                        ) {
                            Polyline(points = points ?: emptyList())
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(83.dp)
                            .background(colorResource(id = R.color.dark_gray)),
                        contentAlignment = Alignment.Center
                    ) {
                        BottomClockIcon(
                            modifier = Modifier,
                            selectedDateText = selectedDateText
                        )
                    }
                }
            }
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