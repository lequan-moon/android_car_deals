package com.example.quanlm.cardeal;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.quanlm.cardeal.configure.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import static com.example.quanlm.cardeal.configure.Constants.IMAGE_PACKS;

public class FetchImageService extends Service {
    FetchImageServiceBinder mBinder = new FetchImageServiceBinder();
    FirebaseStorage mStorage = FirebaseStorage.getInstance();
    public static final String UPDATE_IMAGE_ACTION = "update_image_action";

    public FetchImageService() {
    }

    public void fetchImageForAdapter(final int position, String imagePath) {
        mStorage.getReference().child(mStorage.getReferenceFromUrl(imagePath).getPath()).getDownloadUrl();
        Task<Uri> task = mStorage.getReferenceFromUrl(imagePath).getDownloadUrl();
        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("fetchImage", "onSuccess: " + uri);
                Intent it = new Intent(UPDATE_IMAGE_ACTION);
                Bundle args = new Bundle();
                args.putInt(Constants.POSITION, position);
                args.putString(Constants.IMAGE_URI, uri.toString());
                it.putExtra(Constants.IMAGE_PACKS, args);
                sendBroadcast(it);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class FetchImageServiceBinder extends Binder{
        public FetchImageService getService() {
            return FetchImageService.this;
        }
    }
}
