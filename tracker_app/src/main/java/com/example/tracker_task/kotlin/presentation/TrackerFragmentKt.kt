package com.example.tracker_task.kotlin.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.authentication.ui.theme.poppinsFontFamily
import com.example.tracker_task.R
import com.example.tracker_task.databinding.FragmentTrackerBinding
import com.example.tracker_task.kotlin.hasLocationPermission
import com.example.tracker_task.kotlin.ui.theme.webOrange
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackerFragmentKt : Fragment() {
    private var binding: FragmentTrackerBinding? = null
    private val viewModel: TrackerViewModelKt by viewModels()
    private var state: TrackerStateKt? = null
    private var dialog: AlertDialog? = null

    companion object {
        private var authenticationEvent: AuthenticationEventKt? = null
        fun setAuthenticationEvent(event: AuthenticationEventKt) {
            authenticationEvent = event
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
        binding = FragmentTrackerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        if (requireContext().hasLocationPermission()) {
            viewModel.onEvent(TrackerEventKt.SetPermissionState(true))
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            viewModel.onEvent(TrackerEventKt.SetPermissionState(false))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.composeTrackerFragment.setContent {

            val start = stringResource(id = R.string.start_button_text)
            val stop = stringResource(id = R.string.stop_button_text)
            var buttonColor by rememberSaveable { mutableLongStateOf(0xffFFAA00) }
            var buttonBorderColor by rememberSaveable { mutableLongStateOf(0xffFFAA00) }
            var buttonText by rememberSaveable { mutableStateOf(start) }
            var buttonTextColor by rememberSaveable { mutableLongStateOf(0xFFFFFFFF) }
            var isPermissionGranted by rememberSaveable { mutableStateOf(false) }
            var isGpsOff by rememberSaveable { mutableStateOf(false) }

            fun showTrackerOnUI() {
                buttonColor = 0xFFFFFFFF //white
                buttonText = stop
                buttonTextColor = 0xffFFAA00 //web_orange
            }

            fun showTrackerOffUI() {
                buttonColor = 0xffFFAA00 //web_orange
                buttonText = start
                buttonTextColor = 0xFFFFFFFF //white
            }

            fun showNeedPermissionButtonUI() {
                buttonColor = 0xffACACAC // grey
                buttonBorderColor = 0xffFF0000 // red
            }

            fun hideNeedPermissionButtonUI() {
                dialog?.hide()
                if (state!!.isTracking) {
                    showTrackerOnUI()
                } else {
                    showTrackerOffUI()
                }
                buttonBorderColor = 0xffFFAA00 //web_orange
            }

            state!!.gpsStatusListener?.observe(viewLifecycleOwner) { isGpsEnabled ->
                isGpsOff = !isGpsEnabled
            }

            state!!.isPermissionGranted.observe(viewLifecycleOwner) { isGranted ->
                isPermissionGranted = isGranted
            }

            Scaffold(
                topBar = {
                    AppBar(
                        modifier = Modifier,
                        exitButtonEvent = { authenticationEvent?.logOut() }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 185.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if (isGpsOff) {
                            GpsOffIcon()
                        } else {
                            if (state!!.isTracking) {
                                TrackerOnIcon()
                            } else {
                                TrackerOffIcon()
                            }
                        }

                        if (!isPermissionGranted) {
                            showNeedPermissionButtonUI()
                            Text(
                                modifier = Modifier
                                    .padding(top = 20.dp),
                                text = stringResource(id = R.string.permission_message),
                                fontFamily = poppinsFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W600,
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            hideNeedPermissionButtonUI()
                        }
                    }

                    Button(
                        modifier = Modifier
                            .padding(bottom = 81.dp)
                            .width(315.dp)
                            .height(62.dp),
                        onClick = {
                            if (state!!.isPermissionGranted.value!!) {
                                if (state!!.isTracking) {
                                    viewModel.onEvent(TrackerEventKt.StopTrackingKt)
                                    showTrackerOffUI()
                                } else {
                                    viewModel.onEvent(TrackerEventKt.StartTrackingKt)
                                    showTrackerOnUI()
                                }
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                askUserForOpeningAppSettings()
                            }
                        },
                        enabled = !isGpsOff,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(buttonColor),
                            disabledContainerColor = webOrange
                        ),
                        shape = RoundedCornerShape(15.dp),
                        border = BorderStroke(3.dp, Color(buttonBorderColor))
                    ) {
                        Text(
                            text = buttonText,
                            color = Color(buttonTextColor)
                        )
                    }
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.onEvent(TrackerEventKt.SetPermissionState(true))
        } else {
            viewModel.onEvent(TrackerEventKt.SetPermissionState(false))
        }
    }

    private fun askUserForOpeningAppSettings() {
        val settingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )

        if (requireContext().packageManager.resolveActivity(
                settingsIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            Toast.makeText(
                requireContext(),
                "Permission are denied forever, please check your settings!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            dialog = AlertDialog.Builder(requireContext())
                .setTitle("Sorry, we can't start tracking your location!")
                .setMessage(
                    """
                            You have denied location permission forever.
                            But this is mandatory for using the application.
                            Be sure, we don't send your location to other companies or people!
                            You can change your decision in app settings.
                                              
                                                                
                            Would you like to open the app settings?
                            
                            """
                        .trimIndent()
                )
                .setPositiveButton(
                    "Open"
                ) { _, _ ->
                    startActivity(settingsIntent)
                }
                .create()
            dialog!!.show()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}