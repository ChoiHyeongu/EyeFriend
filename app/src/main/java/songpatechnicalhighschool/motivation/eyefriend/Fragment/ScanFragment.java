package songpatechnicalhighschool.motivation.eyefriend.Fragment;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.List;

import songpatechnicalhighschool.motivation.eyefriend.Adapter.CustomAdapter;
import songpatechnicalhighschool.motivation.eyefriend.Blueinno.RFduinoService;
import songpatechnicalhighschool.motivation.eyefriend.R;

public class ScanFragment extends Fragment implements BeaconConsumer {

    BluetoothAdapter mBluetoothAdapter;
    private CustomAdapter customAdapter;
    Context context;
    ListView deviceList;

    Beacon exitBeacon1;
    Beacon exitBeacon2;
    private BeaconManager beaconManager;
    private List<Beacon> beaconList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private MediaPlayer arriveSound;
    private MediaPlayer induceSound;
    private boolean isAlreadyClicked = false;
    private boolean isArrived = false;
    private float speed = 0;
    private int course = 1;

    private View testButton;

    public ScanFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // ignore the notification here
            // and block propagation
            abortBroadcast();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beaconManager = BeaconManager.getInstanceForApplication(getActivity());
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter ifi = new IntentFilter("be.hcpl.android.beaconexample.NOTIFY_FOR_BEACON");
        ifi.setPriority(10);
        getActivity().registerReceiver(mReceiver, ifi);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        customAdapter = new CustomAdapter();

        context = getContext();
        deviceList = view.findViewById(R.id.scan_device_list);
        testButton = view.findViewById(R.id.scan_test_btn);

        testButton.setOnClickListener(v->clickList());

        induceSound = MediaPlayer.create(getContext(), R.raw.beep);
        induceSound.setLooping(true);
        arriveSound = MediaPlayer.create(getContext(), R.raw.arrive);
        arriveSound.setLooping(true);

        customAdapter.setContext(context);
        customAdapter.addElement("120984712894372", "종합안내데스크", "코엑스 1층 동문");
        customAdapter.addElement("120984712894372", "무역센터 의무실", "코엑스 1층 서문 옆");
        customAdapter.addElement("120984712894372", "별마당도서관", "스타필드 코엑스몰 지하 1층, 1층");
        customAdapter.addElement("120984712894372", "KFC", "코엑스몰 스타필드 지하 1층");
        customAdapter.addElement("120984712894372", "삼성모바일스토어", "코엑스몰 스타필드 지하 2층");

        deviceList.setAdapter(customAdapter);

        return view;
    }

    public void clickList(){ handler.sendEmptyMessage(0); }


    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(mReceiver);
    }

    //Beacon Connect
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //state
            if (exitBeacon1 != null || exitBeacon1.getBluetoothName().equals("IF0126363")) {
                String text = ("ID : " + exitBeacon1.getId2() + " / " + "Distance = " + exitBeacon1.getDistance());
                Log.d("Beacon", text);
                if(course == 1){
                    soundPlay(exitBeacon1.getDistance());
                } else if(course == 2) {
                    soundPlay(exitBeacon2.getDistance());
                }
                handler.sendEmptyMessageDelayed(0, 1500);
            }
        }
    };

    void soundPlay(Double distance) {

        if (distance < 2) {
            if (induceSound != null && induceSound.isPlaying()) {
                induceSound.pause();
                if(course == 1){
                    course++;
                }
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

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier((beacons, region) -> {
            if (beacons.size() > 0) {
                beaconList.clear();
                for (Beacon beacon : beacons) {
                    beaconList.add(beacon);
                    if (beacon.getBluetoothName().equals("IF0126363")) {
                        exitBeacon1 = beacon;
                        Log.d("ScanBeacon", exitBeacon1.getBluetoothName());
                    }

                    if (beacon.getBluetoothName().equals("MiniBeacon_26192")) {
                        exitBeacon2 = beacon;
                        Log.d("ScanBeacon", exitBeacon2.getBluetoothName());
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    @Override
    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return getActivity().bindService(intent, serviceConnection, i);
    }
}
