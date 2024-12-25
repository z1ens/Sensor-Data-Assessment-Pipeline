package sensorassessment;

import org.apache.flink.api.common.functions.MapFunction;

public class SensorParser implements MapFunction<String, SensorData> {
    /**
     * @param value, the data read from source file
     * @return a SensorData object
     * @throws Exception when the parser does not work properly
     */
    @Override
    public SensorData map(String value) throws Exception {
        try {
            // Split the string by space
            String[] fields = value.split("\\s+");

            // Ensure at least 8 fields are present
            if (fields.length < 8) {
                System.err.println("Incomplete record skipped: " + value);
                return null; // skip incomplete record
            }
            // Parsing fields
            String date = fields[0];
            String time = fields[1];
            int epoch = Integer.parseInt(fields[2]);       // Epoch
            int moteId = Integer.parseInt(fields[3]);      // Mote ID
            double temperature = Double.parseDouble(fields[4]);
            double humidity = Double.parseDouble(fields[5]);
            double light = Double.parseDouble(fields[6]);
            double voltage = Double.parseDouble(fields[7]);

            return new SensorData(date, time, epoch, moteId, temperature, humidity, light, voltage);
        } catch (Exception e) {
            // Log the problematic line and return null to skip it
            System.err.println("Error parsing record: " + value + " | Error: " + e.getMessage());
            return null;
        }
    }
}
