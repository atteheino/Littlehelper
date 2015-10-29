package fi.atteheino.littlehelper.container;

import android.os.Parcel;
import android.os.Parcelable;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

/**
 * Created by atte on 27.10.2015.
 */
public class BeaconWithRegion extends Beacon {

    public static final Parcelable.Creator<BeaconWithRegion> CREATOR = new Creator<BeaconWithRegion>(

    ) {
        @Override
        public BeaconWithRegion createFromParcel(Parcel source) {
            return new BeaconWithRegion(source);
        }

        @Override
        public BeaconWithRegion[] newArray(int size) {
            return new BeaconWithRegion[0];
        }
    };
    private Region region;

    protected BeaconWithRegion(Parcel in){
        super(in);
        this.region = in.readParcelable(Region.class.getClassLoader());
    }

    public BeaconWithRegion(Beacon beacon, Region region) {
        super(beacon);
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(region, flags);
    }
}
