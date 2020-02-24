import com.ratherabstract.timing.prof.Profs;
import com.ratherabstract.timing.prof_fmt.FormatAndPrintVisitor;
import com.ratherabstract.timing.prof_fmt.flamegraph.StopwatchFlamegraphFormatter;
import com.ratherabstract.timing.swatch.IStopwatch;
import com.ratherabstract.timing.swatch.Stopwatches;
import com.ratherabstract.timing.swatch.Stopwatches.IStopwatchActivation;
import com.ratherabstract.timing.swatch.StopwatchesConfig;
import com.ratherabstract.timing.swatch.TimeStats;
import com.ratherabstract.timing.swatch_fmt.TimeStatsFmtUtils;
import org.apache.commons.io.IOUtils;
import sun.management.HotspotRuntimeMBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DemoHTimers2 {

//	private static final ClockProfiler<?> prof;

	private static final HotspotRuntimeMBean rtMB;

	static {
//		Slf4jClockProfilersConfig.formatter = new StopwatchPlainFormatter();
//		Slf4jClockProfilersConfig.formatter = new StopwatchXmlFormatter();
//		prof = new ClockProfiler<>("hTimers1", (ign) -> true, new FormatAndPrintVisitor(new StopwatchPlainFormatter(),
//																						System.out::println));
//		prof = new ClockProfiler<>("hTimers1", (ign) -> true, new FormatAndPrintVisitor(new StopwatchXmlFormatter(),
//																						System.out::println));
		disableSslVerification();
		rtMB = sun.management.ManagementFactoryHelper.getHotspotRuntimeMBean();
	}

	private static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
					}

					public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
					}
				} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	private static double eval(double[] coef, double x) {
		double res = 0;
		for (int i = 0; i < coef.length; i++) {
			res = res * x + coef[i];
		}
		return res;
	}

	private static final double[] coef = {0.0000001, 0.27, 0.1, 0.24, 1, 0.79, 1e-5, 1e-6, 1e-7};
	private static double x = 0.1;

	public static void main(String[] args) throws IOException, InterruptedException {

		long r = 0;

		TimeStats ts = new TimeStats();

		long manyNS = TimeUnit.SECONDS.toNanos(100);
		long someNS = TimeUnit.MILLISECONDS.toNanos(13);
		long someNS2 = TimeUnit.MILLISECONDS.toNanos(20);

		double res = 0;

//		long[] src = {1, manyNS, 1001, manyNS / 1000, 101, manyNS / 101};
//		long[] src = {70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
//			1600};
		long[] src = {someNS, someNS2, someNS, someNS2, someNS, someNS2, someNS, someNS2, someNS, someNS2, someNS, someNS2};

		for (int pow = 2; pow < 10; pow++) {

			int it = (int) Math.pow(10, pow);

//			RealStopwatch sw = Stopwatches.install();

			for (int i = 0; i < it; i++) {
//				Thread.currentThread().isInterrupted();
//				if (Thread.currentThread().isInterrupted()) {
//					r++;
//				}

//				x += eval(coef, x);

//				sw.start("foo" + (i % 2));
//				sw.stop();

//				r += System.nanoTime();
//				ts.ingest(src[i % src.length]);
//				r += (src[i % src.length]);
//				if (src[i % src.length] == 1234) {
//					r++;
//				}
//				ts.ingest(90);
//				ts.ingest(850);
			}

			if (x != 100500.) {
				x = 0.1;
			}

			long dtNS;
			long safeptCnt0;
			do {
				safeptCnt0 = rtMB.getSafepointCount();
				long t0 = System.nanoTime();

				for (int i = 0; i < it; i++) {
//				Thread.currentThread().isInterrupted();
//				if (Thread.currentThread().isInterrupted()) {
//					r++;
//				}

//					x += eval(coef, x);

//				sw.start("foo" + (i % 2));
//				sw.stop();

//					r += System.nanoTime();
//					ts.ingest(src[i % src.length]);
//				if (src[i % src.length] == 1234) {
//					r++;
//				}
//				ts.ingest(70);
//				ts.ingest(850);
				}
//			Stopwatches.uninstall();
				long t1 = System.nanoTime();
				safeptCnt0 = rtMB.getSafepointCount() - safeptCnt0;
				dtNS = t1 - t0;
			} while (safeptCnt0 != 0);

			System.out.println(((double)dtNS) / it);

			//			if (r == -1) {
//				System.out.println("unbelievable!");
//			}

//			if (sw.root.invocations > 1) {
//				System.out.println("impossible!");
//			}

			if (r == -1) {
				System.out.println("unbelievable!");
			}
			if (x == 100500.) {
				System.out.println("unbelievable!");
			}
			if (ts.counts[0] == 100500) {
				System.out.println(TimeStatsFmtUtils.format(ts));
			}
		}

		// warmup
//		for (int i = 0; i < 1000; i++) {
//			Util.work();
//		}

//		Thread.sleep(5000);

		Util.work();

		StopwatchesConfig.defaultVisitor = new FormatAndPrintVisitor(new StopwatchFlamegraphFormatter(), System.out::println);
//		StopwatchesConfig.defaultVisitor = new FormatAndPrintVisitor(new StopwatchPlainFormatter(), System.out::println);
		StopwatchesConfig.defaultEnabledPredicate = (_any) -> true;

		try (IStopwatchActivation activation = Stopwatches.activate("DemoHTimers2.main", null)) {
			IStopwatch sw = Stopwatches.get();

//		prof.start("work1");
			Util.work();
//		prof.stop();

			URLConnection conn =
				Profs.profilingX("open connection", () -> new URL("https://edition.cnn.com/").openConnection());

			InputStream in = Profs.profilingX("getInputStream", conn::getInputStream);

			sw.start("copyStream");
			String bla = IOUtils.toString(in, StandardCharsets.UTF_8);
			sw.stop();

			sw.start("print");
//		System.out.println(bla);
			sw.stop();

//		prof.start("work2");
			Util.work();
//		prof.stop();
		}
	}
}
