package fi.atteheino.littlehelper.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fi.atteheino.littlehelper.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IBeaconDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IBeaconDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IBeaconDetailFragment extends Fragment {
    private static final String ARG_ID = "id";
    private String mId;


    private OnFragmentInteractionListener mListener;

    public IBeaconDetailFragment() {
        // Required empty public constructor
    }

    /**
     * @param id ID of IBeacon to show.
     * @return A new instance of fragment IBeaconDetailFragment.
     */
    public static IBeaconDetailFragment newInstance(BluetoothDevice id) {
        IBeaconDetailFragment fragment = new IBeaconDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(ARG_ID);
        }
    }

    public void updateFragment(BluetoothDevice id) {

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
