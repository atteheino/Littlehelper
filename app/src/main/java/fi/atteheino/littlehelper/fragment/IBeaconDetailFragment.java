package fi.atteheino.littlehelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fi.atteheino.littlehelper.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IBeaconDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class IBeaconDetailFragment extends Fragment {
    private static final String ARG_ID = "id";
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();

                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            setBluetoothServices(services);
            gatt.readCharacteristic(services.get(1).getCharacteristics().get
                    (0));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            setBluetoothCharacteristics(characteristic);
            gatt.disconnect();
        }
    };
    private BluetoothDevice mId;
    private BluetoothGatt mGatt;
    private OnFragmentInteractionListener mListener;

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

    public void updateFragment(BluetoothDevice id) {
        connectToGatt(id);
    }

    private void connectToGatt(BluetoothDevice device) {
        mGatt = device.connectGatt(getActivity(), false, gattCallback);
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

    private void setBluetoothCharacteristics(final BluetoothGattCharacteristic characteristic) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView bluetoothCharacteristics = (TextView) getActivity().findViewById(R.id.bluetooth_characteristics);
                bluetoothCharacteristics.setText(characteristic.toString());
            }
        });
    }

    private void setBluetoothServices(final List<BluetoothGattService> services){
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        TextView bluetoothServices = (TextView) getActivity().findViewById(R.id.bluetooth_services);
                        StringBuilder sb = new StringBuilder();
                        for(BluetoothGattService service: services){
                           for(BluetoothGattCharacteristic characteristic: service.getCharacteristics()) {
                               sb.append("\nUUID: \n");
                               sb.append(characteristic.getUuid());
                               sb.append("\nValue: \n");
                               sb.append(characteristic.getValue());
                           }
                        }
                        bluetoothServices.setText(sb.toString());

                    }
                }
        );
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
        public void onFragmentInteraction(BluetoothDevice id);
    }
}


