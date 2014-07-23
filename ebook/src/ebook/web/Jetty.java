package ebook.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jasper.servlet.JspServlet;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import ebook.core.App;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.web.servlets.WebDefaultServlet;

public class Jetty implements IJetty {

	private int jettyPort;
	private String jettyMessage = "Web-сервер не запущен";
	private JettyStatus status = JettyStatus.stopped;
	private boolean manualStart = false;

	HashMap<IPath, Connection> connectionPull = new HashMap<IPath, Connection>();

	// private final Map<String, Server> servers = new HashMap<String,
	// Server>();

	@Override
	public HashMap<IPath, Connection> pull() {
		return connectionPull;
	}

	// private int port;
	private Server server;
	private URI serverURI;
	private boolean openBookOnStratUp;
	private String swt;
	private static final String WEBROOT_INDEX = "/webroot/";
	private static final Logger LOG = Logger.getLogger(Main.class.getName());

	@Override
	public void stop() {

		try {
			server.stop();

			jettyMessage = "Web-сервер не запущен";
			status = JettyStatus.stopped;

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Override
	public void start() {

		jettyMessage = "Web-сервер не запущен";
		if (!PreferenceSupplier.getBoolean(PreferenceSupplier.START_JETTY)
				&& !manualStart)
			return;

		jettyPort = findFreePort();
		jettyMessage = "Web-сервер: localhost:" + jettyPort;

		try {

			Random rand = new Random();
			swt = Integer.toString(rand.nextInt());

			server = new Server();
			ServerConnector connector = new ServerConnector(server);
			connector.setPort(jettyPort);
			server.addConnector(connector);

			boolean EXTERNAL_JETTY_BASE = PreferenceSupplier
					.getBoolean(PreferenceSupplier.EXTERNAL_JETTY_BASE);
			URL indexUri;
			if (EXTERNAL_JETTY_BASE) {

				File file = new File(
						PreferenceSupplier.get(PreferenceSupplier.JETTY_BASE));
				if (!file.exists())
					throw new FileNotFoundException(
							"Unable to find jetty base "
									+ file.getAbsolutePath());

				indexUri = file.toURI().toURL();

				// throw new FileNotFoundException("Unable to find jetty base "
				// + file.getAbsolutePath());

			} else

				indexUri = this.getClass().getResource(WEBROOT_INDEX);

			if (indexUri == null) {
				throw new FileNotFoundException("Unable to find resource "
						+ WEBROOT_INDEX);
			}

			// Points to wherever /webroot/ (the resource) is
			URI baseUri = indexUri.toURI();

			// Establish Scratch directory for the servlet context (used by JSP
			// compilation)
			File tempDir = new File(System.getProperty("java.io.tmpdir"));
			File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

			if (!scratchDir.exists()) {
				if (!scratchDir.mkdirs()) {
					throw new IOException(
							"Unable to create scratch directory: " + scratchDir);
				}
			}

			// Set JSP to use Standard JavaC always
			System.setProperty("org.apache.jasper.compiler.disablejsr199",
					"false");

			// Setup the basic application "context" for this application at "/"
			// This is also known as the handler tree (in jetty speak)
			WebAppContext context = new WebAppContext();
			context.setContextPath("/");
			context.setAttribute("javax.servlet.context.tempdir", scratchDir);
			context.setResourceBase(baseUri.toASCIIString());
			// context.setAttribute(InstanceManager.class.getName(),
			// new SimpleInstanceManager());
			if (!EXTERNAL_JETTY_BASE)
				context.setDefaultsDescriptor(WEBROOT_INDEX + "WEB-INF/web.xml");
			// context.setServer(server);

			// ResourceHandler resource_handler = new ResourceHandler();
			// resource_handler.setDirectoriesListed(true);
			// resource_handler.setWelcomeFiles(new String[] { "index.html" });
			//
			// resource_handler.setResourceBase(baseUri.toASCIIString());
			//
			// HandlerList handlers = new HandlerList();
			// handlers.setHandlers(new Handler[] { resource_handler, context
			// });

			// HandlerList handlers = new HandlerList();
			// handlers.setHandlers(new Handler[] { context, resource_handler
			// });
			// server.setHandler(handlers);

			server.setHandler(context);

			// Add Application Servlets
			// context.addServlet(DateServlet.class, "/date/");

			// Ensure the jsp engine is initialized correctly
			JettyJasperInitializer sci = new JettyJasperInitializer();
			ServletContainerInitializersStarter sciStarter = new ServletContainerInitializersStarter(
					context);
			ContainerInitializer initializer = new ContainerInitializer(sci,
					null);
			List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
			initializers.add(initializer);

			context.setAttribute("org.eclipse.jetty.containerInitializers",
					initializers);
			context.addBean(sciStarter, true);

			// context.preConfigure();

			// Set Classloader of Context to be sane (needed for JSTL)
			// JSP requires a non-System classloader, this simply wraps the
			// embedded System classloader in a way that makes it suitable
			// for JSP to use

			// ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this
			// .getClass().getClassLoader());
			// context.setClassLoader(jspClassLoader);

			context.setClassLoader(Thread.currentThread()
					.getContextClassLoader());

			// Add JSP Servlet (must be named "jsp")
			ServletHolder holderJsp = new ServletHolder("jsp", JspServlet.class);
			holderJsp.setInitOrder(0);
			holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
			holderJsp.setInitParameter("fork", "false");
			holderJsp.setInitParameter("xpoweredBy", "false");
			holderJsp.setInitParameter("compilerTargetVM", "1.7");
			holderJsp.setInitParameter("compilerSourceVM", "1.7");
			holderJsp.setInitParameter("keepgenerated", "true");
			holderJsp.setInitParameter("classpath", context.getClassPath());
			context.addServlet(holderJsp, "*.jsp");
			// context.addServlet(holderJsp,"*.jspf");
			// context.addServlet(holderJsp,"*.jspx");

			// Add Example of mapping jsp to path spec
			// ServletHolder holderAltMapping = new ServletHolder("foo.jsp",
			// JspServlet.class);
			// holderAltMapping.setForcedPath("/test/foo/foo.jsp");
			// context.addServlet(holderAltMapping, "/test/foo/");

			// Add Default Servlet (must be named "default")
			ServletHolder holderDefault = new ServletHolder("default",
					WebDefaultServlet.class);
			LOG.info("Base URI: " + baseUri);
			holderDefault.setInitParameter("resourceBase",
					baseUri.toASCIIString());
			holderDefault.setInitParameter("cacheControl", "max-age=0,public");
			holderDefault.setInitParameter("welcomeServlets", "true");
			// holderDefault.setInitParameter("resourceBase", ".");
			// holderDefault.setInitParameter("useFileMappedBuffer", "true");

			holderDefault.setInitParameter("dirAllowed", "true");
			context.addServlet(holderDefault, "/");

			// ServletHolder holderDefault = context.addServlet(
			// DefaultServlet.class, "/");
			// holderDefault.setInitParameter("resourceBase",
			// baseUri.toASCIIString());
			// holderDefault.setInitParameter("dirAllowed", "true");

			// Start Server
			server.start();

			// Show server state
			if (LOG.isLoggable(Level.FINE)) {
				LOG.fine(server.dump());
			}

			// Establish the Server URI
			String scheme = "http";
			for (ConnectionFactory connectFactory : connector
					.getConnectionFactories()) {
				if (connectFactory.getProtocol().equals("SSL-http")) {
					scheme = "https";
				}
			}
			String host = connector.getHost();
			if (host == null) {
				host = "localhost";
			}
			int port = connector.getLocalPort();
			serverURI = new URI(
					String.format("%s://%s:%d/", scheme, host, port));
			LOG.info("Server URI: " + serverURI);

			// try {
			// Class.forName("jettycustom.ServerCustomizer");
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// Server server = new Server();
			//
			// ServerConnector connector = new ServerConnector(server);
			// connector.setPort(findFreePort());
			// server.setConnectors(new Connector[] { connector });
			//
			// String webapp = "/webapp";
			// WebAppContext context = new WebAppContext();
			// context.setDescriptor(webapp + "/WEB-INF/web.xml");
			// context.setResourceBase("webapp");
			// context.setContextPath("/");
			// context.setParentLoaderPriority(true);
			//
			// server.setHandler(context);

			// server.start();
			// servers.put(Activator.PLUGIN_ID + ".jetty", server);

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
	public boolean isStarted() {
		return status == JettyStatus.started;
	}

	@Override
	public void setManual() {
		manualStart = true;

	}

	@Override
	public String host() {
		return "http://localhost:" + jettyPort + "/";
	}

	// @Override
	// public String info() {
	// return "info";
	// }

	@Override
	public String book(Integer book) {
		// return host().concat("book?book=" + book.toString());
		return "book?book=" + book.toString();
	}

	@Override
	public String list(Integer id) {
		return "list?book=" + id.toString();
	}

	@Override
	public String list() {
		return "list";
	}

	@Override
	public String section(Integer book, Integer section) {
		return "book?book=" + book.toString() + "&id=" + section.toString();
	}

	@Override
	public String bookImage(Integer book, Integer id) {
		return "book_img?book=" + book.toString() + "&id=" + id.toString();
	}

	@Override
	public String listImage(Integer book) {
		return "book_list_img?book=" + book.toString();
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

	@Override
	public void openBookOnStratUp() {

		if (!openBookOnStratUp)
			return;

		IPath p = new Path(
				PreferenceSupplier.get(PreferenceSupplier.BOOK_ON_STARTUP));
		if (p.isEmpty())
			return;

		App.mng.blm().open(p, null);

		App.br.post(Events.EVENT_SHOW_BOOK, null);

		openBookOnStratUp = false;
	}

	@Override
	public void setOpenBookOnStratUp() {
		openBookOnStratUp = true;

	}

	@Override
	public String swt() {

		return swt;

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
