package com.ratherabstract.timing.prof_fmt.xml1;

import com.ratherabstract.timing.prof_fmt.StopwatchFormatter;
import com.ratherabstract.timing.swatch.StopwatchNode;
import com.ratherabstract.timing.swatch_fmt.StopwatchXmlFmtUtils;

public class StopwatchXmlFormatter extends StopwatchFormatter {

	private int level = 0;

	private long rootNS;

	@Override
	public void enteredRoot(StopwatchNode root, StringBuilder sb) {
		rootNS = root.durationInnerNS;
	}

	@Override
	public void enteredNode(StopwatchNode node, StringBuilder sb) {
		StopwatchXmlFmtUtils.startXML(" ".repeat(level), node, sb, rootNS);
		level++;
	}

	@Override
	public void exitedNode(StopwatchNode node, StringBuilder sb) {
		level--;
		StopwatchXmlFmtUtils.endXML(" ".repeat(level), node, sb);
	}

	@Override
	public void exitedRoot(StopwatchNode root, StringBuilder sb) {
	}

}
