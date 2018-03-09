package kz.baldogre.android.donerscatalog.presentation.presenter;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import kz.baldogre.android.donerscatalog.model.MainActivityModel;
import kz.baldogre.android.donerscatalog.R;
import kz.baldogre.android.donerscatalog.model.object.Restaurant;
import kz.baldogre.android.donerscatalog.presentation.view.ViewInterface;

/**
 * Created by lol on 07.03.2018.
 */

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<ViewInterface> {
    private MainActivityModel model;

    public MainActivityPresenter() {
        getViewState().showMessage(R.string.app_name);
        model = new MainActivityModel(this);
    }

    public void showMessage(String message) {
        getViewState().hideProgress();
        getViewState().showMessage(message);
    }

    public void onButtonPressed() {
        getViewState().showProgress();
        model.getNames();
    }

    public void onMarkerPressed(String title) {
        model.getRestaurantInfo(title);
    }

    public void showRestaurant(DocumentSnapshot documentSnapshots) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(documentSnapshots.getString("name"));
        restaurant.setName(documentSnapshots.getString("info"));
        getViewState().showRestaurant(restaurant);
    }

    public void showRestaurants(List<Restaurant> restaurants){
        getViewState().showRestaurants(restaurants);
    }



    public void mapReady(GoogleMap mGoogleMap) {
        getViewState().setGoogleMap(mGoogleMap);
    }

    public void getNewRestaurants() {
        model.getNewRestaurants();
    }

    public void getRestaurants() {
        model.getRestaurants();
    }
}
