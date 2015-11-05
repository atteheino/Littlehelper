package fi.atteheino.littlehelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fi.atteheino.littlehelper.R;
import fi.atteheino.littlehelper.container.BeaconWithRegion;
import fi.atteheino.littlehelper.utils.DisplayHelpers;

/**
 * Created by atte on 16.10.2015.
 */
public class IBeaconArrayAdapter extends ArrayAdapter {
    private List beacons;
    public IBeaconArrayAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        this.beacons = objects;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listitem_device, parent, false);
        TextView deviceName = (TextView) rowView.findViewById(R.id.device_name);
        TextView deviceAddress = (TextView) rowView.findViewById(R.id.device_address);
        TextView deviceDistance = (TextView) rowView.findViewById(R.id.distance);
        BeaconWithRegion beacon = (BeaconWithRegion) beacons.get(position);
        deviceName.setText(DisplayHelpers.formatNameForScreen(beacon.getBluetoothName()));
        deviceAddress.setText(beacon.getBluetoothAddress());
        deviceDistance.setText(DisplayHelpers.formatDistanceForScreen(beacon.getDistance()) + " meters");
        return rowView;
    }
}
