package fi.atteheino.littlehelper.log;

import com.orm.SugarRecord;

import org.altbeacon.beacon.Beacon;

import java.util.Date;
import java.util.List;

import fi.atteheino.littlehelper.utils.DisplayHelpers;

/**
 * Created by atte on 27.3.2016.
 */
public class BleLog extends SugarRecord {
    private String name;
    private String UUID;
    private String major;
    private String minor;
    private String MAC;
    private Date timestamp;

    public BleLog(){

    }

    public BleLog(String name, String UUID, String major, String minor, String MAC) {
        this.name = name;
        this.UUID = UUID;
        this.major = major;
        this.minor = minor;
        this.MAC = MAC;
        this.timestamp = new Date();
    }

    /**
     * Let's try to find existing device from log and update name, mac and timestamp.
     * If no device is found, then let's create a new entry.
     * @param beacon
     */
    public static void logDevice(final Beacon beacon) {

        final List<BleLog> bleLogs = BleLog.find(BleLog.class,
                "UUID=? and major=? and minor=?",
                beacon.getId1().toString(),
                beacon.getId2().toString(),
                beacon.getId3().toString());

        if (bleLogs.size()>0) {
            for (BleLog log : bleLogs) {
                log.updateBleLog(beacon.getBluetoothName(), beacon.getBluetoothAddress());
                log.save();
            }
        } else {
            BleLog logEntry = new BleLog(
                    beacon.getBluetoothName(),
                    beacon.getId1().toString(),
                    beacon.getId2().toString(),
                    beacon.getId3().toString(),
                    beacon.getBluetoothAddress());
            logEntry.save();
        }
    }

    public void updateBleLog(String name, String MAC) {
        this.name = name;
        this.MAC = MAC;
        this.timestamp = new Date();
    }

    public String prettyPrint() {
        return "Name: " + DisplayHelpers.formatNameForScreen(name) + "\n" +
                "UUID: " + UUID + " " +
                "Major: " + major + " " +
                "Minor: " + minor + "\n" +
                "Last seen: " + timestamp;


    }



}
