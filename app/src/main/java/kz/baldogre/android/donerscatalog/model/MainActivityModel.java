package kz.baldogre.android.donerscatalog.model;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kz.baldogre.android.donerscatalog.model.object.Restaurant;
import kz.baldogre.android.donerscatalog.presentation.presenter.MainActivityPresenter;

/**
 * Created by lol on 07.03.2018.
 */

public class MainActivityModel {
    private MainActivityPresenter presenter;
    private FirebaseFirestore mDataBase;

    public MainActivityModel(MainActivityPresenter presenter) {
        this.presenter = presenter;
        mDataBase = DateBaseInstance.getDatabaseInstance();
    }


    public void getNames() {
       mDataBase.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot documentSnapshots) {
               StringBuilder sb = new StringBuilder();
               for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                   sb.append(documentSnapshot.get("name")).append("\n")
                           .append(documentSnapshot.get("Second name")).append("\n")
                           .append(documentSnapshot.get("Phone number")).append("\n\n");
               }
               presenter.showMessage(sb.toString());
           }
       })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.showMessage("Something went wrong...");
            }
        });
    }

    public void getRestaurantInfo(String title) {
        mDataBase.document("restaurants/"+title).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshots) {
                if (!documentSnapshots.exists()){
                    presenter.showMessage("FAIL!");
                    return;
                }
                presenter.showRestaurant(documentSnapshots);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.showMessage("FAIL!");
            }
        });
    }

    public void getRestaurants() {
        mDataBase.collection("restaurants").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                ArrayList<Restaurant> restaurants = new ArrayList<>(documentSnapshots.size());
                for (int i = 0; i < documentSnapshots.size(); i++) {
                    Restaurant restaurant = new Restaurant();
                    DocumentSnapshot documentSnapshot = documentSnapshots.getDocuments().get(i);
                    restaurant.setName(documentSnapshot.getString("name"));
                    restaurant.setInfo(documentSnapshot.getString("info"));
                    Double lat = Double.valueOf(documentSnapshot.get("lat").toString());
                    Double lng = Double.valueOf(documentSnapshot.get("lng").toString());
                    restaurant.setLatLng(new LatLng(lat, lng));
                    restaurant.setId(documentSnapshot.getId());
                    restaurants.add(restaurant);
                }
                presenter.showRestaurants(restaurants);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.showMessage("Something get wrong...");
            }
        });
    }

    public void getNewRestaurants() {

    }
}
