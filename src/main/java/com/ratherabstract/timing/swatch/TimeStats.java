package com.ratherabstract.timing.swatch;

public class TimeStats {

	public long[] counts = new long[16];
	public long[] sums = new long[16];

	public void ingest(long valueNS) {
		long upper = 1; // 1 ns
		for (int i = 0; i < counts.length - 1; i++) {
			if (valueNS < upper) {
				counts[i]++;
				sums[i] += valueNS;
				return;
			}
			upper *= 10;
		}
		counts[counts.length - 1]++;
		sums[counts.length - 1] += valueNS;
	}

	public int minIdx() {
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > 0) {
				return i;
			}
		}
		return -1;
	}

	public int maxIdx() {
		for (int i = counts.length - 1; i >= 0; i--) {
			if (counts[i] > 0) {
				return i;
			}
		}
		return -1;
	}

}