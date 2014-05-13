package codeanalyzer.core;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.core.interfaces.IDbManager;

public class Activator implements BundleActivator {

	private static final String PLUGIN_ID = "ru.codeanalyzer";
	private static Activator bundle;

	@Override
	public void start(BundleContext context) throws Exception {

		IEclipseContext ctx = E4Workbench.getServiceContext();
		ctx.set(IDbManager.class, pico.get(IDbManager.class));
		pico.get(IDbManager.class).init();

		ctx.set(IBookManager.class, pico.get(IBookManager.class));

		bundle = this;

		// Server server = new Server(8080);

		Dictionary settings = new Hashtable();
		settings.put("http.enabled", Boolean.TRUE);
		settings.put("http.port", 8081);
		settings.put("http.host", "0.0.0.0");
		settings.put("https.enabled", Boolean.FALSE);
		settings.put("context.path", "/");
		settings.put("context.sessioninactiveinterval", 1800);

		try {
			// JettyConfigurator.stopServer(PLUGIN_ID + ".jetty");
			JettyConfigurator.startServer(PLUGIN_ID + ".jetty", settings);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// E4Services.disposed = true;
		// IJobManager jobMan = Job.getJobManager();
		// jobMan.cancel(FillProcLinkTableJob.MY_FAMILY);
		// jobMan.join(FillProcLinkTableJob.MY_FAMILY, null);
	}

	public static Activator getDefault() {
		return bundle;
	}
}
