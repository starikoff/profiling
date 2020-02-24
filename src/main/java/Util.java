import com.ratherabstract.timing.swatch.IStopwatch;
import com.ratherabstract.timing.swatch.Stopwatches;
import com.ratherabstract.timing.swatch.Stopwatches.IStopwatchActivation;

public class Util {

//	private static final Slf4jClockProfiler prof = Slf4jClockProfilers.get(Util.class);

	public static void work() {
//		long t0 = System.currentTimeMillis();
		try (IStopwatchActivation activation = Stopwatches.activate("work()", null)) {
			IStopwatch sw = activation.stopwatch();

			sw.start("loop1");
			for (int i = 0; i < 1e3; i++) {
				sw.start("loop2");
				sw.start("loop3-out", (int) 1e3);
				for (int j = 0; j < 1e3; j++) {
					sw.start("loop3");
					if ((long) i + j == 3e9) {
						System.out.println("impossible!");
					}
					sw.stop();
				}
				sw.stop();
				sw.stop();
			}
			sw.stop();

		}
//		System.out.println("loop time: " + (System.currentTimeMillis() - t0));
	}
}
