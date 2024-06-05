package com.example.weatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String API_ID = "ad20f5d222df84046f0527c28dc76ba8";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;


    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView weatherState, Temperature, Humidity, latitudeLongitude, addressTextView, systemTimeTextView;
    ImageView mWeatherIcon;

    LocationManager mLocationManager;
    LocationListener mLocationListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        Humidity = findViewById(R.id.humidityTextView);
        mWeatherIcon = findViewById(R.id.weatherIcon);
        latitudeLongitude = findViewById(R.id.latitudeLongitude);
        addressTextView = findViewById(R.id.addressTextView);
        systemTimeTextView = findViewById(R.id.systemTimeTextView);

        updateSystemTime();

    }

    private void startActivities(Intent intent) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
        updateSystemTime();
    }

    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                latitudeLongitude.setText("Latitude: "+ Latitude + "\nLongitude : " + Longitude);

                getAddressFromLocation(location);

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appId", API_ID);

                networkingFunc(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //LocationListener.super.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                //LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                //LocationListener.super.onProviderDisabled(provider);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this, "Location get Successfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied the permission
                Toast.makeText(MainActivity.this, "Location permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void networkingFunc(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();

                weatherData weatherD = weatherData.fromJson(response);

                assert weatherD != null;
                updateUI(weatherD);
                //super.onSuccess(statusCode, headers, response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

    private void updateUI(weatherData weather)
    {
        Temperature.setText(weather.getmTemperature());
        Humidity.setText(weather.getmHumidity());
        weatherState.setText(weather.getmWeatherDescription());
        int resourceID = getResources().getIdentifier(weather.getmIcon(), "drawable", getPackageName());
        mWeatherIcon.setImageResource(resourceID);
    }

    @SuppressLint("SetTextI18n")
    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try
        {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addresses != null && !addresses.isEmpty())
            {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                addressTextView.setText("Address: " + addressText);
            }
            else
            {
                addressTextView.setText("Address: Unable to determin");
            }
        } catch (IOException e) {
            e.printStackTrace();
            addressTextView.setText("Address: unable to determin");
        }
    }

    private void updateSystemTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        systemTimeTextView.setText(currentTime);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if(mLocationManager != null)
        {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }
}