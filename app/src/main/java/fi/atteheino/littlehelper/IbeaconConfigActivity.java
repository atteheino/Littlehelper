package fi.atteheino.littlehelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wgx.common.util.Log;
import com.wgx.common.util.ToastUtil;
import com.wgx.ibeacon.api.BeaconController;
import com.wgx.ibeacon.api.BeaconnConfig;


public class IbeaconConfigActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibeacon_config);

        BeaconController.getInstance().bindActiviyPage(this);

        BeaconController.getInstance().setBeaconStateListener(new BeaconController.IBeaconStateListener() {

            @Override
            public void onWriteSuccess() {
                ToastUtil.showShort(IbeaconConfigActivity.this, "write success");
                closeProgress();
            }

            @Override
            public void onWriteStart() {
                ToastUtil.showShort(IbeaconConfigActivity.this, "write start");
                showProgress();
            }

            @Override
            public void onWriteFail(String arg0) {
                // TODO Auto-generated method stub
                ToastUtil.showShort(IbeaconConfigActivity.this, "write fail:" + arg0);
                closeProgress();

            }


            @Override
            public void onGetDeviceRssiInfo(int arg0) {
                Log.i("onGetDeviceRssiInfo:" + arg0);
            }

            @Override
            public void onGetDevicePowerInfo(int arg0) {
                Log.i("onGetDevicePowerInfo:" + arg0);

            }
        });
    }

    //3
    public void doWriteConfigClick(View v){
        BeaconnConfig config = new BeaconnConfig();
//        config.setmMPower(mMPower);
        BeaconController.getInstance().doWriteConfig(config);
    }

    //4
    public void doResetPwdClick(View v){
        BeaconController.getInstance().modifyDevicePwd(BeaconnConfig.DEFAULT_DEVICE_PWD, "123456");
    }

    //5
    public void doResetConfigClick(View v){
        BeaconController.getInstance().resetDeviceConfig("123456");

    }


    private ProgressDialog progressDialog;
    protected void showProgress() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(this, null, "write start");
        }
    }

    protected void closeProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        BeaconController.getInstance().unbindActiviyPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ibeacon_config, menu);
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
}
