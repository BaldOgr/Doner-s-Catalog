package kz.baldogre.android.donerscatalog.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import kz.baldogre.android.donerscatalog.model.object.AddNewRestaurantModel;
import kz.baldogre.android.donerscatalog.model.object.Restaurant;
import kz.baldogre.android.donerscatalog.presentation.view.AddNewRestaurantViewInterface;

/**
 * Created by lol on 09.03.2018.
 */
@InjectViewState
public class AddNewRestaurantPresenter extends MvpPresenter<AddNewRestaurantViewInterface> {
    private AddNewRestaurantModel model;

    public AddNewRestaurantPresenter() {
        model = new AddNewRestaurantModel(this);
    }

    public void addRestaurant(Restaurant restaurant) {
        getViewState().showProgress();
        model.addRestaurant(restaurant);
    }

    public void onSuccess() {
        getViewState().done();
    }

    public void onFailure(String message) {
        getViewState().showError(message);
        getViewState().hideProgress();
    }
}
