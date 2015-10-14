package fi.atteheino.littlehelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.altbeacon.beacon.Beacon;

import fi.atteheino.littlehelper.fragment.IBeaconDetailFragment;

public class IBeaconDetailActivity extends Activity
implements IBeaconDetailFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibeacon_detail_fragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent()!=null){
            Beacon device = getIntent().getExtras().getParcelable("id");
            IBeaconDetailFragment detailFragment = (IBeaconDetailFragment)getFragmentManager().findFragmentById(R.id.fragment3);
            detailFragment.updateFragment(device);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ibeacon_detail, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Beacon id) {

    }
}
