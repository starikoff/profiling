package com.ratherabstract.timing.swatch;

import com.ratherabstract.timing.prof.StopwatchVisitor;

import java.util.function.Predicate;

import static com.ratherabstract.timing.swatch.IStopwatch.NO_WATCH;

public class Stopwatches {

	private static final ThreadLocal<IStopwatch> stopwatchTL = ThreadLocal.withInitial(() -> NO_WATCH);

	public static RealStopwatch install() {
		RealStopwatch result = new RealStopwatch();
		stopwatchTL.set(result);
		return result;
	}

	public static class InstalledStopwatch {
		public final RealStopwatch stopwatch;

		public final boolean isNew;

		public InstalledStopwatch(RealStopwatch stopwatch, boolean isNew) {
			this.stopwatch = stopwatch;
			this.isNew = isNew;
		}
	}

	public static InstalledStopwatch installIfNone() {
		IStopwatch current = get();
		if (current instanceof RealStopwatch) {
			return new InstalledStopwatch((RealStopwatch) current, false);
		}
		return new InstalledStopwatch(install(), true);
	}

	public static RealStopwatch uninstall() {
		IStopwatch swatch = stopwatchTL.get();
		if (swatch instanceof RealStopwatch) {
			RealStopwatch realStopwatch = (RealStopwatch) swatch;
			realStopwatch.complete();
			stopwatchTL.set(NO_WATCH);
			return realStopwatch;
		} else {
			throw new IllegalStateException("no stopwatch installed");
		}
	}

	public static IStopwatch get() {
		return stopwatchTL.get();
	}

	public interface IStopwatchActivation extends AutoCloseable {

		void deactivate(StopwatchVisitor whenOver);

		IStopwatch stopwatch();

		default void deactivate() {
			deactivate(StopwatchesConfig.defaultVisitor);
		}

		@Override
		default void close() {
			deactivate();
		}
	}

	public static <T> IStopwatchActivation activate(String tag, T argument) {
		return activate(tag, argument, StopwatchesConfig.defaultEnabledPredicate);
	}

	public static <T> IStopwatchActivation activate(String tag, T argument, Predicate<T> enabledCheck) {
		if (enabledCheck.test(argument)) {
			InstalledStopwatch installedSw = installIfNone();
			RealStopwatch stopwatch = (RealStopwatch) get();
			stopwatch.start(tag);
			return new IStopwatchActivation() {
				@Override
				public void deactivate(StopwatchVisitor whenOver) {
					stopwatch.stop();
					if (installedSw.isNew) {
						uninstall();
						visit(stopwatch, whenOver);
					}
				}

				@Override
				public IStopwatch stopwatch() {
					return stopwatch;
				}
			};
		}
		IStopwatch stopwatch = get();
		stopwatch.start(tag);
		return new IStopwatchActivation() {
			@Override
			public void deactivate(StopwatchVisitor whenOver) {
				stopwatch.stop();
			}

			@Override
			public IStopwatch stopwatch() {
				return stopwatch;
			}
		};
	}

	private static void visit(RealStopwatch swatch, StopwatchVisitor processor) {
		StopwatchNode root = swatch.root;

		processor.enteredRoot(root);
		visit(root, processor);
		processor.exitedRoot(root);
	}

	private static void visit(StopwatchNode node, StopwatchVisitor processor) {
		if (node.invocations > 0) {
			processor.enteredNode(node);
			for (StopwatchNode value : node.children.values()) {
				visit(value, processor);
			}
			processor.exitedNode(node);
		}
	}

}