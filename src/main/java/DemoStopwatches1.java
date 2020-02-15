import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ratherabstract.timing.swatch.IStopwatch;
import com.ratherabstract.timing.swatch.RealStopwatch;
import com.ratherabstract.timing.swatch_fmt.StopwatchXmlFmtUtils;
import com.ratherabstract.timing.swatch.Stopwatches;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Optional;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DemoStopwatches1 {

	private static final Logger log = LoggerFactory.getLogger(DemoStopwatches1.class);

	static {
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

	public static void work() {
		Optional<RealStopwatch> swatchOpt = Optional.empty();
		if (log.isTraceEnabled()) {
			swatchOpt = Stopwatches.installIfNone();
		}
		IStopwatch swatch = Stopwatches.get();
		swatch.start("work()");

		swatch.start("loop1");
		for (int i = 0; i < 1e3; i++) {
			swatch.start("loop2");
			swatch.start("loop3", (int) 1e3);
			for (int j = 0; j < 1e3; j++) {
				if ((long) i + j == 3e9) {
					System.out.println("impossible!");
				}
			}
			swatch.stop();
			swatch.stop();
		}
		swatch.stop();

		swatch.stop();
		swatchOpt.ifPresent(s -> {
			s.complete();
			if (log.isTraceEnabled()) {
				log.trace(System.lineSeparator() + StopwatchXmlFmtUtils.dumpXML(s));
			}
		});
	}

	public static void main(String[] args) throws IOException {
		Optional<RealStopwatch> swatchOpt = Optional.empty();
		if (log.isTraceEnabled()) {
			swatchOpt = Stopwatches.installIfNone();
		}
		IStopwatch swatch = Stopwatches.get();
		swatch.start("DemoStopwatches1.main");

		swatch.start("open connection");
		URLConnection conn = new URL("https://edition.cnn.com/").openConnection();
		swatch.stop();

		swatch.start("getInputStream");
		InputStream in = conn.getInputStream();
		swatch.stop();

		swatch.start("copyStream");
		String bla = IOUtils.toString(in, StandardCharsets.UTF_8);
		swatch.stop();

		swatch.start("print");
		System.out.println(bla);
		swatch.stop();

		work();

		swatch.stop();
		swatchOpt.ifPresent(s -> {
			s.complete();
			if (log.isTraceEnabled()) {
				log.trace(System.lineSeparator() + StopwatchXmlFmtUtils.dumpXML(s));
			}
		});
	}
}
