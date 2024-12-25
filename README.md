# Sensor Data Assessment Pipeline

This project implements a data processing pipeline for analyzing sensor data collected from edge devices. It detects anomalies in sensor readings and aggregates the results in defined time windows.

## **Features**

- **Event Time Processing**: Handles out-of-order data using event-time semantics and watermarks.
- **Anomaly Detection**: Identifies anomalies in sensor readings based on configurable thresholds (e.g., temperature and voltage).
- **Window Aggregation**: Groups data by device and aggregates results over 10-second tumbling time windows.

## **Input Data**

The pipeline processes sensor data in the following format:

| Column | Description | Example |
| --- | --- | --- |
| `date` | Date in `yyyy-MM-dd` format | `2004-03-31` |
| `time` | Time in `HH:mm:ss.SSSSSS` format | `03:38:15.757551` |
| `epoch` | Epoch ID for each sensor | `2` |
| `moteId` | Sensor device ID (1-54) | `37` |
| `temperature` | Sensor temperature (°C) | `122.153` |
| `humidity` | Sensor humidity (%) | `-3.91901` |
| `light` | Sensor light intensity (Lux) | `11.04` |
| `voltage` | Sensor voltage (V) | `2.03397` |

## **How It Works**

1. **Input**: Reads sensor data from a text file (`data.txt`).
2. **Parsing**: Converts raw text into structured `SensorData` objects.
3. **Timestamp Assignment**: Assigns event time to each record based on the `date` and `time` fields.
4. **Anomaly Detection**: Filters out normal readings and flags anomalies (e.g., high temperature or fluctuating voltage).
5. **Window Aggregation**: Aggregates anomalies for each device in 10-second tumbling time windows.

## **Running the Pipeline**

### Prerequisites

- Apache Flink (version 1.20 or compatible)
- Java 11 or higher
- Maven

### Steps

1. Clone this repository:

    ```bash
    git clone https://github.com/z1ens/Sensor-Data-Assessment-Pipeline
    cd sensor-assessment
    ```

2. Build the project with Maven:

    ```bash
    mvn clean install
    ```

3. Run the pipeline: (or simply run the `SensorAssesmentJob.java`)

    ```bash
    java -cp target/sensor-assessment-0.1.jar sensorassessment.SensorAssessmentJob
    ```


---

## **Output**

The pipeline outputs aggregated results to the console in the following format:

```jsx
Device 37 reported 1 anomalies in the last window.
Device 58 reported 3 anomalies in the last window.
```

## **Configuration**

- **Window Duration**: Configured in the `SensorAssessmentJob` class:

    ```java
    TumblingEventTimeWindows.of(Time.seconds(10))
    ```

- **Anomaly Detection Thresholds**: Configured in the `SensorDetector` class:

    ```java
    data.getTemperature() > 30.0 || Math.abs(data.getVoltage() - 2.5) > 0.2
    ```


## **Data Validation**

- Invalid or incomplete records (e.g., missing fields) are logged and skipped during processing. For example in the source file `data.txt` , some records does not contain a voltage information, others might only have `date` and `time` and no other sensor information contained.

## **Acknowledgments**

This project is inspired by edge-to-cloud scenarios and demonstrates real-time stream processing using Apache Flink.