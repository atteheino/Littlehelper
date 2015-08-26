package fi.atteheino.littlehelper.test;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.util.FragmentTestUtil;

import fi.atteheino.littlehelper.BuildConfig;
import fi.atteheino.littlehelper.IBeaconListActivity;
import fi.atteheino.littlehelper.MainActivity;
import fi.atteheino.littlehelper.MainActivityFragment;
import fi.atteheino.littlehelper.R;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by atte on 30.6.2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivity_MainFragmentTest {

    @Test
    public void testStartScanButtonClick() {


        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        FragmentTestUtil.startVisibleFragment(mainActivityFragment);
        mainActivity.findViewById(R.id.startScanButton).performClick();

        Intent expectedIntent = new Intent(mainActivity, IBeaconListActivity.class);
        // Need to call this or assertion fails as StaertBluetooth is the first intent out of main activity
        Intent startBluetoothIntent = Shadows.shadowOf(mainActivity).getNextStartedActivity();
        // Now is the IBeanconListActivity opened?
        assertThat(Shadows.shadowOf(mainActivity).getNextStartedActivity(), equalTo(expectedIntent));


    }
}
