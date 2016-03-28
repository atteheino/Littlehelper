package fi.atteheino.littlehelper.log;

import com.orm.SugarRecord;

import java.util.Date;

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

    public void updateBleLog(String name, String MAC) {
        this.name = name;
        this.MAC = MAC;
        this.timestamp = new Date();
    }
}
