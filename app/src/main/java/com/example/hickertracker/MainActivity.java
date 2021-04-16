package com.example.hickertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView  latitudeTextView;
    TextView longitudeTextView;
    LocationManager locationManager;
    LocationListener locationListener;
    TextView altTextView,accTextView;
    TextView addTextView;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void updateLocationInfo(Location location){
        Log.i("Location info",location.toString());
        addTextView=(TextView)findViewById(R.id.addressTextView);
        altTextView.setText("Altitude : "+location.getAltitude());


        latitudeTextView.setText("Latitude : "+ String.valueOf(location.getLatitude()).substring(0,(String.valueOf(location.getLatitude()).length()/2)));
        longitudeTextView.setText("Longitude : "+String.valueOf(location.getLongitude()).substring(0,(String.valueOf(location.getLongitude()).length()/2)));
        accTextView.setText("Accuracy : "+location.getAccuracy());

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address = "Address could not be found ! ";

            List<Address> listAddress =geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if(listAddress!=null && listAddress.size()>0){
                if(listAddress.get(0).getAddressLine(0)!=null)
                {
                    address=""+listAddress.get(0).getAddressLine(0);
                    addTextView.setText("Address : "+address);
                }
              else
                  addTextView.setText("Address : "+address);

                       }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitudeTextView=(TextView)findViewById(R.id.latitudeTextView);
        longitudeTextView=(TextView)findViewById(R.id.longitudeTextView);
        accTextView=(TextView)findViewById(R.id.accuracyTextView);
        altTextView=(TextView)findViewById(R.id.altitudeTextView);
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(Build.VERSION.SDK_INT<23){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
        else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location!=null){
                    updateLocationInfo(location);
                }

            }
        }

    }
}