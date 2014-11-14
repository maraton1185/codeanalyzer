package updatesite;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import ebook.core.Activator;
import ebook.utils.Strings;

public class UpdateProject {

	@Execute
	public void execute() {

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
		if (!P2Util.addRepository(agent, Strings.updateSite)) {
			System.out.println(">> could no add repostory!");
			return;
		}
		// scheduling job for updates
		UpdateJob updateJob = new UpdateJob(agent);
		updateJob.schedule();

	}
}
