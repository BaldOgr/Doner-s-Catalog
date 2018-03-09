package kz.baldogre.android.donerscatalog.presentation.view;

import com.arellomobile.mvp.MvpView;

/**
 * Created by lol on 09.03.2018.
 */

public interface AddNewRestaurantViewInterface extends MvpView {
    void showMessage(int message);

    void showMessage(String message);

    void showError(String error);

    void showProgress();

    void hideProgress();

    void done();
}
