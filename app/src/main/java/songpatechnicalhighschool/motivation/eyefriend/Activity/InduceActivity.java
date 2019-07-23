package songpatechnicalhighschool.motivation.eyefriend.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import songpatechnicalhighschool.motivation.eyefriend.Service.BluetoothLeService;
import songpatechnicalhighschool.motivation.eyefriend.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class InduceActivity extends FragmentActivity {

    private ArrayList<Integer> iterationsSinceConnect = null;
    private static final int MY_PERMISSION_REQUEST_CONSTANT = 1;
    public static String macs;
    private ArrayList<BluetoothLeService> LEs = null;
    private Thread t = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void reconnect1(int i) {
        if(LEs == null)
            return;
        iterationsSinceConnect.set(i, 0);
        LEs.get(i).disconnect();
        LEs.get(i).close();
        LEs.get(i).initialize(getApplicationContext());
        LEs.get(i).connect(macs);
        LEs.get(i).last_rssi_success = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void reconnect(View v) {
        if(LEs == null)
            return;
        for(int i = 0; i < LEs.size(); i++)
            reconnect1(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void refresh1(int i) {
        if(LEs == null)
            return;
        if(iterationsSinceConnect.get(i) > 5 && LEs.get(i).last_rssi_success == 0) {
            reconnect1(i);
            return;
        }

        //addEntry(i, LEs.get(i).last_rssi);

        if(iterationsSinceConnect.get(i) >= 3) {
            LEs.get(i).readRssi();
        }

        iterationsSinceConnect.set(i, iterationsSinceConnect.get(i) + 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void refresh(View v) {
        if(LEs == null)
            return;
        for(int i = 0; i < LEs.size(); i++)
            refresh1(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_induce);

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CONSTANT);

        startActivity();
    }

    public void startActivity(){
        try {
            macs = (String) getIntent().getSerializableExtra("mac");
            Log.d("Macs", macs);
        }
        catch(Exception e)
        {
            macs = null;
            //Log.d("Macs", macs);
        }

        Log.d("STATE", "MAIN" + macs);
        if(macs != null) {
            int L = 1;
            iterationsSinceConnect = new ArrayList<Integer>();
            for(int i = 0; i < L; i++) iterationsSinceConnect.add(0);

            LEs = new ArrayList<BluetoothLeService>();
            for (int i = 0; i < L; i++) {
                LEs.add(new BluetoothLeService());
                reconnect1(i);
            }

            spawnUpdateThread();
        }
    }

    public void stopUpdateThread(View v) {
        if(t != null)
            t.interrupt();
    }

    private void spawnUpdateThread() {
        if(LEs == null)
            return;
        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(300);
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                            @Override
                            public void run() {
                                refresh(null);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("STATE", "Permission OK");
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
