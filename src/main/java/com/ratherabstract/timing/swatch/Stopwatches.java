package com.ratherabstract.timing.swatch;

import java.util.Optional;

import static com.ratherabstract.timing.swatch.IStopwatch.NO_WATCH;

public class Stopwatches {

	private static final ThreadLocal<IStopwatch> stopwatchTL = ThreadLocal.withInitial(() -> NO_WATCH);

	public static RealStopwatch install() {
		RealStopwatch result = new RealStopwatch();
		stopwatchTL.set(result);
		return result;
	}

	public static Optional<RealStopwatch> installIfNone() {
		IStopwatch current = get();
		if (current instanceof RealStopwatch) {
			return Optional.empty();
		}
		return Optional.of(install());
	}

	public static RealStopwatch uninstall() {
		IStopwatch swatch = stopwatchTL.get();
		if (swatch instanceof RealStopwatch) {
			RealStopwatch realStopwatch = (RealStopwatch) swatch;
			realStopwatch.complete();
			stopwatchTL.set(NO_WATCH);
			return realStopwatch;
		} else {
			throw new IllegalStateException("no stopwatch installed");
		}
	}

	public static IStopwatch get() {
		return stopwatchTL.get();
	}

}