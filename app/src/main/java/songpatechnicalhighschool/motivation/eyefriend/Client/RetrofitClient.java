package songpatechnicalhighschool.motivation.eyefriend.Client;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    public static RetrofitClient retrofitClient = new RetrofitClient();
    String OPEN_WEATHER_MAP_KEY;

    public RetrofitClient() {

    }

    public RetrofitClient(String OPEN_WEATHER_MAP_KEY) {
        this.OPEN_WEATHER_MAP_KEY = OPEN_WEATHER_MAP_KEY;
    }

    public static RetrofitClient getInstance() {
        return retrofitClient;
    }

    public RetrofitService buildRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        return retrofitService;
    }

    public void getCurrentWeather(String latitude, String longitude) {

        final Call<JsonObject> res = RetrofitClient
                .getInstance()
                .buildRetrofit()
                .getCurrentWeather(latitude, longitude, OPEN_WEATHER_MAP_KEY);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String jsonObj = response.body().toString();
                Log.d("Retrofit", "Success :" + jsonObj);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Retrofit", "Failure : " + t.getMessage());
            }
        });
    }
}
