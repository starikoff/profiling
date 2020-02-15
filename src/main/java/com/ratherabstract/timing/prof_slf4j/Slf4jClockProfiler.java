package com.ratherabstract.timing.prof_slf4j;

import com.ratherabstract.timing.prof_fmt.FormatAndPrintVisitor;
import com.ratherabstract.timing.prof_fmt.StopwatchFormatter;
import org.slf4j.Logger;
import com.ratherabstract.timing.prof.ClockProfiler;

public class Slf4jClockProfiler extends ClockProfiler<Void> {

	Slf4jClockProfiler(String name, Logger logger, StopwatchFormatter formatter) {
		super(
			name,
			(_null) -> logger.isTraceEnabled(),
			new FormatAndPrintVisitor(
				formatter,
				(xmlStr) -> logger.trace(System.lineSeparator() + xmlStr)
			)
		);
	}

	public void activate(String tag) {
		super.activate(tag, null);
	}
}
