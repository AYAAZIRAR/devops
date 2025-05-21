package com.example.azirarquiz;

import android.location.Location;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.content.Context;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;

public class MapsViewModel extends ViewModel {
    private final MutableLiveData<Location> currentLocation = new MutableLiveData<>();
    private FusedLocationProviderClient fusedLocationClient;

    public LiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void getLastLocation(Context context) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            currentLocation.postValue(location);
                        }
                    });
        }
    }
}