package com.moudevops.quanlm.cardeal.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.moudevops.quanlm.cardeal.ActLogin;
import com.moudevops.quanlm.cardeal.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment {

    private static final int REQUEST_CODE_LOGIN = 9001;
    View layoutUserInfo;
    ImageView imgProfile;
    TextView txtUserName;
    TextView txtUserEmail;
    TextView txtUserPhone;
    Button btnLogout;

    FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private OnFragmentInteractionListener mListener;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutUserInfo = view.findViewById(R.id.layoutUserInfo);
        imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
        txtUserEmail = (TextView) view.findViewById(R.id.txtUserEmail);
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtUserPhone = (TextView) view.findViewById(R.id.txtUserPhone);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        handleLogin(currentUser);
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        Toast.makeText(getContext(), "Logged out!", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        handleLogin(currentUser);
    }

    private void handleLogin(FirebaseUser currentUser) {
        if (currentUser == null) {
            layoutUserInfo.setVisibility(View.GONE);
            Intent itLogin = new Intent(getContext(), ActLogin.class);
            startActivityForResult(itLogin, REQUEST_CODE_LOGIN);
        } else {
            layoutUserInfo.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(currentUser.getPhotoUrl())
                    .asBitmap()
                    .into(new BitmapImageViewTarget(imgProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imgProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            txtUserName.setText(currentUser.getDisplayName());
            txtUserEmail.setText(currentUser.getEmail());
            txtUserPhone.setText(currentUser.getPhoneNumber());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            handleLogin(currentUser);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
