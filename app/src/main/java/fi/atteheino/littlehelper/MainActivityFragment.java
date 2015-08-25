package fi.atteheino.littlehelper;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wgx.common.util.Log;
import com.wgx.common.util.ToastUtil;
import com.wgx.ibeacon.api.BeaconController;
import com.wgx.ibeacon.api.BeaconnConfig;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    boolean mDualPane;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    View.OnClickListener startScanButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //ToastUtil.show(getActivity().getApplicationContext(), R.string.start_scan_text, Toast.LENGTH_LONG);
            if (mDualPane) {
                // Create new fragment and transaction
                Fragment newFragment = new IBeaconListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.details, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            } else {
                Intent intent = new Intent();
                intent.setClass(getActivity(), IBeaconListFragment.class);
                startActivity(intent);
            }

        }
    };

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
