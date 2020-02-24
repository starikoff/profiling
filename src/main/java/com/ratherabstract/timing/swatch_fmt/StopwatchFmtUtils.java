package com.ratherabstract.timing.swatch_fmt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class StopwatchFmtUtils {

	public static final NumberFormat NF1 = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	public static final NumberFormat NF2 = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	public static final NumberFormat NF3 = new DecimalFormat("#.###", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	public static final NumberFormat NF6 = new DecimalFormat("#.######", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

	public static String formatAsMs(long ns) {
		double ms = msd(ns);
		if (ms >= 2) return Long.toString((long) ms);
		if (ms >= 0.2) return NF1.format(ms);
		if (ms >= 0.02) return NF2.format(ms);
		if (ms >= 0.002) return NF3.format(ms);
		return NF6.format(ms);
	}

	public static long ms(long ns) {
		return ns / 1_000_000;
	}

	public static double msd(long ns) {
		return ns / 1_000_000.;
	}
}
