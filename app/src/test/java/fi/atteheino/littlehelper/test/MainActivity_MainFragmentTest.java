package fi.atteheino.littlehelper.test;


import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.TimedBeaconSimulator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.FragmentTestUtil;

import fi.atteheino.littlehelper.BuildConfig;
import fi.atteheino.littlehelper.MainActivity;
import fi.atteheino.littlehelper.fragment.IBeaconListFragment;

/**
 * Created by atte on 30.6.2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivity_MainFragmentTest {

    @Test
    public void testIBeaconList() {

        BeaconManager.setBeaconSimulator(new TimedBeaconSimulator());
        ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();


        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        IBeaconListFragment iBeaconListFragment = new IBeaconListFragment();
        FragmentTestUtil.startVisibleFragment(iBeaconListFragment);

        /*assertThat("List of iBeacons is empty", );

        Intent expectedIntent = new Intent(mainActivity, IBeaconListActivity.class);
        // Need to call this or assertion fails as StaertBluetooth is the first intent out of main activity
        Intent startBluetoothIntent = Shadows.shadowOf(mainActivity).getNextStartedActivity();
        // Now is the IBeanconListActivity opened?
        assertThat(Shadows.shadowOf(mainActivity).getNextStartedActivity(), equalTo(expectedIntent));*/


    }
}
