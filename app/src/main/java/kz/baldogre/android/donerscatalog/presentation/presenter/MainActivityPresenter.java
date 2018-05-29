package kz.baldogre.android.donerscatalog.presentation.presenter;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
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
    private ArrayList<Restaurant> restaurants;

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

    public void onMarkerPressed(String restaurantId) {
        for (Restaurant re :
                restaurants) {
            if (re.getId().equals(restaurantId)) {
                model.getRestaurantInfo(re);
                return;
            }
        }
    }

    public void showRestaurant(Restaurant restaurant) {
        getViewState().showRestaurant(restaurant);
    }

    public void showRestaurants(List<Restaurant> restaurants) {
        this.restaurants = (ArrayList<Restaurant>) restaurants;
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

    public void getRestaurantInfo(Restaurant restaurant) {
        model.getRestaurantInfo(restaurant);
    }

    public void showRestaurantFullInfo(Restaurant restaurant) {
        getViewState().showRestaurantFullInfo(restaurant);
    }

    public void getRestaurantFullInfo(Restaurant mCurrentRestaurant) {
        model.getRestaurantFullInfo(mCurrentRestaurant);
    }
}
