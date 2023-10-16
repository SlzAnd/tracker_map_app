//package com.example.map_app.java.data;
//
//
//import com.example.authentication.CurrentUserInfo;
//import com.example.map_app.java.domain.PathRepository;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import io.reactivex.rxjava3.core.Observable;
//
//public class PathRepositoryImpl implements PathRepository {
//
//    private static final String LAT = "latitude";
//    private static final String LNG = "longitude";
//
//    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//    @Override
//    public Observable<LatLng> getPath(LocalDate date) {
//        return Observable.create(emitter -> {
//            String collectionName = CurrentUserInfo.getUserUID();
//            firestore.collection(collectionName)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            List<DocumentSnapshot> tasks = task.getResult().getDocuments().stream()
//                                    .filter(documentSnapshot -> convertToLocalDate(documentSnapshot.getTimestamp("time")).isEqual(date))
//                                    .collect(Collectors.toList());
//                            if (tasks.isEmpty()) {
//                                emitter.onComplete();
//                            } else {
//                                tasks.forEach(documentSnapshot -> emitter.onNext(new LatLng(documentSnapshot.getDouble(LAT), documentSnapshot.getDouble(LNG))));
//                            }
//                        } else if (task.isComplete()) {
//                            emitter.onComplete();
//                        } else {
//                            emitter.tryOnError(new IllegalStateException("Firebase task was cancelled"));
//                        }
//                    });
//        });
//    }
//
//    private LocalDate convertToLocalDate(Timestamp timestamp) {
//        ZoneId zoneId = ZoneId.systemDefault();
//        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getSeconds() * 1000 + timestamp.getNanoseconds() / 1_000_000), zoneId).toLocalDate();
//    }
//}
