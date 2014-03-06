package codeanalyzer.db.services;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

import codeanalyzer.core.E4Services;
import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.ILoaderManager;
import codeanalyzer.utils.Const;

public class FillProcLinkTableJob extends Job {

	public class rule implements ISchedulingRule {
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

	public FillProcLinkTableJob(IDb db) {
		super(db.getName());
		// this.family = family;
		this.db = db;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		try {

			loaderManager.fillProcLinkTable(db, monitor);
			E4Services.br.post(Const.EVENT_UPDATE_CONFIG_LIST, null);

			return Status.OK_STATUS;

		} catch (InvocationTargetException e) {

			e.printStackTrace();
			return Status.CANCEL_STATUS;
		}

	}

	@Override
	public boolean belongsTo(Object family) {
		return true;// this.family.equals(family);
	}
}
