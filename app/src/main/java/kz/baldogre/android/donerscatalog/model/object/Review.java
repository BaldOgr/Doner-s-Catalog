package kz.baldogre.android.donerscatalog.model.object;

import com.google.firebase.firestore.DocumentSnapshot;

public class Review {
    public static final String RESTAURANT_ID = "restaurant_id";
    public static final String USER_ID = "user_id";
    public static final String REVIEW = "review";
    public static final String RATING = "rating";
    public static final String REVIEW_COLLECTION = "reviews";
    private String id;
    private String restaurantId;
    private String userId;
    private String review;
    private Double rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    private void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReview() {
        return review;
    }

    private void setReview(String review) {
        this.review = review;
    }

    public Double getRating() {
        return rating;
    }

    private void setRating(Double rating) {
        this.rating = rating;
    }

    public static Review parseReview(DocumentSnapshot snapshot) {
        Review review = new Review();
        review.setId(snapshot.getId());
        review.setRestaurantId(snapshot.getString(Review.RESTAURANT_ID));
        review.setUserId(snapshot.getString(Review.USER_ID));
        review.setReview(snapshot.getString(Review.REVIEW));
        review.setRating(snapshot.getDouble(Review.RATING));
        return review;
    }
}
