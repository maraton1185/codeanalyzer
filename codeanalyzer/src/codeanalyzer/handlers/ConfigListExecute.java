package codeanalyzer.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.E4Services;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.utils.Const;

public class ConfigListExecute {

	@Execute
	public void execute(@Named(Const.CONTEXT_SELECTED_DB) IDb db,
			IDbManager dbManager, Shell shell) {

		dbManager.execute(db, shell);
		E4Services.br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);

		// Job job = new Job("My Job") {
		// @Override
		// protected IStatus run(IProgressMonitor monitor) {
		// // set total number of work units
		// monitor.beginTask("Doing something time consuming here", 100);
		//
		// for (int i = 0; i < 5; i++) {
		// try {
		// // sleep a second
		// TimeUnit.SECONDS.sleep(1);
		//
		// monitor.subTask("I'm doing something here " + i);
		//
		// // report that 20 additional units are done
		// monitor.worked(20);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// return Status.CANCEL_STATUS;
		// }
		// }
		// System.out.println("Called save");
		// return Status.OK_STATUS;
		// }
		// };
		//
		// job.setUser(true);
		// job.schedule();

	}

	@CanExecute
	public boolean canExecute(@Optional @Named(Const.CONTEXT_SELECTED_DB) IDb db) {
		return db != null;
	}

}
