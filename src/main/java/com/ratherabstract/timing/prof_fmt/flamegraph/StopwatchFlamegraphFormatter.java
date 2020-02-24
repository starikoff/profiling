package com.ratherabstract.timing.prof_fmt.flamegraph;

import com.ratherabstract.timing.prof_fmt.StopwatchFormatter;
import com.ratherabstract.timing.swatch.StopwatchNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StopwatchFlamegraphFormatter extends StopwatchFormatter {

	@Override
	protected void enteredRoot(StopwatchNode root, StringBuilder sb) {

	}

	private List<String> path = new ArrayList<>();

	@Override
	protected void enteredNode(StopwatchNode node, StringBuilder sb) {
		path.add(node.tag);

		long ownOuterNS = node.durationOuterNS;
		long coveredNS = 0;
		for (StopwatchNode child : node.children.values()) {
			coveredNS += child.durationOuterNS;
		}

		sb.append(String.join(";", path)).append(" ").append(ownOuterNS - coveredNS).append('\n');
	}

	@Override
	protected void exitedNode(StopwatchNode node, StringBuilder sb) {
		path.remove(path.size() - 1);
	}

	@Override
	protected void exitedRoot(StopwatchNode root, StringBuilder sb) {
		try {
			Files.writeString(Path.of("some-" + System.currentTimeMillis() + ".txt"), sb.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
