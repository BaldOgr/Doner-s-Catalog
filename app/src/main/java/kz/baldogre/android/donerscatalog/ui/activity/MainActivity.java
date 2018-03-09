package kz.baldogre.android.donerscatalog.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import kz.baldogre.android.donerscatalog.R;
import kz.baldogre.android.donerscatalog.model.object.MapReadyCallback;
import kz.baldogre.android.donerscatalog.model.object.Restaurant;
import kz.baldogre.android.donerscatalog.presentation.presenter.MainActivityPresenter;
import kz.baldogre.android.donerscatalog.presentation.view.ViewInterface;

public class MainActivity extends MvpAppCompatActivity implements ViewInterface {

    @InjectPresenter
    MainActivityPresenter presenter;

    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private MarkerOptions mMarkerOptions;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, AddNewRestaurantActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMarkerOptions = new MarkerOptions();
        MapReadyCallback mapReadyCallback = new MapReadyCallback(getApplicationContext(), presenter);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        fragment.getMapAsync(mapReadyCallback);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
        Log.d("MainActivity", "Looking for location...");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("MainActivity", "Adding marker....");
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (mGoogleMap != null) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                mGoogleMap.addMarker(mMarkerOptions.position(latLng));
                    Log.d("MainActivity", "Marker Added!");
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }

    @Override
    public void showMessage(int message) {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showRestaurant(Restaurant restaurant) {
        TextView textView = findViewById(R.id.bottom_sheet_peek_text);
        textView.setText(restaurant.getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getRestaurants();
    }

    @Override
    public void showRestaurants(List<Restaurant> restaurants) {
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            mMarkerOptions.title(restaurant.getName());
            mMarkerOptions.snippet(restaurant.getId());
            mMarkerOptions.position(restaurant.getLatLng());
            mGoogleMap.addMarker(mMarkerOptions);
        }
    }

    @Override
    public void setGoogleMap(GoogleMap mGoogleMap) {
        this.mGoogleMap = mGoogleMap;
    }

}
