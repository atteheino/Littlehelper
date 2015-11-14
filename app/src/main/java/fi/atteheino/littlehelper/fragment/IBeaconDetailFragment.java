package fi.atteheino.littlehelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import fi.atteheino.littlehelper.LittleHelperApplication;
import fi.atteheino.littlehelper.NotifyableBeaconListener;
import fi.atteheino.littlehelper.R;
import fi.atteheino.littlehelper.container.BeaconWithRegion;
import fi.atteheino.littlehelper.utils.DisplayHelpers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IBeaconDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class IBeaconDetailFragment extends Fragment implements  NotifyableBeaconListener {
    private static final String ARG_ID = "id";
    private static final String TAG = "IBeaconDetailFragment";

    private BeaconWithRegion mId;
    private OnFragmentInteractionListener mListener;


    // UI Components
    private TextView mDeviceName;

    public IBeaconDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getParcelable(ARG_ID);
            updateFragment(mId);
        }

    }

    public void updateFragment(BeaconWithRegion id) {
        mId = id;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ibeacon_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setVisibility(View.VISIBLE);
        // Get references to UI Components
        // Only need to do this once
        mDeviceName =  (TextView) getView().findViewById(R.id.device_name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LittleHelperApplication) getActivity().getApplicationContext()).addNotifyableBeaconListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        ((LittleHelperApplication) getActivity().getApplicationContext()).removeNotifyableBeaconListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }


    private void updateValuesInDisplay(final Collection<Beacon> collection) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Beacon beacon : collection) {
                    if (beacon.getBluetoothAddress().equals(mId.getBluetoothAddress())) {

                        mDeviceName.setText(DisplayHelpers.formatNameForScreen(beacon.getBluetoothName()));
                    }
                }
            }
        });
    }

    @Override
    public void notifyOnBeaconsRanged(Collection<Beacon> beacons, Region region) {
        updateValuesInDisplay(beacons);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(BeaconWithRegion id);
    }
}


