package com.example.map_app.kotlin.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.map_app.databinding.DialogCalendarBinding
import java.time.LocalDate

class CalendarDialogKt : DialogFragment() {

    private var binding: DialogCalendarBinding? = null
    private var datePickerEventListener: DatePickerEventListener? = null
    private var selectedDate = LocalDate.now()

    private val TAG = "CalendarDialogKt"

    interface DatePickerEventListener {
        fun onCloseDialog()

        fun onSelectDate(date: LocalDate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
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

        binding!!.calendarView.setOnDateChangeListener { _, year, month, day ->
            selectedDate = LocalDate.of(year, month + 1, day)
        }

        binding!!.okButton.setOnClickListener {
            if (selectedDate.isAfter(LocalDate.now())) {
                Toast.makeText(
                    requireContext(),
                    "You can't choose future date!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                datePickerEventListener?.onSelectDate(selectedDate)
                datePickerEventListener?.onCloseDialog()
            }
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