package codeanalyzer.db.services;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import codeanalyzer.core.E4Services;
import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.ILoaderManager;
import codeanalyzer.utils.Const;

public class JobService extends Job {

	ILoaderManager loaderManager = pico.get(ILoaderManager.class);

	// private String family;

	private IDb db;

	public JobService(IDb db) {
		super(db.getName());
		// this.family = family;
		this.db = db;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {

		try {

			switch (db.getType()) {
			case fillProcLinkTable:
				loaderManager.fillProcLinkTable(db, monitor);
				break;
			default:
				break;
			}

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
