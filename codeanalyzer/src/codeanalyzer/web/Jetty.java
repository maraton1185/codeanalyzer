package codeanalyzer.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.equinox.http.jetty.JettyConfigurator;

import codeanalyzer.core.Activator;
import codeanalyzer.utils.PreferenceSupplier;

public class Jetty implements IJetty {

	private boolean startjetty = true;
	private boolean debug = true;

	private int jettyPort;
	private String jettyMessage;

	@Override
	public String host() {
		return "http://localhost:" + jettyPort;
	};

	@Override
	public void startJetty() {

		if (!startjetty)
			return;

		jettyPort = findFreePort();
		jettyMessage = "Web-сервер: localhost:" + jettyPort;

		Dictionary<String, Object> settings = new Hashtable<String, Object>();
		settings.put("http.enabled", Boolean.TRUE);
		settings.put("http.port", jettyPort);
		settings.put("http.host", "localhost");
		settings.put("https.enabled", Boolean.FALSE);
		settings.put("context.path", "/");
		settings.put("context.sessioninactiveinterval", 1800);
		// settings.put(JettyConstants.CUSTOMIZER_CLASS,
		// "jettycustom.ServerCustomizer");

		Logger.getLogger("org.mortbay").setLevel(Level.WARNING); //$NON-NLS-1$	

		try {
			// server.start();
			// JettyConfigurator.stopServer(PLUGIN_ID + ".jetty");
			JettyConfigurator.startServer(Activator.PLUGIN_ID + ".jetty",
					settings);
		} catch (Exception e) {
			e.printStackTrace();
			jettyMessage = "Ошибка старта web-сервера (порт: " + jettyPort
					+ ")";
		}

	}

	private int findFreePort() {
		int port = 0;
		try (ServerSocket server = create(new int[] {
				PreferenceSupplier.getInt(PreferenceSupplier.REMOTE_PORT), 0 });) {
			port = server.getLocalPort();
			System.err.println(port);
		} catch (Exception e) {
			System.err.println("unable to find a free port");
			return 0;
		}
		return port;
	}

	private ServerSocket create(int[] ports) throws IOException {
		for (int port : ports) {
			try {
				return new ServerSocket(port);
			} catch (IOException ex) {
				continue; // try next port
			}
		}

		// if the program gets here, no port in the range was found
		throw new IOException("no free port found");
	}

	@Override
	public String jettyMessage() {
		return jettyMessage;
	}

	@Override
	public String info() {
		return host().concat("/info");
	}

	@Override
	public boolean debug() {
		return debug;
	}
}

// try {
// Class.forName("jettycustom.ServerCustomizer");
// } catch (ClassNotFoundException e1) {
// // NEXT Auto-generated catch block
// e1.printStackTrace();
// }

// Server server = new Server(jettyPort);
//
// String WEBAPPDIR = "_web/";
// String CONTEXTPATH = "/admin";
//
// final URL warUrl = this.getClass().getClassLoader()
// .getResource(WEBAPPDIR);
// final String warUrlString = warUrl.toExternalForm();
// server.setHandler(new WebAppContext(warUrlString, CONTEXTPATH));

// WebAppContext webapp = new WebAppContext();
// webapp.setContextPath("/");
// webapp.setWar("../../jetty-distribution/target/distribution/demo-base/webapps/test.war");

// Bundle bundle = FrameworkUtil.getBundle(Jetty.class);
// URL warUrl = FileLocator.find(bundle, new Path("icons/"), null);
// final URL warUrl = FrameworkUtil.getBundle(Jetty.class).getResource(
// WEBAPPDIR);
// final String warUrlString = warUrl.toExternalForm();
// server.setHandler(new WebAppContext(warUrl.toString(), CONTEXTPATH));
