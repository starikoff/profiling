package com.ratherabstract.timing.swatch_fmt;

import com.ratherabstract.timing.swatch.IStopwatch;
import com.ratherabstract.timing.swatch.RealStopwatch;
import com.ratherabstract.timing.swatch.StopwatchNode;

/**
 * Fields:
 * <ul>
 * <li>t_ms -- total duration spent in this section, in ms
 * <li>explained_ms -- the sum of durations of all child sections (tells you which for which part of the whole section you
 *     have detailization), in ms
 * <li>inv -- invocations count
 * <li>avg_ms -- average time of 1 invocation, in ms
 * <li>of_total -- percentage of this section in the whole running time
 * <li>of_parent -- percentage of this section in the parent running time
 * <li>warning -- if the profiler was invoked too many times for too short iterations
 * <li>real_inv -- if some of the invocation counts were "imported" ({@link IStopwatch#start(String, int)} had
 *     <code>iterations &gt; 1</code>), this will have the real number of invocations
 * <li>overhead_ms -- an estimate of the overhead incurred by this stopwatch
 * <li>time_stats -- distribution of times
 * </ul>
 */
public class StopwatchXmlFmtUtils {

	public static String dumpXML(RealStopwatch watch) {
		StringBuilder sb = new StringBuilder();
		dumpXML("", watch.root, sb, watch.root.durationInnerNS);
		return sb.toString();
	}

	static void dumpXML(String prefix, StopwatchNode node, StringBuilder sb, long rootNs) {
		if (node.invocations > 0) {
			startXML(prefix, node, sb, rootNs);
			if (!node.children.isEmpty()) {
				String childPrefix = prefix + " ";
				for (StopwatchNode child : node.children.values()) {
					dumpXML(childPrefix, child, sb, rootNs);
				}
			}
			endXML(prefix, node, sb);
		}
	}

	public static void startXML(String prefix, StopwatchNode node, StringBuilder sb, long rootNs) {
		long coveredNS = 0;
		for (StopwatchNode child : node.children.values()) {
			coveredNS += child.durationInnerNS;
		}

		String template =
			"%s<timer t_ms=\"%s\" name=\"%s\" explained_ms=\"%s\" inv=\"%d\" avg_ms=\"%s\" of_total=\"%s\" of_parent=\"%s\"";

		// check that start/stop invocations don't contribute too much (more than 10%)
		long costNS = node.durationOuterNS - node.durationInnerNS;
		if (StopwatchFmtUtils.ms(costNS) > 1) {
			if (costNS * 100 > node.durationInnerNS /* 1% */) {
				template += " overhead_ms=\"" + StopwatchFmtUtils.formatAsMs(costNS) + "\"";
				if (costNS * 10 > node.durationInnerNS /* 10% */) {
					template += " warning=\"EXECUTION SKEWED: TOO MANY AND/OR TOO SHORT INVOCATIONS\"";
				}
			}
		}

		long parentNs = (node.parent == null ? 0 : node.parent.durationInnerNS);
		
		if (node.realInvocations != node.invocations) {
			template += " real_inv=\"" + node.realInvocations + "\"";
		}

		String statsStr = TimeStatsFmtUtils.format(node.timeStats);
		if (statsStr != null) {
			template += " time_stats=\"" + statsStr + "\"";
		}

		sb.append(
			String.format(template,
				prefix,
				StopwatchFmtUtils.formatAsMs(node.durationInnerNS),
				sanitize(node.tag),
				StopwatchFmtUtils.formatAsMs(coveredNS),
				node.invocations,
				StopwatchFmtUtils.formatAsMs(node.durationInnerNS / node.invocations),
				rootNs == 0 ? "NaN" : StopwatchFmtUtils.NF2.format(((double) node.durationInnerNS) / rootNs),
				parentNs == 0 ? "NaN" : StopwatchFmtUtils.NF2.format(((double) node.durationInnerNS) / parentNs)
			)
		);
		if (node.children.isEmpty()) {
			sb.append(" />").append(System.lineSeparator());
		} else {
			sb.append(">").append(System.lineSeparator());
		}
	}

	public static void endXML(String prefix, StopwatchNode node, StringBuilder sb) {
		if (!node.children.isEmpty()) {
			sb.append(String.format("%s</timer>%n", prefix));
		}
	}

	private static String sanitize(String tag) {
		return tag.replaceAll("\"", "'");
	}

}
