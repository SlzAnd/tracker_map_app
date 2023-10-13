//package com.example.tracker_task.java.data;
//
//import android.content.Context;
//import android.location.Location;
//import android.util.Log;
//
//import androidx.hilt.work.HiltWorker;
//import androidx.work.Constraints;
//import androidx.work.NetworkType;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.WorkManager;
//import androidx.work.WorkRequest;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.example.authentication.CurrentUserInfo;
//import com.example.tracker_task.java.data.data_source.LocationDAO;
//import com.example.tracker_task.java.domain.model.MyLocation;
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import dagger.assisted.Assisted;
//import dagger.assisted.AssistedInject;
//import io.reactivex.rxjava3.annotations.NonNull;
//
//public class SenderImpl implements Sender {
//    private static final String TAG = "SENDER_Impl";
//    static LocationDAO dao;
//    static Context context;
//    static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//    private static boolean isFirstPostponed = true;
//
//    public SenderImpl(LocationDAO dao, Context context) {
//        this.dao = dao;
//        this.context = context;
//    }
//
//    @Override
//    public void sendToFirebaseImmediately(Location location) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("latitude", location.getLatitude());
//        data.put("longitude", location.getLongitude());
//        data.put("time", Timestamp.now());
//        sendToFirebase(data);
//    }
//
//    private static void sendToFirebase(Map<String, Object> data) {
//        String userUID = CurrentUserInfo.getUserUID();
//        if (userUID != null) {
//            firestore.collection(userUID)
//                    .add(data)
//                    .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
//                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
//        }
//    }
//
//    @Override
//    public void sendToFirebasePostponed(Location location) {
//        MyLocation loc = new MyLocation(location.getLatitude(), location.getLongitude());
//        dao.insert(loc);
//        if (isFirstPostponed) {
//            Constraints constraints = new Constraints.Builder()
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .build();
//
//            WorkRequest sendRequest =
//                    new OneTimeWorkRequest.Builder(SendWorker.class)
//                            .setConstraints(constraints)
//                            .build();
//
//            WorkManager
//                    .getInstance(context)
//                    .enqueue(sendRequest);
//            isFirstPostponed = false;
//        }
//    }
//
//    @HiltWorker
//    public static class SendWorker extends Worker {
//        @AssistedInject
//        public SendWorker(
//                @Assisted @NonNull Context context,
//                @Assisted @NonNull WorkerParameters params) {
//            super(context, params);
//        }
//
//        @Override
//        public Result doWork() {
//            List<MyLocation> myLocations = dao.getAllLocations();
//            List<HashMap<String, Object>> dataList = new ArrayList<>();
//            Log.d(TAG, "" + myLocations);
//
//            if (!myLocations.isEmpty()) {
//                myLocations.forEach(myLocation -> {
//                    HashMap<String, Object> location = new HashMap<>();
//                    location.put("latitude", myLocation.latitude);
//                    location.put("longitude", myLocation.longitude);
//                    location.put("time", Timestamp.now());
//                    dataList.add(location);
//                });
//
//                dataList.forEach(SenderImpl::sendToFirebase);
//                dao.deleteAll();
//            }
//            isFirstPostponed = true;
//            return Result.success();
//        }
//    }
//}
