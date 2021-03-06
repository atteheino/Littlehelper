package fi.atteheino.littlehelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import fi.atteheino.littlehelper.container.BeaconWithRegion;
import fi.atteheino.littlehelper.fragment.IBeaconDetailFragment;
import fi.atteheino.littlehelper.fragment.IBeaconListFragment;
import fi.atteheino.littlehelper.log.BleLog;


public class MainActivity extends Activity
        implements IBeaconListFragment.OnFragmentInteractionListener,
        IBeaconDetailFragment.OnFragmentInteractionListener {

    public static final String DETAILS = "details";
    private static final int REQUEST_ENABLE_BT = 1;
    private boolean mDualPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        mDualPane = getResources().getBoolean(R.bool.dual_pane);;

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id ==  R.id.menu_show_log) {
            Intent intent = new Intent();
            intent.setClass(this, BLELogActivity.class);
            startActivity(intent);
        }
        if (id == R.id.menu_drop_blelog) {
            confirmDeleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDeleteAll() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.pref_drop_database_title))
                .setMessage(
                        getResources().getString(R.string.pref_drop_database_text))
                .setIcon(
                        getResources().getDrawable(
                                android.R.drawable.ic_dialog_alert))
                .setPositiveButton(
                        getResources().getString(R.string.PostiveYesButton),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                BleLog.deleteAll(BleLog.class);

                            }
                        })
                .setNegativeButton(
                        getResources().getString(R.string.NegativeNoButton),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Do Something Here
                            }
                        }).show();
    }


    @Override
    public void onFragmentInteraction(BeaconWithRegion id) {


            if (mDualPane) {

                IBeaconDetailFragment fragment = (IBeaconDetailFragment) getFragmentManager()
                        .findFragmentByTag(DETAILS);
                //Details fragment has not been created yet.
                if(fragment == null) {
                    // Create new fragment and transaction
                    Fragment newFragment = new IBeaconDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("id", id);
                    newFragment.setArguments(bundle);


                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.replace(R.id.details, newFragment, DETAILS);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();
                } else {
                   fragment.updateFragment(id);
                }

            } else {
                Intent intent = new Intent();
                intent.setClass(this, IBeaconDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }




    }
}
