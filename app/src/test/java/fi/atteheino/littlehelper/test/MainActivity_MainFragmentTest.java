package fi.atteheino.littlehelper.test;

import android.bluetooth.BluetoothManager;
import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.FragmentTestUtil;

import fi.atteheino.littlehelper.BuildConfig;
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
        final String startScanText = mainActivity.getResources().getString(R.string.start_scan_text);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(startScanText));
    }
}
