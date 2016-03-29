package fi.atteheino.littlehelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import fi.atteheino.littlehelper.container.BeaconWithRegion;
import fi.atteheino.littlehelper.fragment.IBeaconListFragment;

public class IBeaconListActivity extends Activity
implements IBeaconListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibeacon_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ibeacon_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id ==  R.id.menu_show_log) {
            Intent intent = new Intent();
            intent.setClass(this, BLELogActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(BeaconWithRegion id) {
        Intent intent = new Intent(this, IBeaconDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
