package com.ratherabstract.timing.swatch;

public class RealStopwatch implements IStopwatch {

	public StopwatchNode root;

	private StopwatchNode current;

	private int depth = 0;

	RealStopwatch() {
		root = new StopwatchNode(null, "ROOT");
		root.doStart(1, System.nanoTime());
		current = root;
	}

	@Override
	public void start(String tag, int iterationsCount) {
		if (depth >= StopwatchesConfig.maxDepth) {
			assert false : "Maximal depth reached: likely the number of stop() calls is less than the number of start() calls.";
			return; // we will output nonsense, but it will be possible to understand what's wrong from the output
		}
		if (current.children.size() >= StopwatchesConfig.maxChildren) {
			assert false : "Maximal children count reached";
			return; // we will output nonsense, but it will be possible to understand what's wrong from the output
		}
		depth++;
		current = current.start(tag, iterationsCount);
	}

	@Override
	public void stop() {
		if (current != root) {
			depth--;
			current = current.stop();
		}
	}

	public void complete() {
		root.stop();
	}

}
