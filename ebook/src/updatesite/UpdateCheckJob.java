package updatesite;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.UpdateOperation;

import ebook.core.App;
import ebook.utils.Events;

public class UpdateCheckJob extends Job {

	private IProvisioningAgent agent;
	private IStatus result;

	/**
	 * Base constructor.
	 * 
	 * @param agent
	 *            Provisioning agent
	 */
	public UpdateCheckJob() {
		super("Checking for updates");
		this.agent = App.agent;
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		if (agent == null)
			return null;
		result = P2Util.checkForUpdates(agent, arg0);
		if (result.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {

			// Utils.popUpInformation(Strings.get("updateNotFound"));

		} else {

			App.br.post(Events.SHOW_UPDATE_AVAILABLE, null);
			// installUpdates();
		}
		return result;
	}
}
