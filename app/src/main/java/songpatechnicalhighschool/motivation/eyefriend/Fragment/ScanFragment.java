package songpatechnicalhighschool.motivation.eyefriend.Fragment;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import songpatechnicalhighschool.motivation.eyefriend.Adapter.CustomAdapter;
import songpatechnicalhighschool.motivation.eyefriend.R;

public class ScanFragment extends Fragment {

    BluetoothAdapter mBluetoothAdapter;
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

        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //mBluetoothAdapter.startDiscovery();

        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //context.registerReceiver(mReceiver, filter);

        customAdapter.setContext(context);
        customAdapter.addElement("120984712894372", "장애인용 화장실", "1층 주차고객센터 통로 입구");
        customAdapter.addElement("120984712894372", "종합안내데스크", "코엑스 1층 동문");
        customAdapter.addElement("120984712894372", "무역센터 의무실", "코엑스 1층 서문 옆");
        customAdapter.addElement("120984712894372", "별마당도서관", "스타필드 코엑스몰 지하 1층, 1층");
        customAdapter.addElement("120984712894372", "KFC", "코엑스몰 스타필드 지하 1층");
        customAdapter.addElement("120984712894372", "삼성모바일스토어", "코엑스몰 스타필드 지하 2층");;

        deviceList.setAdapter(customAdapter);

        return view;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                customAdapter.setContext(context);
                Log.i("BT", device.getName() + "\n" + device.getAddress());
                if (isOurDevice(device.getName())) {
                    //customAdapter.addElement(device.getAddress(), device.getName());
                    deviceList.setAdapter(customAdapter);
                    isExit(device);
                }
            }
        }
    };

    boolean isOurDevice(String device) {
        if (device != null && device.length() >= 3) {
            device = device.substring(0, 3);
            Log.d("Device Name", device);
            if (device.equals("STH"))
                return true;
            else
                return false;
        } else
            return false;
    }

    void isExit(BluetoothDevice device){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(mReceiver);
    }
}
