package fi.atteheino.littlehelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;

import fi.atteheino.littlehelper.log.BleLog;

public class BLELogActivity extends Activity {

    private static final String TAG = "BLELogActivity";
    private ArrayList<String> mBleLogsList;
    private ArrayAdapter<String> mAdapter;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blelog);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mBleLogsList = getBleLogs();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mBleLogsList);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
    }

    private ArrayList<String> getBleLogs() {
        ArrayList<String> tempList = new ArrayList<>();
        final Iterator<BleLog> bleLogIterator = BleLog.findAll(BleLog.class);
        while(bleLogIterator.hasNext()) {
            BleLog log = bleLogIterator.next();
            tempList.add(log.prettyPrint());
        }
        return tempList;
    }

}
