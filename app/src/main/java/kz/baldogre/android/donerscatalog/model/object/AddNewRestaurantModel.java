package kz.baldogre.android.donerscatalog.model.object;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kz.baldogre.android.donerscatalog.presentation.presenter.AddNewRestaurantPresenter;

/**
 * Created by lol on 09.03.2018.
 */

public class AddNewRestaurantModel {
    private static final String LOG_TAG = "AddNewRestaurantModel";
    private FirebaseFirestore mDataBase;
    private StorageReference mStorageRef;
    private AddNewRestaurantPresenter presenter;

    public AddNewRestaurantModel(AddNewRestaurantPresenter addNewRestaurantPresenter) {
        this.mDataBase = FirebaseFirestore.getInstance();
        presenter = addNewRestaurantPresenter;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void addRestaurant(final Restaurant restaurant) {
        final Map<String, Object> restaurantMap = new HashMap<>();
        restaurantMap.put("name", restaurant.getName());
        restaurantMap.put("description", restaurant.getDescription());
        restaurantMap.put("lat", String.valueOf(restaurant.getLatLng().latitude));
        restaurantMap.put("lng", String.valueOf(restaurant.getLatLng().longitude));
        final List<Map<String, String>> images = new ArrayList<>(restaurant.getImages().size());
        for (int i = 0; i < restaurant.getImages().size(); i++) {
            final int finalI = i;
            Uri file = restaurant.getImages().get(i);
            StorageReference riversRef = mStorageRef.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Map<String, String> map = new HashMap<>(1);
                    map.put("URI", taskSnapshot.getUploadSessionUri().toString());
                    images.add(map);
                    if (finalI == restaurant.getImages().size() - 1) {
                        mDataBase.collection(Restaurant.RESTAURANT_COLLECTION).add(restaurantMap)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                restaurant.setId(documentReference.getId());
                                for (int j = 0; j < images.size(); j++) {
                                    final int finalJ = j;
                                    mDataBase.document(Restaurant.RESTAURANT_COLLECTION + "/" + restaurant.getId())
                                            .collection("images").add(images.get(j))
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    if (finalJ == images.size() - 1)
                                                        presenter.onSuccess();
                                                }
                                            });
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                presenter.onFailure(e.getMessage());
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.getMessage());
                }
            });
        }

    }
}
