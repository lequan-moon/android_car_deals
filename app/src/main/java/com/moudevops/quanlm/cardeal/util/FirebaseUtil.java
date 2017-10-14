package com.moudevops.quanlm.cardeal.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moudevops.quanlm.cardeal.configure.Constants;

/**
 * Created by MyPC on 15/10/2017.
 */

public class FirebaseUtil {
    private FirebaseDatabase mDatabase;
    private static FirebaseUtil instance;

    public FirebaseUtil() {
        this.mDatabase = FirebaseDatabase.getInstance();
    }

    public static FirebaseUtil getInstance() {
        if (instance == null) {
            instance = new FirebaseUtil();
        }
        return instance;
    }

    public void fetchFirebaseDeals(ValueEventListener listener) {
        DatabaseReference dealTable = mDatabase.getReference(Constants.DEAL_TABLE);
        dealTable.orderByKey()
                // Get Constants.ITEM_PER_PAGE + 1 item
                // to keep the last record as key to load more later
                .limitToFirst(Constants.ITEM_PER_PAGE + 1)
                .addListenerForSingleValueEvent(listener);
    }

    public void fetchFirebaseDeals(ValueEventListener listener, String startAt) {
        DatabaseReference dealTable = mDatabase.getReference(Constants.DEAL_TABLE);
        dealTable.orderByKey()
                // Get Constants.ITEM_PER_PAGE + 1 item
                // to keep the last record as key to load more later
                .limitToFirst(Constants.ITEM_PER_PAGE + 1)
                .startAt(startAt)
                .addListenerForSingleValueEvent(listener);
    }
}
