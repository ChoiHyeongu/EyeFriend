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
import songpatechnicalhighschool.motivation.eyefriend.Fragment.QuickFragment;
import songpatechnicalhighschool.motivation.eyefriend.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

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

    }
}
