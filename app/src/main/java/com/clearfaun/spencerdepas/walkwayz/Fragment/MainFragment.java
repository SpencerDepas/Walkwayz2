package com.clearfaun.spencerdepas.walkwayz.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.clearfaun.spencerdepas.walkwayz.Activity.WalkWayzApplication;
import com.clearfaun.spencerdepas.walkwayz.Manager.BackendlessCallback;
import com.clearfaun.spencerdepas.walkwayz.Manager.BackendlessManager;
import com.clearfaun.spencerdepas.walkwayz.Model.User;
import com.clearfaun.spencerdepas.walkwayz.Model.UserLocation;
import com.clearfaun.spencerdepas.walkwayz.R;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.Place;
import com.hypertrack.lib.models.SuccessResponse;

import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragmentListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EMERGENCY_TYPE = "ARG_EMERGENCY_TYPE";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.main_progress) ProgressBar progressBar;
    @BindView(R.id.help_type) TextView helpTypeTextView;
    @BindView(R.id.background) RelativeLayout background;
    @BindView(R.id.icon_subtext) TextView iconSubText;


    @BindArray(R.array.emergency_type)
    protected String [] spinnerEmergencyTypes;

    private int currentIndexOfEmergency = 0;
    private final static String ALERT_MODE = "alert_mode";
    private final static String INACTIVE_MODE = "inactive_mode";


    // TODO: Rename and change types of parameters
    private String emergencyType;
    private String mParam2;

    private MainFragmentListener mListener;



    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMERGENCY_TYPE, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();

    }

    private void maybeTriggerEmergencyMode(){
        if(!emergencyType.isEmpty()){
            helpTypeTextView.setText(emergencyType);
            progressBar.setVisibility(View.VISIBLE);
            startTrackingLocationHyperLoop();
        }
    }

    private void switchVisualState(String mode){
        switch (mode) {
            case INACTIVE_MODE:
                inactiveMode();
                break;
            case ALERT_MODE:
                alertMode();
                break;
            default: inactiveMode();
                break;
        }
    }

    private void alertMode(){
        background.setBackgroundColor(
                ContextCompat.getColor(WalkWayzApplication.getAppContext(), R.color.amber));
        iconSubText.setText("We are aware of your issue," +
                " help has been notified");
    }
    private void inactiveMode(){
        background.setBackgroundColor(
                ContextCompat.getColor(WalkWayzApplication.getAppContext(), R.color.colorPrimary));
        iconSubText.setText("We have your back");
    }

    private void getExtras(){
        if (getArguments() != null) {
            emergencyType = getArguments().getString(ARG_EMERGENCY_TYPE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.emergency_fab)
    public void emergencyFab(View view) {

         mListener.OnEmergencyFabPressed();
        //showLocationDialog();

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

        maybeTriggerEmergencyMode();
    }


    @Override
    public void onPause() {
        super.onPause();

        // Stop HyperTrack SDK
        HyperTrack.stopTracking();
        switchVisualState(INACTIVE_MODE);

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


    /**
     * Call this method when user has successfully logged in
     */
    private void startTrackingLocationHyperLoop() {
        HyperTrack.startTracking(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                // Hide Login Button loader
                switchVisualState(ALERT_MODE);
                //com.hypertrack.lib.models.User user = (com.hypertrack.lib.models.User) successResponse.getResponseObject();
                getCurrentUserLocation();


            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                // Hide Login Button loader
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(WalkWayzApplication.getAppContext(),
                        R.string.hyper_track_location_failure,
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateUserLocalUserLocation(Location location){
        UserLocation userLocation = new UserLocation(location.getLatitude()
                ,location.getLongitude());
        User.getInstance().setLocation(userLocation);
    }

    private void getCurrentUserLocation(){
        HyperTrack.getCurrentLocation(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                Location location = (Location) successResponse.getResponseObject();
                updateUserLocalUserLocation(location);
                Place expectedPlace = new Place().setLocation(location.getLatitude(), location.getLongitude())
                        .setName("Emergency button pressed");
                updateBackendless();
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                // Hide Login Button loader
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(WalkWayzApplication.getAppContext(),
                        R.string.hyper_track_location_failure,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBackendless(){
        BackendlessManager.getInstance().emergencyCall(emergencyType,
                new BackendlessManager.BackendlessEmergencyCallback(){
                       @Override
                       public void callbackSuccess(Map response) {
                           progressBar.setVisibility(View.INVISIBLE);
                           Snackbar.make(progressBar,
                                   R.string.main_fragment_emergency_success,
                                   Snackbar.LENGTH_LONG).show();
                       }

                       @Override
                       public void callbackFailure(BackendlessFault fault) {
                           progressBar.setVisibility(View.INVISIBLE);

                       }
                   }
        );
    }

    private void showLocationDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.view_fragment_main_emergency_dialog);
        dialog.setTitle("Title...");
        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.dialog_text_body);
        text.setText("After this countdown has been reached the emergency services will be notified");
        final TextView mTextField = (TextView) dialog.findViewById(R.id.dialog_countdown);
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText("" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                if(dialog.isShowing()){
                    progressBar.setVisibility(View.VISIBLE);
                    startTrackingLocationHyperLoop();
                }
                dialog.dismiss();
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
        if (context instanceof MainFragmentListener) {
            mListener = (MainFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface MainFragmentListener {
        void OnEmergencyFabPressed();
    }
}
