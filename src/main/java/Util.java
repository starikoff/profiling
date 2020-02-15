import com.ratherabstract.timing.prof_slf4j.Slf4jClockProfiler;
import com.ratherabstract.timing.prof_slf4j.Slf4jClockProfilers;

public class Util {

	private static final Slf4jClockProfiler prof = Slf4jClockProfilers.get(Util.class);

	public static void work() {
//		long t0 = System.currentTimeMillis();
		prof.activate("work()");


		prof.start("loop1");
		for (int i = 0; i < 1e3; i++) {
			prof.start("loop2");
			prof.start("loop3-out", (int) 1e3);
			for (int j = 0; j < 1e3; j++) {
//				prof.start("loop3");
				if ((long) i + j == 3e9) {
					System.out.println("impossible!");
				}
//				prof.stop();
			}
			prof.stop();
			prof.stop();
		}
		prof.stop();


		prof.deactivate();
//		System.out.println("loop time: " + (System.currentTimeMillis() - t0));
	}
}
