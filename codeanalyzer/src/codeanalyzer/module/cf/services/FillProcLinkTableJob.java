package codeanalyzer.module.cf.services;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

import codeanalyzer.core.App;
import codeanalyzer.core.pico;
import codeanalyzer.core.exceptions.LinksExistsException;
import codeanalyzer.module.cf.interfaces.ICf;
import codeanalyzer.module.cf.interfaces.ICf.DbState;
import codeanalyzer.module.cf.interfaces.ILoaderManager;
import codeanalyzer.utils.Events;

public class FillProcLinkTableJob extends Job {

	public static final String FillProcLinkTableJob_FAMILY = "all";

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

	private ICf db;

	// private Shell shell;

	public FillProcLinkTableJob(ICf db) {
		super(db.getName());
		// this.family = family;
		this.db = db;
		// this.shell = shell;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		try {

			loaderManager.fillProcLinkTable(db, monitor);
			App.br.post(Events.EVENT_UPDATE_CONFIG_LIST, null);

			return Status.OK_STATUS;

		} catch (final InvocationTargetException e) {

			if (!(e.getTargetException() instanceof LinksExistsException))
				db.setLinkState(DbState.notLoaded);

			if (!(e.getTargetException() instanceof InterruptedException))
				App.br.post(Events.EVENT_PROGRESS_ERROR, e.getMessage());

			return Status.CANCEL_STATUS;
		}

	}

	@Override
	public boolean belongsTo(Object family) {
		return family == FillProcLinkTableJob_FAMILY;
	}

	// @Override
	// public boolean belongsTo(Object family) {
	//
	// return true;// this.family.equals(family);
	// }
}
