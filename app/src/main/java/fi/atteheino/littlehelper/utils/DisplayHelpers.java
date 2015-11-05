package fi.atteheino.littlehelper.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by atte on 5.11.2015.
 */
public class DisplayHelpers {
    private static final String UNKNOWN_DEVICE = "Unknown Device";


    public static String formatDistanceForScreen(final double distance){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(distance);
    }

    public static String formatNameForScreen(final String name){
        if(name==null || name.isEmpty()){
            return UNKNOWN_DEVICE;
        } else {
            return name;
        }
    }
}
