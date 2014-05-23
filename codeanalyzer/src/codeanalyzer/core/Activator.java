package codeanalyzer.core;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import codeanalyzer.core.interfaces.IDbService;
import codeanalyzer.module.books.interfaces.IBookListManager;
import codeanalyzer.module.cf.interfaces.ICfManager;
import codeanalyzer.module.users.interfaces.IUserManager;

public class Activator implements BundleActivator {

	private static final String PLUGIN_ID = "ru.codeanalyzer";
	private static Activator bundle;

	@Override
	public void start(BundleContext context) throws Exception {

		IEclipseContext ctx = E4Workbench.getServiceContext();
		ctx.set(ICfManager.class, pico.get(ICfManager.class));
		pico.get(ICfManager.class).init();

		ctx.set(IBookListManager.class, pico.get(IBookListManager.class));
		ctx.set(IUserManager.class, pico.get(IUserManager.class));
		ctx.set(IDbService.class, pico.get(IDbService.class));

		bundle = this;

		// Dictionary settings = new Hashtable();
		// settings.put("http.enabled", Boolean.TRUE);
		// settings.put("http.port", 8081);
		// settings.put("http.host", "0.0.0.0");
		// settings.put("https.enabled", Boolean.FALSE);
		// settings.put("context.path", "/");
		// settings.put("context.sessioninactiveinterval", 1800);
		//
		// try {
		// // JettyConfigurator.stopServer(PLUGIN_ID + ".jetty");
		// JettyConfigurator.startServer(PLUGIN_ID + ".jetty", settings);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
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
