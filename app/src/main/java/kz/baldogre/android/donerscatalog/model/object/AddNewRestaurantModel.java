package kz.baldogre.android.donerscatalog.model.object;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import kz.baldogre.android.donerscatalog.presentation.presenter.AddNewRestaurantPresenter;

/**
 * Created by lol on 09.03.2018.
 */

public class AddNewRestaurantModel {
    private FirebaseFirestore mDataBase;
    private AddNewRestaurantPresenter presenter;

    public AddNewRestaurantModel(AddNewRestaurantPresenter addNewRestaurantPresenter) {
        this.mDataBase = FirebaseFirestore.getInstance();
        presenter = addNewRestaurantPresenter;
    }

    public void addRestaurant(final Restaurant restaurant) {
        Map<String, String> restaurantMap = new HashMap<>();
        restaurantMap.put("name", restaurant.getName());
        restaurantMap.put("description", restaurant.getDescription());
        restaurantMap.put("lat", String.valueOf(restaurant.getLatLng().latitude));
        restaurantMap.put("lng", String.valueOf(restaurant.getLatLng().longitude));
        mDataBase.collection("restaurants").add(restaurantMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                restaurant.setId(documentReference.getId());
                presenter.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.onFailure(e.getMessage());
            }
        });
    }
}
