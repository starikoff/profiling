package com.ratherabstract.timing.swatch_fmt;

import com.ratherabstract.timing.swatch.TimeStats;

import java.util.ArrayList;
import java.util.List;

public class TimeStatsFmtUtils {

	public static String format(TimeStats stats) {
		int minIdx = stats.minIdx();
		int maxIdx = stats.maxIdx();
		if (minIdx < 0 || maxIdx < 0 || minIdx == maxIdx) {
			return null;
		}
		List<String> strings = new ArrayList<>();
		for (int i = minIdx; i <= maxIdx; i++) {
			if (stats.counts[i] > 0) {
				strings.add(String.format("n=%d,S=%sms", stats.counts[i], StopwatchFmtUtils.formatAsMs(stats.sums[i])));
			} else {
				strings.add("");
			}
			strings.add(String.format("[10^(%d)ms]", i - 6));
		}
		return StopwatchFmtUtils.join(strings, " ");
	}

}
