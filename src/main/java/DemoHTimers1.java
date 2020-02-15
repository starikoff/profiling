import org.apache.commons.io.IOUtils;
import com.ratherabstract.timing.prof.Profs;
import com.ratherabstract.timing.prof_fmt.plain1.StopwatchPlainFormatter;
import com.ratherabstract.timing.prof_slf4j.Slf4jClockProfiler;
import com.ratherabstract.timing.prof_slf4j.Slf4jClockProfilers;
import com.ratherabstract.timing.prof_slf4j.Slf4jClockProfilersConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DemoHTimers1 {

	private static final Slf4jClockProfiler prof;

	static {
		Slf4jClockProfilersConfig.formatter = new StopwatchPlainFormatter();
//		Slf4jClockProfilersConfig.formatter = new StopwatchXmlFormatter();
		prof = Slf4jClockProfilers.get(DemoHTimers1.class);
		disableSslVerification();
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

	public static void main(String[] args) throws IOException, InterruptedException {

		// warmup
//		for (int i = 0; i < 1000; i++) {
//			Util.work();
//		}

//		Thread.sleep(5000);

		prof.activate("DemoStopwatches1.main");

//		prof.start("work1");
		Util.work();
//		prof.stop();

		URLConnection conn =
			Profs.profilingX(prof, "open connection", () -> new URL("https://edition.cnn.com/").openConnection());

		InputStream in = Profs.profilingX(prof, "getInputStream", conn::getInputStream);

		prof.start("copyStream");
		String bla = IOUtils.toString(in, StandardCharsets.UTF_8);
		prof.stop();

		prof.start("print");
		System.out.println(bla);
		prof.stop();

//		prof.start("work2");
		Util.work();
//		prof.stop();

		prof.deactivate();
	}
}
