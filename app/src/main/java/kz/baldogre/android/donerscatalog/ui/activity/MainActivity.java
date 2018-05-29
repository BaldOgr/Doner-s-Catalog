package kz.baldogre.android.donerscatalog.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
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
    private boolean mLocationChanged = false;
    private LatLng mUserPosition;
    private SharedPreferences mSharedPreferences;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Restaurant mCurrentRestaurant;

    private static final int REQUEST_PERMISSION_LOCATION = 2;
    public static final String USER_LATITUDE = "user_lat";
    public static final String USER_LONGITUDE = "user_long";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSheetBehavior();
        initLocationManager();

        mSharedPreferences = getPreferences(MODE_PRIVATE);
        mUserPosition = new LatLng(mSharedPreferences.getFloat(USER_LATITUDE, 0), mSharedPreferences.getFloat(USER_LONGITUDE, 0));

        MapReadyCallback mapReadyCallback = new MapReadyCallback(getApplicationContext(), presenter);

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        fragment.getMapAsync(mapReadyCallback);
    }

    private void initLocationManager() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.d("MainActivity", "Looking for location...");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mUserPosition = new LatLng(location.getLatitude(), location.getLongitude());
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putFloat(USER_LATITUDE, (float) mUserPosition.latitude);
                editor.putFloat(USER_LONGITUDE, (float) mUserPosition.longitude);
                if (mGoogleMap != null && !mLocationChanged) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mUserPosition, 11));
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                mGoogleMap.addMarker(mMarkerOptions.position(latLng));
                    mLocationChanged = true;
                }
                editor.apply();
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

    private void initSheetBehavior() {
        mBottomSheetBehavior = BottomSheetBehavior.from(
                findViewById(R.id.bottom_sheet)
        );
        // настройка состояний нижнего экрана
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // настройка максимальной высоты
//        mBottomSheetBehavior.setPeekHeight(340);

        // настройка возможности скрыть элемент при свайпе вниз
        mBottomSheetBehavior.setHideable(true);

        // настройка колбэков при изменениях
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    presenter.getRestaurantFullInfo(mCurrentRestaurant);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new_restaurant:
                startActivity(new Intent(this, AddNewRestaurantActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Thanks!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "We need this permission to find your location..", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(getApplicationContext(), getString(message), Toast.LENGTH_LONG).show();
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
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        TextView textView = findViewById(R.id.bottom_sheet_peek_text);
        textView.setText(restaurant.getName());
        textView = findViewById(R.id.bottom_sheet_content_description);
        textView.setText("");
        this.mCurrentRestaurant = restaurant;
    }

    @Override
    public void showRestaurantFullInfo(Restaurant restaurant) {
        TextView textView = findViewById(R.id.bottom_sheet_content_description);
        textView.setText(restaurant.getDescription());
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.getRestaurants();
    }

    @Override
    public void showRestaurants(List<Restaurant> restaurants) {
        for (int i = 0; i < restaurants.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            Restaurant restaurant = restaurants.get(i);
            markerOptions.snippet(restaurant.getId());
            markerOptions.position(restaurant.getLatLng());
            mGoogleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void setGoogleMap(GoogleMap mGoogleMap) {
        this.mGoogleMap = mGoogleMap;
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mUserPosition));
    }

}
