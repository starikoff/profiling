package com.ratherabstract.timing.prof_fmt;

import com.ratherabstract.timing.swatch.StopwatchNode;

public abstract class StopwatchFormatter /*implements StopwatchVisitor*/ {

	private StringBuilder sb;

	private String result;

	protected abstract void enteredRoot(StopwatchNode root, StringBuilder sb);

	protected abstract void enteredNode(StopwatchNode node, StringBuilder sb);

	protected abstract void exitedNode(StopwatchNode node, StringBuilder sb);

	protected abstract void exitedRoot(StopwatchNode root, StringBuilder sb);

	public final void enteredRoot(StopwatchNode root) {
		sb = new StringBuilder();
		result = null;
		enteredRoot(root, sb);
	}

	public final void enteredNode(StopwatchNode node) {
		enteredNode(node, sb);
	}

	public final void exitedNode(StopwatchNode node) {
		exitedNode(node, sb);
	}

	public final void exitedRoot(StopwatchNode root) {
		exitedRoot(root, sb);
		result = sb.toString();
		sb = null;
	}

	public final String get() {
		return result;
	}

}
