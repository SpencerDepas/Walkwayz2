package com.clearfaun.spencerdepas.walkwayz.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.clearfaun.spencerdepas.walkwayz.Activity.WalkWayzApplication;
import com.clearfaun.spencerdepas.walkwayz.Manager.BackendlessCallback;
import com.clearfaun.spencerdepas.walkwayz.Manager.BackendlessManager;
import com.clearfaun.spencerdepas.walkwayz.Model.User;
import com.clearfaun.spencerdepas.walkwayz.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.main_progress) ProgressBar progressBar;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;



    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.emergency_fab)
    public void emergencyFab(View view) {
        showLocationDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        

    }

    private void updateUser(){
        progressBar.setVisibility(View.VISIBLE);
        BackendlessManager.getInstance().updateUser(Backendless.UserService.CurrentUser(),
                new BackendlessCallback(){
                    @Override
                    public void callbackSuccess(BackendlessUser user) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void callbackFailure(BackendlessFault fault) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
        );
    }

    private HashMap setUserData(){
        HashMap userInfo = new HashMap<>();
        userInfo.put( "age",  User.getInstance().getAge() );
        userInfo.put( "email",  User.getInstance().getEmail() );
        userInfo.put( "password",   User.getInstance().getPassword() );
        userInfo.put( "height", User.getInstance().getHeight());
        userInfo.put( "location",User.getInstance().getLocation());
        return userInfo;
    }

    private void timerComplete(){
        //callCenterDataAccess.setUserData(setUserData());
    }

    private void showLocationDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.view_fragment_main_emergency_dialog);
        dialog.setTitle("Title...");
        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.dialog_text_body);
        text.setText("Android custom dialog example!");
        final TextView mTextField = (TextView) dialog.findViewById(R.id.dialog_countdown);
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText("" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                dialog.dismiss();
                timerComplete();
            }
        }.start();
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
