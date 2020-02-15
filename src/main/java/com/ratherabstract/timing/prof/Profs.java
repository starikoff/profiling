package com.ratherabstract.timing.prof;

import java.util.function.Supplier;

/**
 * Wall clock profiling utility methods. Try to not use them.
 */
public class Profs {

	public static void profiling(ClockProfiler<?> timer, String tag, Runnable code) {
		timer.start(tag);
		try {
			code.run();
		} finally {
			timer.stop();
		}
	}

	public static <X extends Exception> void profilingX(ClockProfiler<?> timer, String tag, RunnableX<X> code) throws X {
		timer.start(tag);
		try {
			code.run();
		} finally {
			timer.stop();
		}
	}

	public static <T> T profiling(ClockProfiler<?> timer, String tag, Supplier<T> code) {
		timer.start(tag);
		try {
			return code.get();
		} finally {
			timer.stop();
		}
	}

	public static <T, X extends Exception> T profilingX(ClockProfiler<?> timer, String tag, SupplierX<T, X> code) throws X {
		timer.start(tag);
		try {
			return code.get();
		} finally {
			timer.stop();
		}
	}

	public interface RunnableX<X extends Exception> {
		void run() throws X;
	}

	public interface SupplierX<T, X extends Exception> {
		T get() throws X;
	}
}
