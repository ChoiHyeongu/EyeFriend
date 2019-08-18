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

public class ScanFragment extends Fragment {

    private CustomAdapter customAdapter;
    Context context;
    ListView deviceList;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        customAdapter = new CustomAdapter();

        context = getContext();
        deviceList = view.findViewById(R.id.scan_device_list);

        customAdapter.setContext(context);
        customAdapter.addElement("120984712894372", "종합안내데스크", "코엑스 1층 동문");
        customAdapter.addElement("120984712894372", "무역센터 의무실", "코엑스 1층 서문 옆");
        customAdapter.addElement("120984712894372", "별마당도서관", "스타필드 코엑스몰 지하 1층, 1층");
        customAdapter.addElement("120984712894372", "KFC", "코엑스몰 스타필드 지하 1층");
        customAdapter.addElement("120984712894372", "삼성모바일스토어", "코엑스몰 스타필드 지하 2층");

        deviceList.setAdapter(customAdapter);

        return view;
    }
}
