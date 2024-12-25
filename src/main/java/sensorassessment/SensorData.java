package sensorassessment;

public class SensorData {
    /**
     * The Date of the data, format: yyyy-mm-dd.
     */
    private String date;

    /**
     * The time of the data, format: hh:mm:ss.xxx
     */
    private String time;

    /**
     * Epoch is a monotonically increasing sequence number from each mote.
     */
    private int epoch;

    /**
     *  Sensor ID
     */
    private int moteId;

    /**
     * Temperature is in degrees Celsius.
     */
    private double temperature;

    /**
     * Humidity is temperature corrected relative humidity, ranging from 0-100%
     */
    private double humidity;

    /**
     * Light is in Lux, a value of 1 Lux corresponds to moonlight,
     * 400 Lux to a bright office, and 100,000 Lux to full sunlight.
     */
    private double light;

    /**
     * Voltage is expressed in volts, ranging from 2-3;
     * the batteries, in this case, were lithium-ion cells that maintain a fairly constant voltage over their lifetime;
     * note that variations in voltage are highly correlated with temperature.
     */
    private double voltage;

    private boolean alert;

    private long timestamp;

    public SensorData(String date, String time, int epoch, int moteId, double temperature, double humidity, double light, double voltage) {
        this.date = date;
        this.time = time;
        this.epoch = epoch;
        this.moteId = moteId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.voltage = voltage;
        this.alert = false;
    }

    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getEpoch() {
        return epoch;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }

    public int getMoteId() {
        return moteId;
    }

    public void setMoteId(int moteId) {
        this.moteId = moteId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", epoch=" + epoch +
                ", moteId=" + moteId +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", light=" + light +
                ", voltage=" + voltage +
                '}';
    }
}
