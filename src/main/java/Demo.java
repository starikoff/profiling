import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Demo {

	static {
		disableSslVerification();
	}

	private static void disableSslVerification() {
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
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

	private enum P {
		openConnection,
		getInputStream,
		copyStream,
		print,
		loop1,
		loop2,
		loop3
	}

	public static void main(String[] args) throws IOException {
//		{ // warmup
//
//			prof.activate("main");
//
//			long start = System.nanoTime();
//
//			prof.start("loop1");
//			for (int i = 0; i < 1e3; i++) {
//				prof.start("loop2");
//				for (int j = 0; j < 1e3; j++) {
//					prof.start("loop3");
//					if ((long) i + j == 3e9) {
//						System.out.println("impossible!");
//					}
//					prof.stop();
//				}
//				prof.stop();
//			}
//			prof.stop();
//
//			prof.deactivate(System.out::println);
//		}
//		{
//			IProfiler<P> profiler = Profiling.get(P.class, true);
//
//			profiler.start(P.openConnection);
//			URLConnection conn = new URL("https://edition.cnn.com/").openConnection();
//			profiler.stop();
//
//			profiler.start(P.getInputStream);
//			InputStream in = conn.getInputStream();
//			profiler.stop();
//
//			profiler.start(P.copyStream);
//			String bla = IOUtils.toString(in, StandardCharsets.UTF_8);
//			profiler.stop();
//
//			profiler.start(P.print);
//			System.out.println(bla);
//			profiler.stop();
//
//			long start = System.nanoTime();
//
//			profiler.start(P.loop1);
//			for (int i = 0; i < 1e3; i++) {
////			profiler.start(P.loop2);
//				for (int j = 0; j < 1e3; j++) {
////					IProfiler<P> subProfiler = profiler.spawn(P.class).start(P.loop3);
//					if ((long) i + j == 3e9) {
//						System.out.println("impossible!");
//					}
////					subProfiler.stop();
//				}
////			profiler.stop();
//			}
//			profiler.stop();
//
//			System.out.println(Profiling.ms(System.nanoTime() - start));
//
//			profiler.complete(System.out::println);
//		}
	}
}
