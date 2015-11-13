package fi.atteheino.littlehelper;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.TimedBeaconSimulator;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fi.atteheino.littlehelper.fragment.IBeaconDetailFragment;
import fi.atteheino.littlehelper.fragment.IBeaconListFragment;

/**
 * Created by atte on 9.10.2015.
 */
public class LittleHelperApplication extends Application implements BootstrapNotifier, RangeNotifier, BeaconConsumer {

    private static final String TAG = "LittleHelperApplication";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private IBeaconListFragment iBeaconListFragment = null;
    private IBeaconDetailFragment mIBeaconDetailFragment = null;
    private List<NotifyableBeaconListener> notifyableBeaconListenerList = null;
    private BeaconManager mBeaconManager;

    public void onCreate() {
        super.onCreate();
        mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
        // find a different type of beacon, you must specify the byte layout for that beacon's
        // advertisement with a line like below.  The example shows how to find a beacon with the
        // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb.  To find the proper
        // layout expression for other beacon types, do a web search for "setBeaconLayout"
        // including the quotes.
        //
        mBeaconManager.getBeaconParsers().clear();
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        // If you wish to test beacon detection in the Android Emulator, you can use code like this:

        BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
        ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();

        mBeaconManager.bind(this);
    }


    public void addNotifyableBeaconListener(NotifyableBeaconListener notifyableBeaconListener) {
        if(notifyableBeaconListenerList==null){
            notifyableBeaconListenerList = Collections.EMPTY_LIST;
        }
        notifyableBeaconListenerList.add(notifyableBeaconListener);
    }

    public void removeNotifyableBeaconListener(NotifyableBeaconListener notifyableBeaconListener){
        if(notifyableBeaconListenerList != null && notifyableBeaconListener!= null){
            notifyableBeaconListenerList.remove(notifyableBeaconListener);
        }
    }

    @Override
    public void didEnterRegion(Region arg0) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Log.d(TAG, "did enter region.");
        try {
            // start ranging for beacons.  This will provide an update once per second with the estimated
            // distance to the beacon in the didRAngeBeaconsInRegion method.
            mBeaconManager.startRangingBeaconsInRegion(arg0);
            mBeaconManager.setRangeNotifier(this);
        } catch (RemoteException e) {   }

    }

    @Override
    public void didExitRegion(Region region) {
        if (iBeaconListFragment != null) {

            Log.d(TAG, "JUST EXITED FROM REGION: " + region);
            //iBeaconListFragment.logToDisplay("I no longer see a beacon.");
        }
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        if (iBeaconListFragment != null) {
            Log.d(TAG, "STATE OF REGION CHANGED. New State: " + state + " for region: " + region);
            //iBeaconListFragment.logToDisplay("I have just switched from seeing/not seeing beacons: " + state);
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("LittleHelper")
                        .setContentText("An beacon is nearby.")
                        .setSmallIcon(R.mipmap.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void setiBeaconListFragment(IBeaconListFragment iBeaconListFragment) {
        this.iBeaconListFragment = iBeaconListFragment;
    }


    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");

            // The very first time since boot that we detect an beacon, we launch the
            // MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.INTENT_RANGED_REGION, region);
            // Important:  make sure to add android:launchMode="singleInstance" in the manifest
            // to keep multiple copies of this activity from getting created if the user has
            // already manually launched the app.
            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (notifyableBeaconListenerList != null && notifyableBeaconListenerList.size()>0) {
                // If the Fragment is visible, we log info about the beacons we have
                // seen on its display
                for(NotifyableBeaconListener listener : notifyableBeaconListenerList){
                    listener.notifyOnBeaconsRanged(collection, region);
                }

            } else {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.
                Log.d(TAG, "Noticed beacon but won't do anything about it as app is not in the foreground.");
                //sendNotification();

            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // release whatever is needed
        mBeaconManager.unbind(this);
        mBeaconManager = null;
    }

    @Override
    public void onBeaconServiceConnect() {
        // callback for when service connection is complete

        mBeaconManager.setBackgroundMode(true);

    }
}
