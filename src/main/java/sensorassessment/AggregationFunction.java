package sensorassessment;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AggregationFunction extends ProcessWindowFunction<SensorData, String, Integer, TimeWindow> {

    /**
     * On the Cloud side, use the customized ProcessWindowFunction to aggregate and analyze the abnormal data.
     * For example, count the number of abnormalities in each device in 10 seconds.
     * @param key the key for the device
     * @param context
     * @param iterable
     * @param collector
     * @throws Exception
     */
    @Override
    public void process(Integer key, Context context, Iterable<SensorData> iterable, Collector<String> collector) throws Exception {
        int anomalyCount = 0;

        // Count anomalies in the window
        for (SensorData data : iterable) {
            anomalyCount++;
        }

        // Emit the aggregated result
        collector.collect("Device " + key + " reported " + anomalyCount + " anomalies in the last window.");
    }
}
