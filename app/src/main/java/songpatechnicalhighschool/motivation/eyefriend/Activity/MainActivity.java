package songpatechnicalhighschool.motivation.eyefriend.Activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import songpatechnicalhighschool.motivation.eyefriend.Adapter.PageAdapter;
import songpatechnicalhighschool.motivation.eyefriend.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final int MY_PERMISSION_REQUEST_CONSTANT = 1;

    private BeaconManager beaconManager;
    private List<Beacon> beaconList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private MediaPlayer arriveSound;
    private MediaPlayer induceSound;
    private boolean isAlreadyClicked = false;
    private boolean isArrived = false;
    private float speed = 0;
    private Beacon exitBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabs = findViewById(R.id.tabs);

        tabs.addTab(tabs.newTab().setText("빠른 이용"));
        tabs.addTab(tabs.newTab().setText("주변 목록"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        induceSound = MediaPlayer.create(this, R.raw.beep);
        induceSound.setLooping(true);
        arriveSound = MediaPlayer.create(this, R.raw.arrive);
        arriveSound.setLooping(true);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        final PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        final FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> onFabClicked(view));

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CONSTANT);
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
        beaconManager.unbind(this);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier((beacons, region) -> {
            if (beacons.size() > 0) {
                beaconList.clear();
                for (Beacon beacon : beacons) {
                    beaconList.add(beacon);
                    if (beacon.getBluetoothName().equals("IF0126363")) {
                        exitBeacon = beacon;
                    }
                    Log.d("BeaconsList", beacon.getBluetoothName());
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    public void onFabClicked(View view) {
        handler.sendEmptyMessage(0);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (exitBeacon != null || exitBeacon.getBluetoothName().equals("IF0126363")) {
                String text = ("ID : " + exitBeacon.getId2() + " / " + "Distance = " + exitBeacon.getDistance());
                Log.d("Beacon", text);
                soundPlay(exitBeacon.getDistance());
                handler.sendEmptyMessageDelayed(0, 1500);
            }
        }
    };

    void soundPlay(Double distance) {

        Toast.makeText(this, "Distance : " + exitBeacon.getDistance(), Toast.LENGTH_SHORT).show();
        if (distance < 2) {
            if (induceSound != null && induceSound.isPlaying()) {
                induceSound.pause();
            }
            arriveSound.start();
        } else {
            if (arriveSound != null && arriveSound.isPlaying()) {
                arriveSound.pause();
            }
            speed = (float) (10 / (((distance * distance) / 2) + 5));
                if (induceSound.isPlaying()) {
                    induceSound.pause();
                    induceSound.setPlaybackParams(induceSound.getPlaybackParams().setSpeed(speed));
                } else {
                    induceSound.setPlaybackParams(induceSound.getPlaybackParams().setSpeed(speed));
                }
                induceSound.start();
        }
        Log.d("Beacon", speed + "");
        Log.d("speed", String.valueOf(speed));
    }
}
