package com.clearfaun.spencerdepas.walkwayz.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clearfaun.spencerdepas.walkwayz.R;
import com.clearfaun.spencerdepas.walkwayz.Util.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmergencyTypeSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmergencyTypeSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmergencyTypeSelectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.timer_fab)
    FloatingActionButton fab;

    @BindView(R.id.police_main_button)
    Button policeMainButton;

    @BindView(R.id.fire_selection_button)
    Button fireMainButton;

    @BindView(R.id.health_selection_button)
    Button healthMainButton;

    @BindView(R.id.fire_fab_button_holder)
    LinearLayout fireFabHolder;

    @BindView(R.id.health_container)
    LinearLayout healthContainer;

    @BindView(R.id.fire_container)
    LinearLayout fireContainer;

    @BindView(R.id.police_container)
    LinearLayout policeContainer;

    @BindView(R.id.line)
    LinearLayout lineSeparator;

    @BindView(R.id.detail_fab_three)
    FloatingActionButton detailFabThree;

    @BindView(R.id.detail_fab_two)
    FloatingActionButton detailFabTwo;

    @BindView(R.id.detail_fab_one)
    FloatingActionButton detailFabOne;

    @BindView(R.id.detail_emergency_textview_one)
    TextView emergencyTextViewOne;

    @BindView(R.id.detail_emergency_textview_two)
    TextView emergencyTextViewTwo;

    @BindView(R.id.detail_emergency_textview_three)
    TextView emergencyTextViewThree;

    private OnFragmentInteractionListener mListener;

    public EmergencyTypeSelectionFragment() {
        // Required empty public constructor
    }


    public static EmergencyTypeSelectionFragment newInstance(String param1, String param2) {
        EmergencyTypeSelectionFragment fragment = new EmergencyTypeSelectionFragment();
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
    @OnClick(R.id.police_main_button)
    public void policeButtonClick(View view) {

        setDetailEmergency("police");
        setTextViews("I am being followed", "I am being raped", "Someone is attacking me");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.health_selection_button)
    public void healthButtonClick(View view) {

        setDetailEmergency("Health");
        setTextViews("I am dying", "An old lady fell over", "I can't breath");
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.fire_selection_button)
    public void fireButtonClick(View view) {

        setDetailEmergency("Fire");
        setTextViews("There is a cat in a tree", "There is a fire", "There is a rabid dog");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.detail_fab_one)
    public void detailFabOne(View view) {

        emrgencySelected(emergencyTextViewOne.getText().toString());
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.detail_fab_two)
    public void detailFabTwo(View view) {

        emrgencySelected(emergencyTextViewTwo.getText().toString());
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.detail_fab_three)
    public void detailFabThree(View view) {

        emrgencySelected(emergencyTextViewThree.getText().toString());
    }

    private void setDetailEmergency(String emergencyType){
        policeMainButton.setText(emergencyType);
        healthContainer.setBackgroundResource(R.color.colorPrimary);
        healthMainButton.setVisibility(View.INVISIBLE);
        fireMainButton.setVisibility(View.INVISIBLE);
        fireFabHolder.setVisibility(View.VISIBLE);
        lineSeparator.setVisibility(View.INVISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_type, container, false);
        ButterKnife.bind(this, view);


        setUpTimerFab();


        return view;
    }

    private void setUpTimerFab(){
         new CountDownTimer(9000, 1000) {
            public void onTick(long millisUntilFinished) {
                fab.setImageBitmap(ImageUtil.textAsBitmap("" + millisUntilFinished / 1000, 100, Color.WHITE));
                fab.setClickable(false);
            }
            public void onFinish() {
                fab.setImageBitmap(ImageUtil.textAsBitmap("" + 0, 100, Color.WHITE));
                emrgencySelected("Emergency");


            }
        }.start();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                fab.show();
            }
        }, 50);
    }

    public void emrgencySelected(String emergency) {
        if (mListener != null) {
            mListener.onEmergencySelected(emergency);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainFragmentListener");
        }
    }

    private void setTextViews(String emergencyOne, String emergencyTwo, String emergencyThree){
        emergencyTextViewOne.setText(emergencyOne);
        emergencyTextViewTwo.setText(emergencyTwo);
        emergencyTextViewThree.setText(emergencyThree);
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
        void onEmergencySelected(String emergency);
    }
}
