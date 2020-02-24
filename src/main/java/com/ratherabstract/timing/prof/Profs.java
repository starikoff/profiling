package com.ratherabstract.timing.prof;

import com.ratherabstract.timing.swatch.IStopwatch;
import com.ratherabstract.timing.swatch.Stopwatches;

import java.util.function.Supplier;

/**
 * Wall clock profiling utility methods. Allow to save some wording for sections that can throw exceptions.
 */
public class Profs {

	public static void profiling(String tag, Runnable code) {
		IStopwatch sw = Stopwatches.get();
		sw.start(tag);
		try {
			code.run();
		} finally {
			sw.stop();
		}
	}

	public static <X extends Exception> void profilingX(String tag, RunnableX<X> code) throws X {
		IStopwatch sw = Stopwatches.get();
		sw.start(tag);
		try {
			code.run();
		} finally {
			sw.stop();
		}
	}

	public static <T> T profiling(String tag, Supplier<T> code) {
		IStopwatch sw = Stopwatches.get();
		sw.start(tag);
		try {
			return code.get();
		} finally {
			sw.stop();
		}
	}

	public static <T, X extends Exception> T profilingX(String tag, SupplierX<T, X> code) throws X {
		IStopwatch sw = Stopwatches.get();
		sw.start(tag);
		try {
			return code.get();
		} finally {
			sw.stop();
		}
	}

	public interface RunnableX<X extends Exception> {
		void run() throws X;
	}

	public interface SupplierX<T, X extends Exception> {
		T get() throws X;
	}
}
