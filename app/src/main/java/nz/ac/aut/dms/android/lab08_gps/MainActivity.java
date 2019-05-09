package nz.ac.aut.dms.android.lab08_gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String locationProviders;
    private TextView informationTextView;
    private final int all_permission_code = 1;

    private String[] permissionList = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        informationTextView= (TextView) findViewById(R.id.informationTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationProviders = locationManager.GPS_PROVIDER;
        //make the location listener
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("GPSApp","Location: "+location.toString());
                informationTextView.setText(location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "GPS location providers has been enabled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(), "GPS location providers has been disabled", Toast.LENGTH_SHORT).show();

            }
        };
        //check for permission
        if (!hasPermission(this, permissionList)) {
            ActivityCompat.requestPermissions(this, permissionList, all_permission_code);
        }
    }

    private boolean hasPermission(MainActivity mainActivity, String[] permissionList) {
        for (String aPermission : permissionList) {
            if (ActivityCompat.checkSelfPermission(this, aPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                //if we need to explain to user that they need to allow use sms
                if (shouldShowRequestPermissionRationale(aPermission)) {
                    Toast.makeText(this, "Please allow send sms permission!", Toast.LENGTH_SHORT).show();
                }
                return false;
//                //ask user
//                ActivityCompat.requestPermissions(this, new String[]{aPermission}, SEND_SMS_PERMISSION_REQUEST);
            }

        }
        return true;
    }

    public void getLocationManager(View view) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Toast.makeText(this, "Location Manager has been created", Toast.LENGTH_SHORT).show();
    }

    public void getGPSProviderButtonOnclick(View view) {
        locationProviders = locationManager.GPS_PROVIDER;
        Toast.makeText(this, "GPS location providers has been get", Toast.LENGTH_SHORT).show();
    }

    public void isGPSEnabledButtonOnclick(View view) {
        if (locationManager != null) {
            if (locationProviders != null) {
                boolean isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
                if (isGPSEnabled) {
                    Toast.makeText(this, "GPS location service is available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please get Location Provider", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please get Location Manager", Toast.LENGTH_SHORT).show();
        }

    }

    public void getGPSButtonOnclick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Toast.makeText(this, "Please allow Access fine location!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, all_permission_code);
            return;
        }

        //update the location
        locationManager.requestLocationUpdates(locationProviders, 0, 0, locationListener);
    }

    //if the app stop then remove the locationListener to free resource
    @Override
    protected void onStop() {
        super.onStop();

        if(locationManager!=null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }

}
