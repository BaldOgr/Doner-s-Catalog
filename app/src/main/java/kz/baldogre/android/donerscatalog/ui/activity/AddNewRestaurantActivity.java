package kz.baldogre.android.donerscatalog.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.model.LatLng;

import kz.baldogre.android.donerscatalog.R;
import kz.baldogre.android.donerscatalog.model.object.Restaurant;
import kz.baldogre.android.donerscatalog.presentation.presenter.AddNewRestaurantPresenter;
import kz.baldogre.android.donerscatalog.presentation.view.AddNewRestaurantViewInterface;

public class AddNewRestaurantActivity extends MvpAppCompatActivity implements AddNewRestaurantViewInterface {

    @InjectPresenter
    AddNewRestaurantPresenter presenter;

    TextView mLocation;
    EditText mTitle;
    EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = findViewById(R.id.add_restaurant_title);
        mDescription = findViewById(R.id.add_restaurant_description);
        mLocation = findViewById(R.id.add_restaurant_location_text);

        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int PLACE_PICKER_REQUEST = 1;
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, new LocationListener() {

                        @Override
                        public void onLocationChanged(Location location) {
                            mLocation.setText(location.getLatitude() + " " + location.getLongitude());
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
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_restaurant, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_restaurant_done:
                if (isCorrect()) {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setName(mTitle.getText().toString());
                    restaurant.setInfo(mDescription.getText().toString());
                    String[] latLngStr = mLocation.getText().toString().split(" ");
                    double lat = Double.parseDouble(latLngStr[0]);
                    double lng = Double.parseDouble(latLngStr[1]);
                    LatLng latLng = new LatLng(lat, lng);
                    restaurant.setLatLng(latLng);
                    presenter.addRestaurant(restaurant);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isCorrect() {
        return !(mTitle.getText().toString().equals(getString(R.string.title)) ||
                mDescription.getText().toString().equals(getString(R.string.description)) ||
                mLocation.getText().equals(getString(R.string.click_to_add_location)));
    }

    @Override
    public void showMessage(int message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String error) {
        showMessage(error);
    }

    @Override
    public void showProgress() {
        mLocation.setClickable(false);
        mTitle.setClickable(false);
        mDescription.setClickable(false);
    }

    @Override
    public void hideProgress() {
        mLocation.setClickable(true);
        mTitle.setClickable(true);
        mDescription.setClickable(true);
    }

    @Override
    public void done() {
        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
        finish();
    }
}
