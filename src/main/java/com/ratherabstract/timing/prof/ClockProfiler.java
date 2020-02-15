package com.ratherabstract.timing.prof;

import com.ratherabstract.timing.swatch.RealStopwatch;
import com.ratherabstract.timing.swatch.StopwatchNode;
import com.ratherabstract.timing.swatch.Stopwatches;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * This is a class for profiling.
 * Allows to get profiles of running times of different sections, like the one below:
 * <pre>
 * {@literal
 * <timer t_ms="20680" name="ROOT" sum_ms="20680" inv="1" avg_ms="20680" of_total="100%" of_parent="100%">
 *  <timer t_ms="1285" name="iterator->chunked list [UVMap]" sum_ms="988" inv="1" avg_ms="1285" of_total="6%" of_parent="6%">
 *   <timer t_ms="988" name="uvSpectraIt.next() [UVMapTest]" sum_ms="0" inv="10000" avg_ms="0.098" of_total="4%" of_parent="76%" />
 *  </timer>
 *  <timer t_ms="292" name="find all wavelengths [UVMap]" sum_ms="0" inv="1" avg_ms="292" of_total="1%" of_parent="1%" />
 *  <timer t_ms="239" name="create UV block [UVMap]" sum_ms="0" inv="1" avg_ms="239" of_total="1%" of_parent="1%" />
 *  <timer t_ms="203" name="add UV spectra [UVMap]" sum_ms="0" inv="1" avg_ms="203" of_total="0%" of_parent="0%" />
 *  <timer t_ms="18660" name="process XICs [UVMap]" sum_ms="12694" inv="1" avg_ms="18660" of_total="90%" of_parent="90%">
 *   <timer t_ms="12694" name="swapper: write [TicketOwner]" sum_ms="0" inv="857" avg_ms="14" of_total="61%" of_parent="68%" />
 *  </timer>
 * </timer>
 * }
 * </pre>
 *
 * For that, in your class that has methods that should be the top level of measurements, write e.g.:
 * <pre>
 * {@literal
 *     package pkg.of.interest;
 *
 *     public class ClassIAmInterestedIn {
 *         private static final Slf4jClockProfiler prof = Slf4jClockProfilers.get(ClassIAmInterestedIn.class);
 *         ...
 *
 *         public void methodIAmInterestedIn(List<Step> steps) {
 *             prof.activate("methodIAmInterestedIn");
 *
 *             prof.start("stage A");
 *             doStageA();
 *             prof.stop();
 *
 *             prof.start("stage B");
 *             for (Step step : steps) {
 *                 prof.start("prepare");
 *                 prepare();
 *                 prof.stop();
 *
 *                 prof.start("process");
 *                 process(step);
 *                 prof.stop();
 *             }
 *             prof.stop();
 *
 *             ...
 *
 *             prof.deactivate();
 *         }
 *     }
 * }
 * </pre>
 *
 * And, additionally, enable TRACE level logging for a logger "TIMING.pkg.of.interest.ClassIAmInterestedIn" (it's enough to
 * enable TRACE level for "TIMING"). Then everything recorded inside
 * <code>ClassIAmInterestedIn.methodIAmInterestedIn(...)</code>, as well as all submethods, will be logged
 * at the TRACE level at the last <code>prof.deactivate()</code> call.<br>
 * <br>
 *
 * <code>prof.activate()/prof.deactivate()</code> can be nested, in this case only the outer pair that has
 * TRACE logging enabled will have effect.<br>
 * <br>
 *
 * This is sub-nanosecond when the profiler is not enabled via the log configuration; when it is enabled, think
 * of each <code>start("...")/stop()</code> pair as ~100ns, so don't add them in places that can iterate "millions" of times.
 * The profile will contain a warning if it estimates there were too many/too short iterations it was called for.<br>
 * <br>
 *
 * It dumps XML because XML can be operated as a tree structure in text editors and IDEs (collapse all then uncollapse level
 * by level) and because it allows to have 1 line per all attibutes of 1 timer (name, total time, number of invocations
 * etc).<br>
 * <br>
 *
 * Using these statements doesn't make the code pretty. Maybe have them where they don't obfuscate the code (e.g.
 * in the swapper, to know when it's swapping, and you don't look in that code often). Maybe remove them after some time.
 * Maybe tolerate them because after some time you'll want to understand performance of some part on some server and it
 * might be the only way there.
 */
public class ClockProfiler<T> {

	private String shortName;

	private Predicate<T> enabledCheck;

	private StopwatchVisitor processor;

	private ThreadLocal<StopwatchInstallation> installedStopwatchTL =
		ThreadLocal.withInitial(() -> new StopwatchInstallation(null));


	protected ClockProfiler(String name, Predicate<T> enabledCheck, StopwatchVisitor processor) {
		this.shortName = shorten(name);
		this.enabledCheck = enabledCheck;
		this.processor = processor;
	}

	/**
	 * If profiling has already been started down the call stack, this is equivalent to {@link #start(String)}.<br>
	 * If it hasn't been started, it will be started here IF {@link #enabledCheck} will say "true".
	 * Then this will create 2 stopwatch nodes: the "ROOT" stopwatch, and the "#tag" stopwatch inside.<br>
	 * <br>
	 * A good place for calling this is an "entry point": activity start, request handler, API public method etc.
	 */
	public void activate(String tag, T obj) {
		if (enabledCheck.test(obj)) {
			Optional<RealStopwatch> realSwatchOpt = Stopwatches.installIfNone();
			//noinspection OptionalIsPresent
			if (realSwatchOpt.isPresent()) {
				installedStopwatchTL.set(new StopwatchInstallation(realSwatchOpt.get()));
			}
		}
		installedStopwatchTL.get().activations++;
		start(tag);
	}

	public void deactivate() {
		stop();
		StopwatchInstallation installation = installedStopwatchTL.get();
		if (--installation.activations == 0) {
			RealStopwatch swatch = installation.stopwatch;
			if (swatch != null) {
				Stopwatches.uninstall();
				process(swatch, processor);
			}
		}
	}

	private static void process(RealStopwatch swatch, StopwatchVisitor processor) {
		StopwatchNode root = swatch.root;

		processor.enteredRoot(root);
		process(root, processor);
		processor.exitedRoot(root);
	}

	private static void process(StopwatchNode node, StopwatchVisitor processor) {
		if (node.invocations > 0) {
			processor.enteredNode(node);
			for (StopwatchNode value : node.children.values()) {
				process(value, processor);
			}
			processor.exitedNode(node);
		}
	}

	/**
	 * You call this before a loop for example and you are interested in the loop iterations count --
	 * pass the loop iterations count as the second parameter. This could be achieved by calling {@link #start(String)}
	 * inside the loop, but then you pay the start/stop overhead for each iteration.
	 */
	public void start(String tag, int iterations) {
		Stopwatches.get().start(tag + " [" + shortName + "]", iterations);
	}

	public void start(String tag) {
		start(tag, 1);
	}

	public void stop() {
		Stopwatches.get().stop();
	}

	private static String shorten(String name) {
		String result = name;
		int idx = result.lastIndexOf('.');
		if (idx >= 0) {
			result = result.substring(idx + 1);
		}
		return result;
	}

	private static class StopwatchInstallation {

		final RealStopwatch stopwatch;

		int activations;

		StopwatchInstallation(RealStopwatch stopwatch) {
			this.stopwatch = stopwatch;
		}
	}
}
