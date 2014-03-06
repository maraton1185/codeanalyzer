package codeanalyzer.db;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.E4Services;
import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDb.DbState;
import codeanalyzer.core.interfaces.IDbManager;
import codeanalyzer.core.interfaces.ILoaderManager;
import codeanalyzer.core.interfaces.ILoaderManager.operationType;
import codeanalyzer.db.services.FillProcLinkTableJob;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;

public class DbManager implements IDbManager {

	ILoaderManager loaderManager = pico.get(ILoaderManager.class);

	List<IDb> dbs = new ArrayList<IDb>();
	IDb active;
	IDb nonActive;

	HashMap<operationType, String> operationNames = new HashMap<operationType, String>();

	@Override
	public void init() {

		operationNames.put(operationType.fromDirectory,
				Strings.get("operationType.fromDirectory"));
		operationNames.put(operationType.update,
				Strings.get("operationType.update"));
		operationNames.put(operationType.fromDb,
				Strings.get("operationType.fromDb"));
		operationNames.put(operationType.fromSQL,
				Strings.get("operationType.fromSQL"));
		operationNames.put(operationType.fillProcLinkTable,
				Strings.get("operationType.fillProcLinkTable"));

		String activeKey = PreferenceSupplier
				.get(PreferenceSupplier.BASE_ACTIVE);
		String compareKey = PreferenceSupplier
				.get(PreferenceSupplier.BASE_COMPARE);

		List<String> keys = PreferenceSupplier.getBaseList();

		for (String key : keys) {
			IDb db = pico.get(IDb.class);
			db.load(key);
			dbs.add(db);

			if (activeKey.equalsIgnoreCase(key))
				setActive(db);

			if (compareKey.equalsIgnoreCase(key))
				setNonActive(db);

			executeInit(db);
		}

		Collections.sort(dbs, new Comparator<IDb>() {
			@Override
			public int compare(IDb db1, IDb db2) {

				return db1.getName().compareTo(db2.getName());
			}
		});

		if (dbs.size() != 0) {
			if (active == null)
				setActive(dbs.get(0));

			if (nonActive == null)
				setNonActive(dbs.get(0));
		}

	}

	@Override
	public List<IDb> getList() {
		return dbs;
	}

	@Override
	public void add(IDb db) {
		dbs.add(db);

	}

	@Override
	public void remove(IDb db) {
		dbs.remove(db);
	}

	@Override
	public IDb getActive() {
		return active;
	}

	@Override
	public IDb getNonActive() {
		return nonActive;
	}

	@Override
	public void setActive(IDb db) {
		active = db;
	}

	@Override
	public void setNonActive(IDb db) {
		nonActive = db;
	}

	@Override
	public String getOperationName(operationType key) {
		String name = operationNames.get(key);
		return name == null ? "DBManager.getOperationName" : name;
	}

	@Override
	public void execute(final IDb db, Shell shell) {

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {

				switch (db.getType()) {
				case fromDirectory:
					// loaderManager.loadFromDirectory(db, monitor);
					sheduleFillProcLinkTableJob(db);
					break;
				case fromDb:
					// loaderService.loadFromDb(db, monitor);
					break;
				case fillProcLinkTable:

					loaderManager.fillProcLinkTable(db, monitor);
					break;
				case update:
					// loaderService.update(db, monitor);
					break;
				case fromSQL:
					// loaderService.loadFromSQL(db, monitor);
					break;
				default:
					throw new InterruptedException();
				}
			}
		};

		try {
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(shell);
			pmd.open();
			Shell pShell = pmd.getShell();
			pShell.setText(Strings.get("ProgressMonitorTitle"));
			pShell.update();
			pmd.run(true, true, runnable);
			pShell.dispose();

		} catch (InvocationTargetException e) {

			MessageDialog.openError(shell, "Ошибка загрузки конфигурации",
					e.getMessage());

			db.setState(DbState.notLoaded);

		} catch (Exception e) {

			db.setState(DbState.notLoaded);
		}
	}

	@Override
	public void executeInit(IDb db) {
		if (db.getType() == operationType.fromDb) {
			// loaderService.loadFromDb(db, monitor);
		}

	}

	private void sheduleFillProcLinkTableJob(IDb db) {

		// setting the progress monitor
		IJobManager manager = Job.getJobManager();

		// ToolItem has the ID "statusbar" in the model
		MToolControl element = (MToolControl) E4Services.model.find(
				Strings.get("model.id.statustool"), E4Services.app);

		Object widget = element.getObject();
		final IProgressMonitor p = (IProgressMonitor) widget;
		ProgressProvider provider = new ProgressProvider() {
			@Override
			public IProgressMonitor createMonitor(Job job) {
				return p;
			}
		};

		manager.setProgressProvider(provider);

		FillProcLinkTableJob job = new FillProcLinkTableJob(db);
		job.schedule();
	}
}
