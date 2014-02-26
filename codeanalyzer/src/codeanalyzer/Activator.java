package codeanalyzer;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDbManager;


public class Activator implements BundleActivator {
	
	private static Activator bundle;
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		IEclipseContext ctx = E4Workbench.getServiceContext();
		ctx.set(IDbManager.class, pico.get(IDbManager.class));
		pico.get(IDbManager.class).init();
		
		bundle = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		//work

	}

	public static Activator getDefault() {
		return bundle;
	}
}
