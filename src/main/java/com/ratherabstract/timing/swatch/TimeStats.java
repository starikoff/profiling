package com.ratherabstract.timing.swatch;

public class TimeStats {

	private static final int SIZE = 12;

	public long[] counts = new long[SIZE];
	public long[] sums = new long[SIZE];

	private boolean untrusted = false;

	public void ingest(long valueNS) {
		if (valueNS < 1000) {
			counts[2]++;
			sums[2] += valueNS;
			if (!untrusted && valueNS < 100) {
				untrusted = true;
			}
		} else {
			slowestPath(valueNS);
		}
	}

	private void slowestPath(long valueNS) {
		long upper = 10_000; // 1-10 us
		for (int i = 3; i < SIZE - 1; i++) {
			if (valueNS < upper) {
				counts[i]++;
				sums[i] += valueNS;
				return;
			}
			upper *= 10;
		}
		counts[SIZE - 1]++;
		sums[SIZE - 1] += valueNS;
	}

	public void ingest(long valueNS, long count) {
		long oneRunNS = valueNS / count;
		long upper = 10; // 1-10 ns
		for (int i = 0; i < SIZE - 1; i++) {
			if (oneRunNS < upper) {
				counts[i] += count;
				sums[i] += valueNS;
				return;
			}
			upper *= 10;
		}
		counts[SIZE - 1] += count;
		sums[SIZE - 1] += valueNS;
	}

	public boolean isUntrusted() {
		return untrusted;
	}
}