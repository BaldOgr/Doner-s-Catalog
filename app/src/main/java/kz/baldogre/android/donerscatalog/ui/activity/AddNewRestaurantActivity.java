package kz.baldogre.android.donerscatalog.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import kz.baldogre.android.donerscatalog.R;
import kz.baldogre.android.donerscatalog.model.object.Restaurant;
import kz.baldogre.android.donerscatalog.presentation.presenter.AddNewRestaurantPresenter;
import kz.baldogre.android.donerscatalog.presentation.view.AddNewRestaurantViewInterface;

public class AddNewRestaurantActivity extends MvpAppCompatActivity implements AddNewRestaurantViewInterface {

    @InjectPresenter
    AddNewRestaurantPresenter presenter;
    int PLACE_PICKER_REQUEST = 1;
    TextView mLocation;
    EditText mTitle;
    EditText mDescription;
    private Place place;

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
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AddNewRestaurantActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                Toast.makeText(this, place.toString(), Toast.LENGTH_LONG).show();
                mLocation.setText(place.getLatLng().toString());
            }
        }
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
                    restaurant.setDescription(mDescription.getText().toString());
                    restaurant.setLatLng(place.getLatLng());
                    presenter.addRestaurant(restaurant);
                    Toast.makeText(getApplicationContext(), "THANKS!", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isCorrect() {   // Check restaurant name and description
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
