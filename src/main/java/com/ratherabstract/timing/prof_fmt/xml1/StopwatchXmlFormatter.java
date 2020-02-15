package com.ratherabstract.timing.prof_fmt.xml1;

import com.ratherabstract.timing.swatch.StopwatchNode;
import com.ratherabstract.timing.swatch_fmt.StopwatchFmtUtils;
import com.ratherabstract.timing.swatch_fmt.StopwatchXmlFmtUtils;
import com.ratherabstract.timing.prof_fmt.StopwatchFormatter;

public class StopwatchXmlFormatter extends StopwatchFormatter {

	private int level = 0;

	private long rootNS;

	@Override
	public void enteredRoot(StopwatchNode root, StringBuilder sb) {
		rootNS = root.durationInnerNS;
	}

	@Override
	public void enteredNode(StopwatchNode node, StringBuilder sb) {
		StopwatchXmlFmtUtils.startXML(StopwatchFmtUtils.prefix(" ", level), node, sb, rootNS);
		level++;
	}

	@Override
	public void exitedNode(StopwatchNode node, StringBuilder sb) {
		level--;
		StopwatchXmlFmtUtils.endXML(StopwatchFmtUtils.prefix(" ", level), node, sb);
	}

	@Override
	public void exitedRoot(StopwatchNode root, StringBuilder sb) {
	}

}
