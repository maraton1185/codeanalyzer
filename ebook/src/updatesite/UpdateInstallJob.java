package updatesite;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.statushandlers.StatusManager;

import ebook.core.Activator;
import ebook.core.App;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class UpdateInstallJob extends Job {

	private IProvisioningAgent agent;
	private IStatus result;

	/**
	 * Base constructor.
	 * 
	 * @param agent
	 *            Provisioning agent
	 */
	public UpdateInstallJob() {
		super("Install updates");
		this.agent = App.agent;
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		if (agent == null)
			return null;
		result = P2Util.checkForUpdates(agent, arg0);
		if (result.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			Utils.popUpInformation(Strings.msg("updateNotFound"));
		} else {
			installUpdates();
		}
		return result;
	}

	/**
	 * Pop up for updates installation and, if accepted, install updates.
	 */
	private void installUpdates() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				boolean install = MessageDialog.openQuestion(null,
						Strings.title("appTitle"),
						Strings.msg("installUpdates?"));
				if (install) {
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(
							Display.getDefault().getActiveShell());
					try {
						dialog.run(true, true, new IRunnableWithProgress() {
							@Override
							public void run(IProgressMonitor arg0)
									throws InvocationTargetException,
									InterruptedException {
								P2Util.installUpdates(agent, arg0);
								PreferenceSupplier
										.set(PreferenceSupplier.SHOW_ABOUT_ON_STARTUP,
												true);
								PreferenceSupplier.save();
								App.br.post(Events.RESTART_WORKBENCH, null);
								// workbench.restart();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						result = new Status(Status.ERROR, Activator.PLUGIN_ID,
								Strings.msg("updateError"), e);
						StatusManager.getManager().handle(result);
					}
				}
			}
		});
	}

}
