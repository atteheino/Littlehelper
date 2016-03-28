package fi.atteheino.littlehelper.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fi.atteheino.littlehelper.LittleHelperApplication;
import fi.atteheino.littlehelper.NotifyableBeaconListener;
import fi.atteheino.littlehelper.R;
import fi.atteheino.littlehelper.adapter.IBeaconArrayAdapter;
import fi.atteheino.littlehelper.container.BeaconWithRegion;
import fi.atteheino.littlehelper.log.BleLog;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class IBeaconListFragment extends Fragment implements AbsListView.OnItemClickListener, NotifyableBeaconListener {


    private static final String TAG = "IBeaconListFragment";
    private Handler mHandler;
    private OnFragmentInteractionListener mListener;
    private ArrayList<BeaconWithRegion> mLeDevicesList;
    private ArrayAdapter<BeaconWithRegion> mAdapter;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IBeaconListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLeDevicesList = new ArrayList<>();
        mAdapter = new IBeaconArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mLeDevicesList);
        mHandler = new Handler();

        //verifyBluetooth();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ibeacon, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.bluetoothDeviceList);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(getActivity()).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getActivity().finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    getActivity().finish();
                    System.exit(0);
                }

            });
            builder.show();

        }

    }

    public void update(final Collection<Beacon> collection, final Region region) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Device(s) found ");
                for (Beacon beacon : collection) {
                    addDevice(new BeaconWithRegion(beacon, region));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        // Now outside the UI Thread let's create a log entry to db. This could potentially take a while..
        for (Beacon beacon : collection) {
            logDevice(beacon);
        }
    }

    /**
     * Let's try to find existing device from log and update name, mac and timestamp.
     * If no device is found, then let's create a new entry.
     * @param beacon
     */
    private void logDevice(Beacon beacon) {

        final List<BleLog> bleLogs = BleLog.find(BleLog.class,
                "UUID=? and major=? and minor=?",
                beacon.getId1().toString(),
                beacon.getId2().toString(),
                beacon.getId3().toString());

        if (bleLogs.size()>0) {
            for (BleLog log : bleLogs) {
                log.updateBleLog(beacon.getBluetoothName(), beacon.getBluetoothAddress());
                log.save();
            }
        } else {
            BleLog logEntry = new BleLog(
                    beacon.getBluetoothName(),
                    beacon.getId1().toString(),
                    beacon.getId2().toString(),
                    beacon.getId3().toString(),
                    beacon.getBluetoothAddress());
            logEntry.save();
        }
    }

    /**
     * If device already exists in the list, let's update it in the list
     * @param device The Bluetooth Beacon to be added.
     */
    public void addDevice(BeaconWithRegion device) {
        if(mLeDevicesList.contains(device)) {
            mLeDevicesList.remove(device);
            mLeDevicesList.add(device);
        }else {
            mLeDevicesList.add(device);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //verifyBluetooth();
        ((LittleHelperApplication) getActivity().getApplicationContext()).addNotifyableBeaconListener(this);
        // Initializes list view adapter.
        if(mLeDevicesList == null){
            mLeDevicesList = new ArrayList<>();
        }
        if(mAdapter==null) {
            mAdapter = new IBeaconArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mLeDevicesList);
        }
        // Set the adapter
        mListView = (ListView) getActivity().findViewById(R.id.bluetoothDeviceList);
        mListView.setAdapter(mAdapter);

    }

    @Override
    public void onPause() {
        super.onPause();

        ((LittleHelperApplication) getActivity().getApplicationContext()).removeNotifyableBeaconListener(this);
        mAdapter.clear();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_ibeacon_list, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mAdapter.clear();

                break;
            case R.id.menu_stop:

                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(mAdapter.getItem(position));
        }
    }

    @Override
    public void notifyOnBeaconsRanged(Collection<Beacon> beacons, Region region) {
        update(beacons, region);
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


