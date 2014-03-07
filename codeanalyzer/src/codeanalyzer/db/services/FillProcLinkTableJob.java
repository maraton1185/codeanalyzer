package codeanalyzer.db.services;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDb.DbState;
import codeanalyzer.core.interfaces.ILoaderManager;
import codeanalyzer.utils.Const;

public class FillProcLinkTableJob extends Job {

	public static final String MY_FAMILY = "all";

	public static class rule implements ISchedulingRule {
		@Override
		public boolean contains(ISchedulingRule rule) {
			return rule == this;
		}

		@Override
		public boolean isConflicting(ISchedulingRule rule) {

			if (rule.getClass().equals(FillProcLinkTableJob.rule.class))
				return true;

			return rule == this;
		}
	};

	ILoaderManager loaderManager = pico.get(ILoaderManager.class);

	// private String family;

	private IDb db;

	private Shell shell;

	public FillProcLinkTableJob(IDb db, Shell shell) {
		super(db.getName());
		// this.family = family;
		this.db = db;
		this.shell = shell;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		try {

			loaderManager.fillProcLinkTable(db, monitor);
			AppManager.br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);

			return Status.OK_STATUS;

		} catch (final InvocationTargetException e) {

			db.setLinkState(DbState.notLoaded);

			AppManager.sync.syncExec(new Runnable() {
				@Override
				public void run() {
					MessageDialog.openError(shell,
							"Ошибка выполнения операции", e.getMessage());
				}
			});

			return Status.CANCEL_STATUS;
		}

	}

	@Override
	public boolean belongsTo(Object family) {
		return family == MY_FAMILY;
	}

	// @Override
	// public boolean belongsTo(Object family) {
	//
	// return true;// this.family.equals(family);
	// }
}
