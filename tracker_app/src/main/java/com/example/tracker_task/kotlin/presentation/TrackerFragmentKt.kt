package com.example.tracker_task.kotlin.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.authentication.kotlin.AuthenticationEventKt
import com.example.tracker_task.R
import com.example.tracker_task.databinding.FragmentTrackerBinding
import com.example.tracker_task.kotlin.hasLocationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackerFragmentKt : Fragment() {
    private var binding: FragmentTrackerBinding? = null
    private val viewModel: TrackerViewModelKt by viewModels()
    private lateinit var state: TrackerStateKt
    lateinit var authenticationEvent: AuthenticationEventKt

    private var dialog: AlertDialog? = null

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

        binding!!.startStopButton.setOnClickListener {
            if (state.isPermissionGranted.value!!) {
                if (state.isTracking) {
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
        }

        binding!!.toolbarExitIcon.setOnClickListener { authenticationEvent.logOut() }

        state.gpsStatusListener?.observe(viewLifecycleOwner) { isGpsEnabled ->
            if (!isGpsEnabled) {
                showGPSOffUI()
            } else {
                if (state.isTracking) {
                    showTrackerOnUI()
                } else {
                    showTrackerOffUI()
                }
            }
        }

        state.isPermissionGranted.observe(viewLifecycleOwner) { isGranted ->
            if (isGranted) {
                hideNeedPermissionMessage()
            } else {
                showNeedPermissionMessage()
            }
        }
    }

    private fun showTrackerOffUI() {
        binding!!.trackerOffInclude.root.visibility = View.VISIBLE
        binding!!.trackerWorkingInclude.root.visibility = View.GONE
        binding!!.trackerGpsOffInclude.root.visibility = View.GONE
        binding!!.startStopButton.setText(R.string.start_button_text)
        binding!!.startStopButton.setTextColor(-0x1)
        binding!!.startStopButton.backgroundTintList = ColorStateList.valueOf(-0x5600)
        binding!!.startStopButton.isEnabled = true
    }

    private fun showTrackerOnUI() {
        binding!!.trackerOffInclude.root.visibility = View.GONE
        binding!!.trackerWorkingInclude.root.visibility = View.VISIBLE
        binding!!.trackerGpsOffInclude.root.visibility = View.GONE
        binding!!.startStopButton.setText(R.string.stop_button_text)
        binding!!.startStopButton.setTextColor(-0x5600)
        binding!!.startStopButton.backgroundTintList = ColorStateList.valueOf(-0x1)
        binding!!.startStopButton.isEnabled = true
    }

    private fun showGPSOffUI() {
        binding!!.trackerOffInclude.root.visibility = View.GONE
        binding!!.trackerWorkingInclude.root.visibility = View.GONE
        binding!!.trackerGpsOffInclude.root.visibility = View.VISIBLE
        binding!!.startStopButton.setText(R.string.start_button_text)
        binding!!.startStopButton.setTextColor(-0x1)
        binding!!.startStopButton.backgroundTintList = ColorStateList.valueOf(-0x5600)
        binding!!.startStopButton.isEnabled = false
    }

    private fun showNeedPermissionMessage() {
        binding!!.startStopButton.setBackgroundColor(Color.GRAY)
        binding!!.startStopButton.strokeColor = ColorStateList.valueOf(Color.RED)
        binding!!.permissionMessage.visibility = View.VISIBLE
    }

    private fun hideNeedPermissionMessage() {
        dialog?.hide()
        binding!!.startStopButton.setBackgroundColor(resources.getColor(R.color.web_orange))
        binding!!.startStopButton.strokeColor =
            ColorStateList.valueOf(resources.getColor(R.color.web_orange))
        binding!!.permissionMessage.visibility = View.GONE
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            hideNeedPermissionMessage()
            viewModel.onEvent(TrackerEventKt.SetPermissionState(true))
        } else {
            showNeedPermissionMessage()
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