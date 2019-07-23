package songpatechnicalhighschool.motivation.eyefriend.Service;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import songpatechnicalhighschool.motivation.eyefriend.Module.Weather;

public class WeatherService {

    public Weather weatherToText(JsonObject jsonObject) {

        String weatherText;

        JsonArray weatherArr = (JsonArray) jsonObject.get("weather");
        JsonObject weather = (JsonObject) weatherArr.get(0);
        int id = Integer.parseInt(weather.get("id").toString());

        JsonObject main = (JsonObject) jsonObject.get("main");
        String temperatureStr = main.get("temp").toString();
        float temperature = (float) (Float.parseFloat(temperatureStr) - 273.15);

        switch (id / 100) {
            case 2:
                weatherText = getThunder(id);
                break;
            case 3:
                weatherText = getDrizzle(id);
                break;
            case 4:
                weatherText = getRain(id);
                break;
            case 5:
                weatherText = getSnow(id);
                break;
            case 6:
                weatherText = getDisaster(id);
                break;
            case 7:
                weatherText = getDisaster(id);
                break;
            case 8:
                weatherText = getCloudy(id);
                break;
            case 9:
                weatherText = getDisaster2(id);
                break;
            default:
                weatherText = "없음";
        }

        Log.d("WeatherText", id+"");
        Weather weatherVal = new Weather(weatherText, Float.toString(temperature));
        Log.d("WeatherText", weatherVal.getText()+ ", " + weatherVal.getTemperature());
        return weatherVal;
    }

    private String getThunder(int id) {

        String weatherKor;

        switch (id) {
            case 200:
                weatherKor = "가벼운 비를 동반한 천둥구름";
                break;
            case 201:
                weatherKor = "비를 동반한 천둥구름";
                break;
            case 202:
                weatherKor = "폭우를 동반한 천둥구름";
                break;
            case 210:
                weatherKor = "약한 천둥구름";
                break;
            case 211:
                weatherKor = "천둥구름";
                break;
            case 212:
                weatherKor = "강한 천둥구름";
                break;
            case 221:
                weatherKor = "불규칙적 천둥구름";
                break;
            case 230:
                weatherKor = "약한 연무를 동반한 천둥구름";
                break;
            case 231:
                weatherKor = "연무를 동반한 천둥구름";
                break;
            case 232:
                weatherKor = "강한 안개비를 동반한 천둥구름";
                break;
            default:
                weatherKor = "없음";
                break;
        }
        return weatherKor;
    }

    private String getDrizzle(int id) {

        String weatherKor;

        switch (id) {
            case 300:
                weatherKor = "가벼운 안개비";
                break;
            case 301:
                weatherKor = "안개비";
                break;
            case 302:
                weatherKor = "강한 안개비";
                break;
            case 310:
                weatherKor = "가벼운 적은비";
                break;
            case 311:
                weatherKor = "적은비";
                break;
            case 312:
                weatherKor = "강한 적은비";
                break;
            case 313:
                weatherKor = "소나기와 안개비";
                break;
            case 314:
                weatherKor = "강한 소나기와 안개비";
                break;
            case 321:
                weatherKor = "소나기";
                break;
            default:
                weatherKor = "없음";
                break;
        }
        return weatherKor;
    }

    private String getRain(int id) {

        String weatherKor;

        switch (id) {
            case 500:
                weatherKor = "악한 비";
                break;
            case 501:
                weatherKor = "중간 비";
                break;
            case 502:
                weatherKor = "rain 강한 비";
                break;
            case 503:
                weatherKor = "rain 매우 강한 비";
                break;
            case 504:
                weatherKor = "극심한 비";
                break;
            case 511:
                weatherKor = "우박";
                break;
            case 520:
                weatherKor = "약한 소나기 비";
                break;
            case 521:
                weatherKor = "소나기 비";
                break;
            case 522:
                weatherKor = "강한 소나기 비";
                break;
            case 531:
                weatherKor = "불규칙적 소나기 비";
                break;
            default:
                weatherKor = "없음";
                break;
        }
        return weatherKor;
    }

    private String getSnow(int id) {

        String weatherKor;

        switch (id) {
            case 600:
                weatherKor = "가벼운 눈";
                break;
            case 601:
                weatherKor = "눈";
                break;
            case 602:
                weatherKor = "강한 눈";
                break;
            case 611:
                weatherKor = "진눈깨비";
                break;
            case 612:
                weatherKor = "소나기 진눈깨비";
                break;
            case 615:
                weatherKor = "약한 비와 눈";
                break;
            case 616:
                weatherKor = "비와 눈";
                break;
            case 620:
                weatherKor = "약한 소나기 눈";
                break;
            case 621:
                weatherKor = "소나기 눈";
                break;
            case 622:
                weatherKor = "강한 소나기 눈";
                break;
            default:
                weatherKor = "없음";
                break;
        }
        return weatherKor;
    }

    private String getDisaster(int id) {

        String weatherKor;

        switch (id) {
            case 701:
                weatherKor = "대체로 맑고 ";
                break;
            case 711:
                weatherKor = "연기";
                break;
            case 721:
                weatherKor = "대체로 흐리고";
                break;
            case 731:
                weatherKor = "모래 먼지";
                break;
            case 741:
                weatherKor = "안개";
                break;
            case 751:
                weatherKor = "모래";
                break;
            case 761:
                weatherKor = "먼지";
                break;
            case 762:
                weatherKor = "화산재";
                break;
            case 791:
                weatherKor = "돌풍";
                break;
            case 781:
                weatherKor = "토네이도";
                break;

            default:
                weatherKor = "없음";
                break;
        }
        return weatherKor;
    }

    private String getCloudy(int id) {

        String weatherKor;

        switch (id) {
            case 800:
                weatherKor = "구름 한 점 없는 맑은 하늘";
                break;
            case 801:
                weatherKor = "약간의 구름이 낀 하늘";
                break;
            case 802:
                weatherKor = "드문드문 구름이 낀 하늘";
                break;
            case 803:
                weatherKor = "구름이 거의 없는 하늘";
                break;
            case 804:
                weatherKor = "구름으로 뒤덮인 흐린 하늘";
                break;
            default:
                weatherKor = "없음";
                break;
        }
        return weatherKor;
    }

    private String getDisaster2(int id) {

        String weatherKor;

        switch (id) {
            case 900:
                weatherKor = "토네이도";
                break;
            case 901:
                weatherKor = "태풍";
                break;
            case 902:
                weatherKor = "허리케인";
                break;
            case 903:
                weatherKor = "한랭";
                break;
            case 904:
                weatherKor = "고온";
                break;
            case 905:
                weatherKor = "바람부는";
                break;
            case 906:
                weatherKor = "우박";
                break;
            case 951:
                weatherKor = "바람이 거의 없는";
                break;
            case 952:
                weatherKor = "약한 바람";
                break;
            case 953:
                weatherKor = "부드러운 바람";
                break;
            case 954:
                weatherKor = "중간 세기 바람";
                break;
            case 955:
                weatherKor = "신선한 바람";
                break;
            case 956:
                weatherKor = "센 바람";
                break;
            case 957:
                weatherKor = "돌풍에 가까운 센 바람";
                break;
            case 959:
                weatherKor = "돌풍";
                break;
            case 958:
                weatherKor = "심각한 돌풍";
                break;
            case 960:
                weatherKor = "폭풍";
                break;
            case 961:
                weatherKor = "강한 폭풍";
                break;
            case 962:
                weatherKor = "허리케인";
                break;

            default:
                weatherKor = "없음";
                break;
        }
        return weatherKor;
    }
}


