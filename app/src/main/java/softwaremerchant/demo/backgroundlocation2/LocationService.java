package softwaremerchant.demo.backgroundlocation2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * Created by Shilpan Patel on 3/9/16.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    protected Location mLocation;
    protected IBinder iBinder = new BinderService();
    protected GoogleApiClient mGoogleApiClient;
    protected double latitude, longitude;
    protected boolean mbound = false;

    protected synchronized void buildGoogleApiClient() {


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    public void onCreate() {
        Log.i("create", "Service_created");
        super.onCreate();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mbound = true;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.i("bind", "activityBind");
        return iBinder;
    }

    @Override
    public void onConnected(Bundle bundle) {



        Log.i("onconnect", "connect_api");

        if (mbound) {
                mLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(mLocation == null) {

                    Log.i("null","location_null");
                    return;
                }
                else {
                    Log.i("got", "Got_location");
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();

                }

            }

        else {

            mbound = false;
        }


    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mGoogleApiClient.disconnect();
        //nothing to do here
    }

    public class BinderService extends Binder {

        LocationService locationService() {

            return LocationService.this;
        }
    }
}

