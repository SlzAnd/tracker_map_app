package com.example.map_app.kotlin.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.map_app.R
import com.example.map_app.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Polyline
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.atomic.AtomicReference

@AndroidEntryPoint
class MapFragmentKt : Fragment(), CalendarDialogKt.DatePickerEventListener {
    lateinit var authenticationEvent: AuthenticationEventKt
    private var binding: FragmentMapsBinding? = null
    private val viewModel: MapViewModelKt by viewModels()
    private lateinit var state: MapStateKt

    private val callback = OnMapReadyCallback { googleMap ->
        state.setDate(LocalDate.now())
        val polyline = AtomicReference<Polyline>()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                polyline.get()?.remove()
                val polylineOpt = viewModel.getPath(LocalDate.now()).last()
                if (polylineOpt.points.isNotEmpty()) {
                    polyline.set(googleMap.addPolyline(polylineOpt))
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            polylineOpt.points[0]
                        )
                    )
                }
            }
        }

        state.selectedDate.observe(viewLifecycleOwner) { date ->
            lifecycleScope.launch {
                val polylineOpt = viewModel.getPath(date).last()
                if (polylineOpt.points.isNotEmpty()) {
                    polyline.get()?.remove()
                    polyline.set(googleMap.addPolyline(polylineOpt))
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            polylineOpt.points[0]
                        )
                    )
                } else {
                    polyline.get()?.remove()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = viewModel.state
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
        val mapFragment =
            getChildFragmentManager().findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Log out
        binding!!.toolbarExitIcon.setOnClickListener {
            authenticationEvent.logOut()
        }


        // Open calendar dialog
        binding!!.toolbarCalendarIcon.setOnClickListener {
            if (!state.isShownDialog) {
                binding!!.calendarDialog.visibility = View.VISIBLE
                state.isShownDialog = true
            } else {
                binding!!.calendarDialog.visibility = View.GONE
                state.isShownDialog = false
            }
        }

        //bottom message -> selected date
        state.selectedDate.observe(getViewLifecycleOwner()) { selectedDate: LocalDate ->
            if (selectedDate.isEqual(LocalDate.now())) {
                binding!!.timeTextView.setText(R.string.last_24_h)
            } else {
                val pattern =
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                val formattedDate = selectedDate.format(pattern)
                binding!!.timeTextView.text = formattedDate
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onCloseDialog() {
        binding!!.calendarDialog.visibility = View.GONE
    }

    override fun onSelectDate(date: LocalDate) {
        state.setDate(date)
    }
}