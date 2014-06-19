package ebook.core;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ebook.core.interfaces.IDbConnection;
import ebook.core.interfaces.IManagerFactory;
import ebook.core.interfaces.IServiceFactory;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "ru.ebook";
	private static Activator bundle;

	@Override
	public void start(BundleContext context) throws Exception {

		IEclipseContext ctx = E4Workbench.getServiceContext();
		// ctx.set(IConfManager.class, pico.get(IConfManager.class));
		// pico.get(IConfManager.class).init();

		// ctx.set(IBookListManager.class, pico.get(IBookListManager.class));
		// ctx.set(IUserManager.class, pico.get(IUserManager.class));
		// ctx.set(IConfManager.class, pico.get(IConfManager.class));
		ctx.set(IDbConnection.class, pico.get(IDbConnection.class));

		ctx.set(IServiceFactory.class, pico.get(IServiceFactory.class));
		ctx.set(IManagerFactory.class, pico.get(IManagerFactory.class));

		bundle = this;

		Bundle bundle = Platform.getBundle("org.eclipse.equinox.http.registry");
		if (bundle.getState() == Bundle.RESOLVED) {
			bundle.start(Bundle.START_TRANSIENT);
		}

		// App.getJetty().startJetty();
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
