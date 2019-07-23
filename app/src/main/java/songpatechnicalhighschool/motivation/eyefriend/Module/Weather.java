package songpatechnicalhighschool.motivation.eyefriend.Module;

public class Weather {

    private String text;
    private String temperature;

    public Weather() {
    }

    public Weather(String text, String temperature) {
        this.text = text;
        this.temperature = temperature;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemperature() {
        temperature = temperature.substring(0,2);
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
