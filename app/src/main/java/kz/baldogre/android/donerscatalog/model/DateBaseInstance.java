package kz.baldogre.android.donerscatalog.model;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by lol on 07.03.2018.
 */

public class DateBaseInstance {
    private static FirebaseFirestore mDataBase;

    public static FirebaseFirestore getDatabaseInstance(){
        if (mDataBase == null){
            mDataBase = FirebaseFirestore.getInstance();
        }
        return mDataBase;
    }
}
