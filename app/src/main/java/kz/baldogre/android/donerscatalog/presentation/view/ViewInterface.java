package kz.baldogre.android.donerscatalog.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import kz.baldogre.android.donerscatalog.model.object.Restaurant;

/**
 * Created by lol on 07.03.2018.
 */

public interface ViewInterface extends MvpView {
    void showMessage(int message);

    void showMessage(String message);

    void showProgress();

    void hideProgress();

    void showRestaurant(Restaurant restaurant);

    void showRestaurants(List<Restaurant> restaurants);

    void setGoogleMap(GoogleMap mGoogleMap);

    void showRestaurantFullInfo(Restaurant restaurant);

}