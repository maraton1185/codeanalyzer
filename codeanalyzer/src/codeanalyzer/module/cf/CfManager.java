package codeanalyzer.module.cf;

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
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.core.App;
import codeanalyzer.core.pico;
import codeanalyzer.module.cf.interfaces.ICf;
import codeanalyzer.module.cf.interfaces.ICf.DbState;
import codeanalyzer.module.cf.interfaces.ICfManager;
import codeanalyzer.module.cf.interfaces.ILoaderManager;
import codeanalyzer.module.cf.interfaces.ILoaderManager.operationType;
import codeanalyzer.module.cf.services.FillProcLinkTableJob;
import codeanalyzer.temp.ProgressControl;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;

public class CfManager implements ICfManager {

	ILoaderManager loaderManager = pico.get(ILoaderManager.class);

	List<ICf> dbs = new ArrayList<ICf>();
	ICf active;
	ICf nonActive;

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
			ICf db = pico.get(ICf.class);
			db.load(key);
			dbs.add(db);

			if (activeKey.equalsIgnoreCase(key))
				setActive(db);

			if (compareKey.equalsIgnoreCase(key))
				setNonActive(db);

			executeInit(db);
		}

		Collections.sort(dbs, new Comparator<ICf>() {
			@Override
			public int compare(ICf db1, ICf db2) {

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
	public List<ICf> getList() {
		return dbs;
	}

	@Override
	public void add(ICf db) {
		dbs.add(db);

	}

	@Override
	public void remove(ICf db) {
		dbs.remove(db);
	}

	@Override
	public ICf getActive() {
		return active;
	}

	@Override
	public ICf getNonActive() {
		return nonActive;
	}

	@Override
	public void setActive(ICf db) {
		active = db;
	}

	@Override
	public void setNonActive(ICf db) {
		nonActive = db;
	}

	@Override
	public String getOperationName(operationType key) {
		String name = operationNames.get(key);
		return name == null ? "DBManager.getOperationName" : name;
	}

	@Override
	public void execute(final ICf db, final Shell shell) {

		switch (db.getType()) {
		case fillProcLinkTable:
			sheduleFillProcLinkTableJob(db);
			return;
		case fromDb:
			BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
				@Override
				public void run() {
					try {
						loaderManager.loadFromDb(db);
					} catch (InvocationTargetException e) {

						db.setState(DbState.notLoaded);

						MessageDialog.openError(shell,
								"Ошибка выполнения операции", e.getMessage());

					} catch (Exception e) {

						db.setState(DbState.notLoaded);
					}
				}
			});
			return;
		default:
			break;
		}

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {

				switch (db.getType()) {
				case fromDirectory:
					loaderManager.loadFromDirectory(db, monitor);
					sheduleFillProcLinkTableJob(db);
					break;
				case update:
					loaderManager.update(db, monitor);
					sheduleFillProcLinkTableJob(db);
					break;
				// case fromSQL:
				// // loaderService.loadFromSQL(db, monitor);
				// break;
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

			db.setState(DbState.notLoaded);

			MessageDialog.openError(shell, "Ошибка выполнения операции",
					e.getMessage());

		} catch (Exception e) {

			db.setState(DbState.notLoaded);
		}
	}

	@Override
	public void executeInit(final ICf db) {

		if (!PreferenceSupplier.getBoolean(PreferenceSupplier.INIT_EXECUTION))
			return;

		if (db.getType() == operationType.fromDb) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						loaderManager.loadFromDb(db);
						App.br.post(Events.EVENT_UPDATE_CONFIG_LIST,
								null);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}).start();

		}

	}

	private void sheduleFillProcLinkTableJob(ICf db) {

		// setting the progress monitor
		IJobManager manager = Job.getJobManager();

		// ToolItem has the ID "statusbar" in the model
		MToolControl element = (MToolControl) App.model.find(
				Strings.get("model.id.statustool"), App.app);

		Object widget = element.getObject();
		((ProgressControl) widget).setDb(db);
		final IProgressMonitor p = (IProgressMonitor) widget;
		ProgressProvider provider = new ProgressProvider() {
			@Override
			public IProgressMonitor createMonitor(Job job) {
				return p;
			}
		};

		manager.setProgressProvider(provider);

		FillProcLinkTableJob job = new FillProcLinkTableJob(db);
		job.setRule(new FillProcLinkTableJob.rule());
		job.schedule();
	}
}
