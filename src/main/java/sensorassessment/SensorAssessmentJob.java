/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sensorassessment;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * Skeleton code for the datastream walkthrough
 */
public class SensorAssessmentJob {
	public static void main(String[] args) throws Exception {
		// Create a Flink execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// Read the sensor data file
		DataStream<String> inputData = env.readTextFile("src/main/resources/data.txt");

		DataStream<SensorData> parsedData = inputData
				.map(new SensorParser())
				.filter(Objects::nonNull); // Filter out null records

		// Assign time stamps and water level lines
		DataStream<SensorData> timestampedData = parsedData
				.flatMap((SensorData event, Collector<SensorData> out) -> {
					try {
						// Create a time and date formatter.
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
						LocalDateTime localDateTime = getLocalDateTime(event, formatter);
						long timestamp = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
						event.setTimestamp(timestamp);

						// Collect the valid data
						out.collect(event);

					} catch (Exception e) {
						System.err.println("Invalid date or time format, skipping: "
								+ event.getDate() + " " + event.getTime());
					}
				})
				.returns(SensorData.class)
				.assignTimestampsAndWatermarks(
						WatermarkStrategy.<SensorData>forMonotonousTimestamps()
								.withTimestampAssigner((event, timestamp) -> event.getTimestamp())
				);

		// Edge: Detect anomalies and flag them
		DataStream<SensorData> flaggedData = timestampedData
				.filter(new SensorDetector()) // Detect anomalies
				.map(data -> {
					data.setAlert(true); // Flag as abnormal
					return data;
				});

		// Cloud: Aggregate flagged data
		DataStream<String> aggregatedData = flaggedData
				.keyBy(SensorData::getMoteId) // Group by sensor ID
				.window(TumblingEventTimeWindows.of(Time.seconds(10))) // 10-second window
				.process(new AggregationFunction()); // Aggregate anomalies

		// Print aggregated results
		aggregatedData.print();
		env.execute("Sensor Data Processing Pipeline");
	}

	private static LocalDateTime getLocalDateTime(SensorData event, DateTimeFormatter formatter) {
		String dateTimeString = event.getDate() + " " + event.getTime();

		// If the fraction part is less than 6, fill the fraction part
		int dotIndex = dateTimeString.lastIndexOf('.');
		if (dotIndex != -1) {
			int fractionLength = dateTimeString.length() - dotIndex - 1;
			if (fractionLength < 6) {
				StringBuilder sb = new StringBuilder(dateTimeString);
				for (int i = 0; i < 6 - fractionLength; i++) {
					sb.append('0');
				}
				dateTimeString = sb.toString();
			}
		} else {
			dateTimeString += ".000000"; // fill the blank part
		}

		// Analyse the LocalDateTime
        return LocalDateTime.parse(dateTimeString, formatter);
	}
}
