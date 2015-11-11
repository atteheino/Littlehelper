package fi.atteheino.littlehelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
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
public class IBeaconDetailFragment extends Fragment implements BeaconConsumer, NotifyableBeaconListener {
    private static final String ARG_ID = "id";
    private static final String TAG = "IBeaconDetailFragment";

    private BeaconWithRegion mId;
    private OnFragmentInteractionListener mListener;
    private BeaconManager mBeaconManager = null;

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
        //verifyBluetooth();
        mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getActivity());
        mBeaconManager.bind(this);
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
        mDeviceName =  (TextView) getActivity().findViewById(R.id.device_name);
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
        mBeaconManager.setBackgroundMode(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    @Override
    public void onPause() {
        super.onPause();
        mBeaconManager.setBackgroundMode(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeaconManager.unbind(this);
        try {
            mBeaconManager.stopRangingBeaconsInRegion(mId.getRegion());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.d(TAG, "Collection of beacons found.");
                Log.d(TAG, "Size of collection: " + collection.size());
                updateValuesInDisplay(collection);
            }
        });

        try {
            // and start ranging for the given region. This region has a uuid specificed so will
            // only react on beacons with this uuid, the 2 other fields are minor and major version
            // to be more specific if desired
            mBeaconManager.startRangingBeaconsInRegion(mId.getRegion());
        } catch (RemoteException e) {
            Log.e(IBeaconDetailFragment.class.getSimpleName(), "Failed to start ranging", e);
        }
    }

    private void updateValuesInDisplay(final Collection<Beacon> collection) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Beacon beacon: collection){
                    if(beacon.getBluetoothAddress().equals(mId.getBluetoothAddress())){

                        mDeviceName.setText(DisplayHelpers.formatNameForScreen(beacon.getBluetoothName()));
                    }
                }
            }
        });
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
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


