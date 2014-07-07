package ebook.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import ebook.core.Activator;
import ebook.utils.PreferenceSupplier;

public class Jetty implements IJetty {

	private int jettyPort;
	private String jettyMessage = "Web-сервер не запущен";
	private JettyStatus status = JettyStatus.stopped;
	private boolean manualStart = false;

	HashMap<IPath, Connection> connectionPull = new HashMap<IPath, Connection>();

	private final Map<String, Server> servers = new HashMap<String, Server>();

	@Override
	public HashMap<IPath, Connection> pull() {
		return connectionPull;
	}

	@Override
	public void startJetty() {

		jettyMessage = "Web-сервер не запущен";
		if (!PreferenceSupplier.getBoolean(PreferenceSupplier.START_JETTY)
				&& !manualStart)
			return;

		jettyPort = findFreePort();
		jettyMessage = "Web-сервер: localhost:" + jettyPort;

		// try {
		// Class.forName("jettycustom.ServerCustomizer");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(findFreePort());
		server.setConnectors(new Connector[] { connector });

		String webapp = "/webapp";
		WebAppContext context = new WebAppContext();
		context.setDescriptor(webapp + "/WEB-INF/web.xml");
		context.setResourceBase("webapp");
		context.setContextPath("/");
		context.setParentLoaderPriority(true);

		server.setHandler(context);

		try {

			server.start();
			servers.put(Activator.PLUGIN_ID + ".jetty", server);

			// Dictionary<String, Object> settings = new Hashtable<String,
			// Object>();
			// settings.put("http.enabled", Boolean.TRUE);
			// settings.put("http.port", jettyPort);
			// settings.put("http.host", "localhost");
			// settings.put("https.enabled", Boolean.FALSE);
			// settings.put("context.path", "/");
			// settings.put("context.sessioninactiveinterval", 1800);
			// settings.put(JettyConstants.CUSTOMIZER_CLASS,
			// "jettycustom.ServerCustomizer");
			//
			//		Logger.getLogger("org.mortbay").setLevel(Level.WARNING); //$NON-NLS-1$	

			// try {
			// server.start();
			// JettyConfigurator.stopServer(PLUGIN_ID + ".jetty");
			// JettyConfigurator.startServer(Activator.PLUGIN_ID + ".jetty",
			// settings);

			status = JettyStatus.started;
		} catch (Exception e) {
			e.printStackTrace();
			jettyMessage = "Ошибка старта web-сервера (порт: " + jettyPort
					+ ")";

			status = JettyStatus.error;
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
	public JettyStatus status() {
		return status;
	}

	@Override
	public void setManual() {
		manualStart = true;

	}

	@Override
	public String host() {
		return "http://localhost:" + jettyPort + "/";
	}

	@Override
	public String info() {
		return host().concat("info");
	}

	@Override
	public String book(Integer book, Integer section) {
		return host().concat(
				"book?book=" + book.toString() + "&id=" + section.toString());
	}

	@Override
	protected void finalize() {
		for (Connection con : connectionPull.values()) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
