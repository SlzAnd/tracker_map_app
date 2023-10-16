//package com.example.map_app.java.presentation;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import java.time.LocalDate;
//import java.util.Objects;
//
//public class MapState {
//
//    private boolean isShownDialog = false;
//    private MutableLiveData<LocalDate> _selectedDate = new MutableLiveData<>(LocalDate.now());
//    private LiveData<LocalDate> selectedDate = _selectedDate;
//
//    private final MutableLiveData<PolylineOptions> _polyline = new MutableLiveData<>();
//    private final LiveData<PolylineOptions> polyline = _polyline;
//
//
//    public boolean isShownDialog() {
//        return isShownDialog;
//    }
//
//    public void setShownDialog(boolean shownDialog) {
//        isShownDialog = shownDialog;
//    }
//
//    public LiveData<PolylineOptions> getPolyline() {
//        return polyline;
//    }
//
//    public void setPolyline(PolylineOptions polyline) {
//        this._polyline.postValue(polyline);
//    }
//
//    public LiveData<LocalDate> getSelectedDate() {
//        return selectedDate;
//    }
//
//    public void setDate(LocalDate date) {
//        this._selectedDate.postValue(date);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MapState mapState = (MapState) o;
//        return isShownDialog == mapState.isShownDialog && Objects.equals(_selectedDate, mapState._selectedDate) && Objects.equals(selectedDate, mapState.selectedDate) && Objects.equals(_polyline, mapState._polyline) && Objects.equals(polyline, mapState.polyline);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(isShownDialog, _selectedDate, selectedDate, _polyline, polyline);
//    }
//}
