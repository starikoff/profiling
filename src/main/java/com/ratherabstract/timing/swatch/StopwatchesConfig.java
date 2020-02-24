package com.ratherabstract.timing.swatch;

import com.ratherabstract.timing.prof.StopwatchVisitor;

import java.util.function.Predicate;

public class StopwatchesConfig {

	public static int maxDepth = 100;

	public static int maxChildren = 100;

	public static StopwatchVisitor defaultVisitor = new StopwatchVisitor() {
		@Override
		public void enteredRoot(StopwatchNode root) {
			throw new IllegalStateException("Default stopwatch visitor is not configured");
		}

		@Override
		public void enteredNode(StopwatchNode node) {

		}

		@Override
		public void exitedNode(StopwatchNode node) {

		}

		@Override
		public void exitedRoot(StopwatchNode root) {

		}
	};

	public static Predicate defaultEnabledPredicate = (_anything) -> false;
}
