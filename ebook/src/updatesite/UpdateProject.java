package updatesite;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import ebook.core.Activator;
import ebook.utils.PreferenceSupplier;

public class UpdateProject {
	// repository location needs to be adjusted for your
	// location
	// private static final String REPOSITORY_LOC = System.getProperty(
	// "UpdateHandler.Repo", "file://home/vogella/repository");

	@Execute
	public void execute(final Shell parent, final UISynchronize sync,
			final IWorkbench workbench) {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class)
				.getBundleContext();
		ServiceReference<IProvisioningAgent> serviceReference = bundleContext
				.getServiceReference(IProvisioningAgent.class);
		IProvisioningAgent agent = bundleContext.getService(serviceReference);
		if (agent == null) {
			System.out.println(">> no agent loaded!");
			return;
		}
		// Adding the repositories to explore
		if (!P2Util.addRepository(agent,
				PreferenceSupplier.get(PreferenceSupplier.UPDATE_SITE))) {
			System.out.println(">> could no add repostory!");
			return;
		}
		// scheduling job for updates
		UpdateJob updateJob = new UpdateJob(agent, workbench);
		updateJob.schedule();

	}
}
