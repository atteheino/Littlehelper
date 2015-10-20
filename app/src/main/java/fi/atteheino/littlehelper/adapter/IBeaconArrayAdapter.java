package fi.atteheino.littlehelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import fi.atteheino.littlehelper.R;

/**
 * Created by atte on 16.10.2015.
 */
public class IBeaconArrayAdapter extends ArrayAdapter {
    private static final String UNKNOWN_DEVICE = "Unknown Device";
    private List beacons;
    public IBeaconArrayAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        this.beacons = objects;
    }

    private static String formatDistanceForScreen(final double distance){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(distance);
    }

    private static String formatNameForScreen(final String name){
        if(name==null || name.isEmpty()){
            return UNKNOWN_DEVICE;
        } else {
            return name;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listitem_device, parent, false);
        TextView deviceName = (TextView) rowView.findViewById(R.id.device_name);
        TextView deviceAddress = (TextView) rowView.findViewById(R.id.device_address);
        TextView deviceDistance = (TextView) rowView.findViewById(R.id.distance);
        Beacon beacon = (Beacon) beacons.get(position);
        deviceName.setText(formatNameForScreen(beacon.getBluetoothName()));
        deviceAddress.setText(beacon.getBluetoothAddress());
        deviceDistance.setText(formatDistanceForScreen(beacon.getDistance()) + " meters");
        return rowView;
    }
}
