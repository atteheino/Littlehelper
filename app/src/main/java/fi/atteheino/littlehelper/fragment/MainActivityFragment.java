package fi.atteheino.littlehelper.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fi.atteheino.littlehelper.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    boolean mDualPane;
    View.OnClickListener startScanButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //ToastUtil.show(getActivity().getApplicationContext(), R.string.start_scan_text, Toast.LENGTH_LONG);

                // Create new fragment and transaction
                Fragment newFragment = new IBeaconListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragmentContainer, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();



        }
    };


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;


        Button startScanButton = (Button) getActivity().findViewById(R.id.startScanButton);
        startScanButton.setOnClickListener(startScanButtonOnClickListener);


    }
}
