package com.ratherabstract.timing.prof_fmt.plain1;

import com.ratherabstract.timing.prof_fmt.StopwatchFormatter;
import com.ratherabstract.timing.swatch.StopwatchNode;
import com.ratherabstract.timing.swatch_fmt.StopwatchFmtUtils;
import com.ratherabstract.timing.swatch_fmt.TimeStatsFmtUtils;

public class StopwatchPlainFormatter extends StopwatchFormatter {

	private static final String HEADER =
		String.format(
			"%-64s  %12s   %-12s  %16s   %s%n",
			"[name]",
			"[duration,ms]",
			"[children,ms]",
			"[invocations]",
			"[avg,ms]"
		);

	private int level = 0;

	@Override
	protected void enteredRoot(StopwatchNode root, StringBuilder sb) {
		sb.append(HEADER);
	}

	@Override
	protected void enteredNode(StopwatchNode node, StringBuilder sb) {
		long coveredNS = 0;
		for (StopwatchNode child : node.children.values()) {
			coveredNS += child.durationOuterNS;
		}

		String template = "%s%-64s | %12s | %-12s | %16d | %s |";

		// check that start/stop invocations don't contribute too much (more than 10%)
		long costNS = node.durationOuterNS - node.durationInnerNS;
		if (StopwatchFmtUtils.ms(costNS) > 1) {
			if (costNS * 100 > node.durationInnerNS /* 1% */) {
				template += " [+ overhead: " + StopwatchFmtUtils.formatAsMs(costNS) + " ms";
				if (costNS * 10 > node.durationInnerNS /* 10% */) {
					template += ", WARNING: EXECUTION SKEWED: TOO MANY AND/OR TOO SHORT INVOCATIONS";
				}
				template += "]";
			}
		}

		if (TimeStatsFmtUtils.nonZeroBuckets(node.timeStats) > 1) {
			template += " [time stats: " + TimeStatsFmtUtils.format(node.timeStats) + "]";
		}

		if (node.realInvocations != node.invocations) {
			template += " [real invocations: " + node.realInvocations + "]";
		}

		template += "%n";
		sb.append(
			String.format(template,
				" ".repeat(level),
				node.tag,
				StopwatchFmtUtils.formatAsMs(node.durationInnerNS),
				StopwatchFmtUtils.formatAsMs(coveredNS),
				node.invocations,
				StopwatchFmtUtils.formatAsMs(node.durationInnerNS / node.invocations)
			)
		);

		level++;
	}

	@Override
	protected void exitedNode(StopwatchNode node, StringBuilder sb) {
		level--;
	}

	@Override
	protected void exitedRoot(StopwatchNode root, StringBuilder sb) {

	}
}
