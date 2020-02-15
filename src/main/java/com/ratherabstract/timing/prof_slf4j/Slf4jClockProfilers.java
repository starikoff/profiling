package com.ratherabstract.timing.prof_slf4j;

import org.slf4j.LoggerFactory;

public class Slf4jClockProfilers {

	public static Slf4jClockProfiler get(String name) {
		return new Slf4jClockProfiler(name, LoggerFactory.getLogger(name), Slf4jClockProfilersConfig.formatter);
	}

	public static Slf4jClockProfiler get(Class<?> cls) {
		return new Slf4jClockProfiler(cls.getName(), LoggerFactory.getLogger(cls), Slf4jClockProfilersConfig.formatter);
	}

}
