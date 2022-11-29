package com.example.shippermanager.Model;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class LocationService extends Service {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private Shipper shipper;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.v("location", "= null");
                return;
            }
            for (Location location : locationResult.getLocations()) {
                Log.v("location", location.getLatitude() + "-" + location.getLongitude());
                databaseReference.child(shipper.Id).child("KinhDo").setValue(location.getLatitude());
                databaseReference.child(shipper.Id).child("ViDo").setValue(location.getLongitude());
            }
        }
    };

    int TIME_INTERVAL = 10000;
    int FASTEST_TIME_INTERVAL = 5000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Shipper");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //setting update location
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(TIME_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_TIME_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //get shipper
        String json = HelperUtils.GetSharedObject(this, "Shipper");
        Gson gson = new Gson();
        shipper = gson.fromJson(json, Shipper.class);
        checkSettingAndStartLocationUpdate();
        return START_STICKY;
    }

    private void getLastLocation() {
//        Task<Location> locationTask = fusedLocationClient.getLastLocation();
//        locationTask.addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                // Got last known location. In some rare situations this can be null.
//                if (location != null) {
//                    Log.v("location google service", String.valueOf(location.getLatitude()));
//                }else
//                {
//                    Log.d("TAG", "onSuccess: location was null");
//                }
//            }
//        });
//        locationTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("fail","on failure");
//            }
//        });
    }

    private void checkSettingAndStartLocationUpdate() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdate();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("location service", e.getMessage());
            }
        });

    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


}
