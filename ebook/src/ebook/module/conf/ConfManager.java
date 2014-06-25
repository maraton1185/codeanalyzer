package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

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

import ebook.core.App;
import ebook.core.pico;
import ebook.module.conf.interfaces.ILoaderManager;
import ebook.module.conf.interfaces.ILoaderManager.operationType;
import ebook.module.conf.services.FillProcLinkTableJob;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confList.tree.ListConfInfo.DbState;
import ebook.temp.ProgressControl;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;

public class ConfManager {

	ILoaderManager loaderManager = pico.get(ILoaderManager.class);

	HashMap<operationType, String> operationNames = new HashMap<operationType, String>();

	public ConfManager() {

		operationNames.put(operationType.fromDirectory,
				Strings.get("operationType.fromDirectory"));
		operationNames.put(operationType.update,
				Strings.get("operationType.update"));
		// operationNames.put(operationType.fromDb,
		// Strings.get("operationType.fromDb"));
		// operationNames.put(operationType.fromSQL,
		// Strings.get("operationType.fromSQL"));
		operationNames.put(operationType.fillProcLinkTable,
				Strings.get("operationType.fillProcLinkTable"));

	}

	public String getOperationName(operationType key) {
		String name = operationNames.get(key);
		return name;
		// return name == null ? "DBManager.getOperationName" : name;
	}

	public void execute(final ListConfInfo db, final Shell shell) {

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

	public void executeInit(final ListConfInfo db) {

		if (!PreferenceSupplier.getBoolean(PreferenceSupplier.INIT_EXECUTION))
			return;

		if (db.getType() == operationType.fromDb) {
			new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						loaderManager.loadFromDb(db);
						App.br.post(Events.EVENT_UPDATE_CONFIG_LIST, null);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}).start();

		}

	}

	private void sheduleFillProcLinkTableJob(ListConfInfo db) {

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
