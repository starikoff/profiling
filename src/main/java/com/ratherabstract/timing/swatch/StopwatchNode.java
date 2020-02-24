package com.ratherabstract.timing.swatch;

import java.util.LinkedHashMap;
import java.util.Map;

public class StopwatchNode {

	public final StopwatchNode parent;

	public final Map<String, StopwatchNode> children = new LinkedHashMap<>();

	public final String tag;

	private long startedOuterNS;

	private long startedInnerNS;

	public long durationOuterNS;

	public long durationInnerNS;

	public long invocations;

	public long realInvocations;

	public TimeStats timeStats = new TimeStats();

	StopwatchNode(StopwatchNode parent, String tag) {
		this.parent = parent;
		this.tag = tag;
	}

	void doStart(int iterations, long startCalledNS) {
		this.invocations += iterations;
		this.realInvocations++;
		startedInnerNS = System.nanoTime();
		startedOuterNS = startCalledNS;
	}

	StopwatchNode start(String childTag, int iterations) {
		long started = System.nanoTime();
		StopwatchNode child = children.get(childTag);
		if (child == null) {
			child = new StopwatchNode(this, childTag);
			children.put(childTag, child);
		}
		child.doStart(iterations, started);
		return child;
	}

	StopwatchNode stop() {
		long stoppedNS = System.nanoTime();
		long deltaInnerNS = stoppedNS - startedInnerNS;
		long deltaOuterNS = stoppedNS - startedOuterNS;
		this.durationInnerNS += deltaInnerNS;
		this.durationOuterNS += deltaOuterNS;
		startedInnerNS = 0;
		startedOuterNS = 0;
		timeStats.ingest(deltaInnerNS);
		return parent;
	}

}
