package kz.baldogre.android.donerscatalog.model;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import kz.baldogre.android.donerscatalog.model.object.Restaurant;
import kz.baldogre.android.donerscatalog.model.object.Review;
import kz.baldogre.android.donerscatalog.presentation.presenter.MainActivityPresenter;

import static kz.baldogre.android.donerscatalog.model.object.Restaurant.RESTAURANT_COLLECTION;
import static kz.baldogre.android.donerscatalog.model.object.Restaurant.RESTAURANT_DESCRIPTION;
import static kz.baldogre.android.donerscatalog.model.object.Restaurant.RESTAURANT_LATITUDE;
import static kz.baldogre.android.donerscatalog.model.object.Restaurant.RESTAURANT_LONGITUDE;
import static kz.baldogre.android.donerscatalog.model.object.Restaurant.RESTAURANT_NAME;
import static kz.baldogre.android.donerscatalog.model.object.Restaurant.parseRestaurant;

/**
 * Created by lol on 07.03.2018.
 */

public class MainActivityModel {
    private static final String LOG_TAG = "MainActivityModel";
    private MainActivityPresenter presenter;
    private FirebaseFirestore mDataBase;
    private ArrayList<Restaurant> restaurants;


    public MainActivityModel(MainActivityPresenter presenter) {
        this.presenter = presenter;
        mDataBase = FirebaseFirestore.getInstance();
        restaurants = new ArrayList<>();
        getNewRestaurants();

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

    public void getRestaurantInfo(Restaurant restaurant) {
        mDataBase.document(Restaurant.RESTAURANT_COLLECTION + "/" + restaurant.getId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        presenter.showRestaurant(parseRestaurant(snapshot));
                    }
                });
    }

    public void getRestaurantFullInfo(Restaurant restaurant) {
        mDataBase.document(RESTAURANT_COLLECTION + "/" + restaurant.getId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshots) {
                        if (!documentSnapshots.exists()) {
                            presenter.showMessage("FAIL!");
                            return;
                        }
                        final Restaurant restaurant = parseRestaurant(documentSnapshots);
                        mDataBase.collection(Review.REVIEW_COLLECTION).whereEqualTo(Review.RESTAURANT_ID, restaurant.getId()).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot documentSnapshot :
                                                queryDocumentSnapshots.getDocuments()) {
                                            Review review = Review.parseReview(documentSnapshot);
                                            restaurant.addReview(review);
                                        }
                                        presenter.showRestaurantFullInfo(restaurant);
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                presenter.showMessage("FAIL!");
            }
        });
    }

    public void getRestaurants() {
        mDataBase.collection(Restaurant.RESTAURANT_COLLECTION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for (int i = 0; i < documentSnapshots.size(); i++) {
                    Restaurant restaurant = new Restaurant();
                    DocumentSnapshot documentSnapshot = documentSnapshots.getDocuments().get(i);
                    restaurant.setName(documentSnapshot.getString(RESTAURANT_NAME));
                    restaurant.setDescription(documentSnapshot.getString(RESTAURANT_DESCRIPTION));
                    Double lat = Double.valueOf(documentSnapshot.get(Restaurant.RESTAURANT_LATITUDE).toString());
                    Double lng = Double.valueOf(documentSnapshot.get(Restaurant.RESTAURANT_LONGITUDE).toString());
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
        mDataBase.collection(Restaurant.RESTAURANT_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(MainActivityModel.LOG_TAG, e.getMessage());
                    return;
                }
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
//                    TODO: Add restaurant listener
//                    if (queryDocumentSnapshots.size() > restaurants.size()) {
//                        for (DocumentChange snapshot : queryDocumentSnapshots.getDocumentChanges()) {
//                            Restaurant restaurant = snapshot.getDocument().toObject(Restaurant.class);
//
//                        }
//                    }
                }
            }
        });
    }
}
