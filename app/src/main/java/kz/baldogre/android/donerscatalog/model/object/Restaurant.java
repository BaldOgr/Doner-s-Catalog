package kz.baldogre.android.donerscatalog.model.object;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

/**
 * Created by lol on 08.03.2018.
 */

public class Restaurant {
    public static final String RESTAURANT_COLLECTION = "restaurants";
    public static final String RESTAURANT_LATITUDE = "lat";
    public static final String RESTAURANT_LONGITUDE = "lng";
    private String name;
    private String description;
    private LatLng latLng;
    private String id;

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        if (reviews == null){
            reviews = new ArrayList<>();
        }
        reviews.add(review);
    }

    private ArrayList<Review> reviews;
    public static final String RESTAURANT_NAME = "name";
    public static final String RESTAURANT_DESCRIPTION = "description";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String info) {
        this.description = info;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public static Restaurant parseRestaurant(DocumentSnapshot documentSnapshot) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName((String) documentSnapshot.get(RESTAURANT_NAME));
        restaurant.setDescription((String) documentSnapshot.get(RESTAURANT_DESCRIPTION));
        restaurant.setId(documentSnapshot.getId());
        LatLng latLng = new LatLng(Double.parseDouble(documentSnapshot.getString(RESTAURANT_LATITUDE)),
                Double.parseDouble(documentSnapshot.getString(RESTAURANT_LONGITUDE)));
        restaurant.setLatLng(latLng);
        return restaurant;
    }
}
