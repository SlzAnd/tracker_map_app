//package com.example.map_app.java.presentation;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.example.map_app.databinding.DialogCalendarBinding;
//
//import java.time.LocalDate;
//
//public class CalendarDialog extends DialogFragment {
//    DialogCalendarBinding binding = null;
//    LocalDate selectedDate = LocalDate.now();
//
//    private static final String TAG = "CalendarDialog";
//
//    public interface DatePickerEventListener {
//        void onCloseDialog();
//
//        void onSelectDate(LocalDate date);
//    }
//
//    public DatePickerEventListener datePickerEventListener;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DialogCalendarBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        binding.calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
//            selectedDate = LocalDate.of(year, month + 1, day);
//        });
//
//        binding.okButton.setOnClickListener(view1 -> {
//            if (selectedDate.isAfter(LocalDate.now())) {
//                Toast.makeText(requireContext(), "You can't choose future date!", Toast.LENGTH_SHORT).show();
//            } else {
//                datePickerEventListener.onSelectDate(selectedDate);
//                datePickerEventListener.onCloseDialog();
//            }
//        });
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            datePickerEventListener = (DatePickerEventListener) getParentFragment();
//            Log.d(TAG, "onAttach: " + datePickerEventListener);
//        } catch (ClassCastException e) {
//            Log.d(TAG, "onAttach: ClassCastException : " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        binding = null;
//        super.onDestroy();
//    }
//}
