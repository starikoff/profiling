package com.ratherabstract.timing.swatch;

public interface IStopwatch {

	void start(String tag, int iterationsCount);

	void stop();

	default void start(String tag) {
		start(tag, 1);
	}

	IStopwatch NO_WATCH = new IStopwatch() {
		@Override
		public void start(String tag, int iterationsCount) {
		}

		@Override
		public void stop() {
		}
	};

}
