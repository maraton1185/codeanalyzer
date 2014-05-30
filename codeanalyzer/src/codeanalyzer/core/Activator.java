package codeanalyzer.core;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import codeanalyzer.core.interfaces.IDbConnection;
import codeanalyzer.core.interfaces.IServiceFactory;
import codeanalyzer.module.booksList.IBookListManager;
import codeanalyzer.module.cf.interfaces.ICfManager;
import codeanalyzer.module.users.interfaces.IUserManager;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "ru.codeanalyzer";
	private static Activator bundle;

	@Override
	public void start(BundleContext context) throws Exception {

		IEclipseContext ctx = E4Workbench.getServiceContext();
		ctx.set(ICfManager.class, pico.get(ICfManager.class));
		pico.get(ICfManager.class).init();

		ctx.set(IBookListManager.class, pico.get(IBookListManager.class));
		ctx.set(IUserManager.class, pico.get(IUserManager.class));
		ctx.set(IDbConnection.class, pico.get(IDbConnection.class));

		ctx.set(IServiceFactory.class, pico.get(IServiceFactory.class));

		bundle = this;

		App.getJetty().startJetty();
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
