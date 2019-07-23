package songpatechnicalhighschool.motivation.eyefriend.Fragment;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import songpatechnicalhighschool.motivation.eyefriend.Blueinno.RFduinoService;
import songpatechnicalhighschool.motivation.eyefriend.Client.RetrofitClient;
import songpatechnicalhighschool.motivation.eyefriend.Module.Weather;
import songpatechnicalhighschool.motivation.eyefriend.R;
import songpatechnicalhighschool.motivation.eyefriend.Service.WeatherService;

import static android.content.Context.BIND_AUTO_CREATE;

public class QuickFragment extends Fragment {

    public static BluetoothDevice exitDevice;
    final String TAG = "QuickFragment";
    final String OPEN_WEATHER_MAP_KEY = "704a83a5f8f3436366adba0f15c18d38";
    final String emergencyTell = "tel:119";
    WeatherService weatherService;
    LocationManager lm;
    double longitude;
    double latitude;
    int lastDistance = 0;

    private RFduinoService rfduinoService;


    private static final int REQUEST_CODE_LOCATION = 2;
    Button micButton;
    Button kitButton;
    Button emergencyButton;
    Button callButton;
    Intent intent;
    SpeechRecognizer speechRecognizer;
    TextToSpeech tts;
    String speakingValue;
    RetrofitClient retrofitClient;

    public QuickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(bluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        getContext().registerReceiver(rfduinoReceiver, RFduinoService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();

        getContext().unregisterReceiver(bluetoothStateReceiver);
        getContext().unregisterReceiver(rfduinoReceiver);
    }

    private final BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            if (state == BluetoothAdapter.STATE_ON) {
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
            } else if (state == BluetoothAdapter.STATE_OFF) {
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final ServiceConnection rfduinoServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "rfduinoServiceConnection");
            rfduinoService = ((RFduinoService.LocalBinder) service).getService();
            if (rfduinoService.initialize()) {
                if (rfduinoService.connect("C3:69:48:21:36:C8")) {
                    Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            rfduinoService = null;
            Toast.makeText(getContext(), "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    private final BroadcastReceiver rfduinoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "rfduinoReceiver");
            final String action = intent.getAction();
            if (RFduinoService.ACTION_CONNECTED.equals(action)) {
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            } else if (RFduinoService.ACTION_DISCONNECTED.equals(action)) {
                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
            } else if (RFduinoService.ACTION_DATA_AVAILABLE.equals(action)) {
                addData(intent.getByteArrayExtra(RFduinoService.EXTRA_DATA));
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick, container, false);

        weatherService = new WeatherService();
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getContext().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizer.setRecognitionListener(listener);
        retrofitClient = new RetrofitClient(OPEN_WEATHER_MAP_KEY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

        }

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        tts = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.KOREAN);
            }
        });

        micButton = view.findViewById(R.id.quick_voice);
        micButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 100);
            } else {
                try {
                    speechRecognizer.startListening(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        kitButton = view.findViewById(R.id.quick_on_kit);
        kitButton.setOnClickListener(v -> onKit());

        emergencyButton = view.findViewById(R.id.quick_emergency);
        emergencyButton.setOnClickListener(v -> startActivity(new Intent("android.intent.action.CALL", Uri.parse(emergencyTell))));

        callButton = view.findViewById(R.id.quick_call);
        callButton.setOnClickListener(v -> startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:010-2269-1061"))));

        return view;
    }

    private void onKit() {
        Log.d(TAG, "connectButton");
        Intent rfduinoIntent = new Intent(getContext(), RFduinoService.class);
        getContext().bindService(rfduinoIntent, rfduinoServiceConnection, BIND_AUTO_CREATE);
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d("Speech", "onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Toast.makeText(getContext(), "음성인식 시작", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            Log.d("Speech", "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d("Speech", "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d("Speech", "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
            Toast.makeText(getContext(), "다시 말해주세요.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.d("Speech", "onPartialResults");
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d("Speech", "onEvent");
        }

        @Override
        public void onResults(Bundle results) {
            String key;
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            speakingValue = mResult.toString();
            speakingValue = speakingValue.substring(1, speakingValue.length() - 1);
            if (speakingValue.equals("오늘 날씨")) {
                Location location = getLocation();
                getCurrentWeather(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            } else if(speakingValue.equals("키트")){
                onKit();
            }else{
                Toast.makeText(getContext(), speakingValue, Toast.LENGTH_LONG).show();
            }
        }
    };

    public void getCurrentWeather(String latitude, String longitude) {

        final Call<JsonObject> res = RetrofitClient
                .getInstance()
                .buildRetrofit()
                .getCurrentWeather(latitude, longitude, OPEN_WEATHER_MAP_KEY);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String jsonObj = null;
                if (response.body() != null) {
                    jsonObj = response.body().toString();
                    Log.d("JSONOBJ", jsonObj);
                    Weather weather = weatherService.weatherToText(response.body());
                    String speechText = makeSentence(weather);
                    Log.d("WeatherText", speechText);
                    tts.setSpeechRate(0.8f); //1배속으로 읽기
                    tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    System.out.println(response.toString());
                }
                Log.d("Retrofit", "Success :" + jsonObj);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Retrofit", "Failure : " + t.getMessage());
            }
        });
    }

    private String makeSentence(Weather weather){
        String sentence = "오늘 날씨는 "
                + weather.getText()
                +" 오늘의 기온은 "
                +weather.getTemperature()
                +" 도 입니다.";

        return sentence;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer.cancel();
            speechRecognizer = null;
        }
    }

    private Location getLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
            getLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        } else {

            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = lm.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                latitude = currentLocation.getLongitude();
                longitude = currentLocation.getLatitude();
                Log.d("GPSlocation", longitude + ", " + latitude);
                retrofitClient.getCurrentWeather(Double.toString(longitude), Double.toString(latitude));
            }
        }
        return currentLocation;
    }

    private void addData(byte[] data) {
        String value = String.valueOf(data[0]);
        Log.d("AddData", value);
        checkLedLight(value);
    }

    private void checkLedLight(String value) {

        byte[] on = {1};
        byte[] off = {0};
        int distance = Integer.parseInt(value);

        if(distance != lastDistance) {
            if (distance > 5 && distance < 50) {
                rfduinoService.send(on);
                tts.setSpeechRate(0.8f); //1배속으로 읽기
                tts.speak(value + "cm 앞에 물체가 있습니다.", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                rfduinoService.send(off);
            }
        }
        lastDistance = distance;
    }
}
