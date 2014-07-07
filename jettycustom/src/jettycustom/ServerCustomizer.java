package jettycustom;

import java.util.Dictionary;

import org.eclipse.equinox.http.jetty.JettyCustomizer;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class ServerCustomizer extends JettyCustomizer {

	@Override
	public Object customizeContext(Object _context,
			Dictionary<String, ?> settings) {
		ServletContextHandler context = (ServletContextHandler) _context;
		// String path = Activator.class.getResource("/").toExternalForm();
		// path = path.substring(0, path.length() - 1);
		// IPath p = new Path(path);

		// context.setClassLoader(Thread.currentThread().getContextClassLoader());
		context.setContextPath("/");
		context.setResourceBase(".");
		// context.setwe
		// context.setWelcomeFiles(new String[] { "error.jsp" });
		// context.setContextPath("/_web");

		// if (!pico.get(IJetty.class).debug()) {
		// ErrorHandler errorHandler = new JettyErrorHandler();
		// errorHandler.setShowStacks(true);
		// context.setErrorHandler(errorHandler);

		// }
		// context.addServlet(new ServletHolder(new IndexServlet()), "/test");
		//
		// // Server s = context.getServer(); // return null
		// context.addServlet(
		// new ServletHolder(new JspServlet(Platform
		// .getBundle("codeanalyzer"), "/_web/error.jsp")),
		// "/*.jsp");

		// context.addServlet(, "");
		// final HttpService httpService =
		// (HttpService)context.getService(reference);
		// HttpContext httpContext = new
		// BundleEntryHttpContext(context.getBundle(),"/web");
		// httpService.registerServlet("/*.jsp",new
		// JspServlet(context.getBundle(),
		// "/web"), null, httpContext);
		// try {
		//
		// Class.forName("org.eclipse.equinox.jsp.jasper.registry.JSPFactory");
		// context.addServlet(
		// "org.eclipse.equinox.jsp.jasper.registry.JSPFactory:/",
		// "/*.jsp");
		// } catch (ClassNotFoundException e) {
		// // NEXT Auto-generated catch block
		// e.printStackTrace();
		// }
		// pico.get(type)
		// WebAppContext w;
		// App.

		// final String WEBAPPDIR = "com/xxx/yyy/webapp";
		// final String CONTEXTPATH = "/admin";
		//
		// // for localhost:port/admin/index.html and whatever else is in the
		// webapp directory
		// final URL warUrl =
		// this.class.getClassLoader().getResource(WEBAPPDIR);
		// final String warUrlString = warUrl.toExternalForm();
		// server.setHandler(new WebAppContext(warUrlString, CONTEXTPATH));

		return super.customizeContext(context, settings);
	}
}
