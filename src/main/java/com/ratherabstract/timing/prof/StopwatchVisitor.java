package com.ratherabstract.timing.prof;

import com.ratherabstract.timing.swatch.StopwatchNode;

public interface StopwatchVisitor {

	void enteredRoot(StopwatchNode root);

	void enteredNode(StopwatchNode node);

	void exitedNode(StopwatchNode node);

	void exitedRoot(StopwatchNode root);

}
