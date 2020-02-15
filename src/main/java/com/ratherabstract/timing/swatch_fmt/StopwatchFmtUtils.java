package com.ratherabstract.timing.swatch_fmt;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;

public class StopwatchFmtUtils {

	public static final NumberFormat NF1 = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	public static final NumberFormat NF2 = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	public static final NumberFormat NF3 = new DecimalFormat("#.###", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	public static final NumberFormat NF6 = new DecimalFormat("#.######", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

	public static String formatAsMs(long ns) {
		double ms = msd(ns);
		if (ms > 2) {
			return Integer.toString((int) ms);
		}
		if (ms >= 1) {
			return NF1.format(ms);
		}
		return NF3.format(ms);
	}

	public static long ms(long ns) {
		return ns / 1_000_000;
	}

	public static double msd(long ns) {
		return ns / 1_000_000.;
	}

	public static String prefix(String s, int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append(s);
		}
		return sb.toString();
	}

	public static String join(Iterable<String> lines, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = lines.iterator(); it.hasNext();) {
			String s = it.next();
			sb.append(s);
			if (it.hasNext()) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}
}
