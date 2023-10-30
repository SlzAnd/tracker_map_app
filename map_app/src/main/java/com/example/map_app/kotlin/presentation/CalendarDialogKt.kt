package com.example.map_app.kotlin.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import com.example.map_app.databinding.DialogCalendarBinding
import java.time.LocalDate


class CalendarDialogKt : DialogFragment() {

    private var binding: DialogCalendarBinding? = null
    private var datePickerEventListener: DatePickerEventListener? = null

    private val TAG = "CalendarDialogKt"

    interface DatePickerEventListener {
        fun onCloseDialog()

        fun onSelectDate(date: LocalDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCalendarBinding.inflate(inflater, container, false)
        return binding!!.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.composeDatePicker.setContent {
            val selectedDate = remember { mutableStateOf(LocalDate.now()) }

            CalendarDialog(
                modifier = Modifier.padding(horizontal = 20.dp),
                onDateSelected = {
                    selectedDate.value = it
                },
                onOkButtonEvent = {
                    if (selectedDate.value.isAfter(LocalDate.now())) {
                        Toast.makeText(
                            requireContext(),
                            "You can't choose future date!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        datePickerEventListener?.onSelectDate(selectedDate.value)
                        datePickerEventListener?.onCloseDialog()
                    }
                }
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            datePickerEventListener = parentFragment as DatePickerEventListener
            Log.d(TAG, "onAttach: $datePickerEventListener")
        } catch (e: ClassCastException) {
            Log.d(TAG, "onAttach: ClassCastException : " + e.message)
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}