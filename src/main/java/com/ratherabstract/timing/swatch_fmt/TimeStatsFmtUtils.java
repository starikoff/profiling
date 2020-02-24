package com.ratherabstract.timing.swatch_fmt;

import com.ratherabstract.timing.swatch.TimeStats;

public class TimeStatsFmtUtils {

	/**
	 * delimiter within one class of times ("nanoseconds", "milliseconds" etc)
 	 */
	private static final String d = ":";

	/**
	 * delimiter across classes of times ("nanoseconds", "milliseconds" etc)
	 */
	public static final String D = "|";

	public static String format(TimeStats stats) {
		long[] c = stats.counts;
		long[] s = stats.sums;

		if (nonZeroBuckets(stats) == 0) {
			return null;
		}

		if (stats.isUntrusted()) {
			long count0 = c[0] + c[1] + c[2];
			long sum0 = s[0] + s[1] + s[2];
			return "(#) [" + fmtC(count0) +
				D + fmtC(c[3]) + d + fmtC(c[4]) + d + fmtC(c[5]) +
				D + fmtC(c[6]) + d + fmtC(c[7]) + d + fmtC(c[8]) +
				D + fmtC(c[9]) + d + fmtC(c[10]) + d + fmtC(c[11]) +
				"] (t,ms) [" + StopwatchFmtUtils.formatAsMs(sum0) +
				D + fmtS(c, s, 3) + d + fmtS(c, s, 4) + d + fmtS(c, s, 5) +
				D + fmtS(c, s, 6) + d + fmtS(c, s, 7) + d + fmtS(c, s, 8) +
				D + fmtS(c, s, 9) + d + fmtS(c, s, 10) + d + fmtS(c, s, 11) + "]";
		}
		return "(#) [" + fmtC(c[0]) + d + fmtC(c[1]) + d + fmtC(c[2]) +
			D + fmtC(c[3]) + d + fmtC(c[4]) + d + fmtC(c[5]) +
			D + fmtC(c[6]) + d + fmtC(c[7]) + d + fmtC(c[8]) +
			D + fmtC(c[9]) + d + fmtC(c[10]) + d + fmtC(c[11]) +
			"] (t,ms) [" + fmtS(c, s, 0) + d + fmtS(c, s, 1) + d + fmtS(c, s, 2) +
			D + fmtS(c, s, 3) + d + fmtS(c, s, 4) + d + fmtS(c, s, 5) +
			D + fmtS(c, s, 6) + d + fmtS(c, s, 7) + d + fmtS(c, s, 8) +
			D + fmtS(c, s, 9) + d + fmtS(c, s, 10) + d + fmtS(c, s, 11) + "]";
	}

	public static int nonZeroBuckets(TimeStats stats) {
		long[] c = stats.counts;
		int nonZero = 0;
		for (int i = 0; i < stats.counts.length; i++) {
			if (c[i] > 0) {
				nonZero++;
			}
		}
		return nonZero;
	}

	private static String fmtC(long c) {
		if (c == 0) return "-";
		return "" + c;
	}

	private static String fmtS(long[] c, long[] s, int i) {
		if (c[i] == 0) return "-";
		return StopwatchFmtUtils.formatAsMs(s[i]);
	}
}
