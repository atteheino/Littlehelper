package fi.atteheino.littlehelper;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * Created by atte on 11.11.2015.
 */
public interface NotifyableBeaconListener {

    void notifyOnBeaconsRanged(Collection<Beacon> beacons, Region region);
}
