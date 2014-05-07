package codeanalyzer.core;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.core.interfaces.IDbManager;

public class Activator implements BundleActivator {

	private static Activator bundle;

	@Override
	public void start(BundleContext context) throws Exception {

		IEclipseContext ctx = E4Workbench.getServiceContext();
		ctx.set(IDbManager.class, pico.get(IDbManager.class));
		pico.get(IDbManager.class).init();

		ctx.set(IBookManager.class, pico.get(IBookManager.class));

		bundle = this;

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
