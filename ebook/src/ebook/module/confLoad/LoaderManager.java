package ebook.module.confLoad;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import ebook.auth.interfaces.IAuthorize;
import ebook.core.pico;
import ebook.core.exceptions.DbCantLoadException;
import ebook.core.exceptions.LoadConfigException;
import ebook.module.conf.ConfConnection;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confLoad.interfaces.ILoaderManager;
import ebook.module.confLoad.model.DbState;
import ebook.module.confLoad.services.LoaderService;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Const;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class LoaderManager implements ILoaderManager {

	IAuthorize sign = pico.get(IAuthorize.class);
	// CfStructure cfStructure = new CfStructure();
	LoaderService loaderService = new LoaderService();

	@Override
	public void loadFromDirectory(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {

		// �������� ******************************************************

		File folder = db.getLoadPath().toFile();
		if (!folder.exists())
			throw new InvocationTargetException(new LoadConfigException(),
					Const.ERROR_CONFIG_PATH);

		File[] files;
		files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String extension = Utils.getExtension(pathname);
				return extension.equalsIgnoreCase("txt");
				// || extension.equalsIgnoreCase("meta");
			}
		});
		if (files.length == 0) {
			files = folder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					String extension = Utils.getExtension(pathname);
					return extension.equalsIgnoreCase("xml");
				}
			});
		}
		int length = files.length;
		if (length == 0)
			throw new InvocationTargetException(new LoadConfigException(),
					Const.ERROR_CONFIG_EMPTY);

		// if (!sign.check()) {
		// if (files.length > Const.DEFAULT_FREE_FILES_COUNT) {
		// throw new InvocationTargetException(new LoadConfigException(),
		// Const.ERROR_PRO_ACCESS_LOAD);
		// }
		// }

		// �������� ******************************************************

		Connection con = null;
		try {

			ConfConnection _con = new ConfConnection(db.getAbsolutePath());

			// con = _con.getConnection();
			con = _con.getConnection();// makeConnection(true);

			if (!loaderService.srv.load().canLoad(con))
				throw new DbCantLoadException();

			monitor.beginTask("������� ������...", 0);

			loaderService.srv.load().clearTables(con);

			monitor.beginTask("�������� ������������...", length);

			loadFromDirectoryDoWork(con, monitor, files, db.getDoLog());

			_con.setExternalConnection(con);
			_con.srv(null).setState(DbState.Loaded);
			_con.resetExternalConnection();
			// if (!sign.check())
			// if (!dbStructure.checkLisence(db))
			// throw new InvocationTargetException(
			// new InterruptedException(),
			// Const.ERROR_PRO_ACCESS_LOAD);

			db.setState(DbState.Loaded);

			if (db.getDeleteSourceFiles()) {
				monitor.beginTask("�������� ������...", length);
				monitor.subTask("");
				for (File f : files) {

					if (monitor.isCanceled()) {
						throw new InterruptedException();
					}

					f.delete();

					monitor.worked(1);
				}
			}

		} catch (InterruptedException e) {
			throw new InvocationTargetException(new InterruptedException(),
					Const.ERROR_CONFIG_INTERRUPT);
		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		} finally {
			// try {
			// con.close();
			// } catch (Exception e) {
			// throw new InvocationTargetException(e,
			// Const.ERROR_CONFIG_CREATE_DATABASE);
			// }
			monitor.done();
		}

		// if (_con != null && done)
		// _con.srv().setState(DbState.Loaded);
	}

	// @Override
	// public void loadFromDb(ListConfInfo db) throws InvocationTargetException
	// {
	//
	// // �������� ******************************************************
	//
	// File folder = db.getAbsolutePath().toFile();
	// if (!folder.exists())
	// throw new InvocationTargetException(new LoadConfigException(),
	// Const.ERROR_CONFIG_PATH);
	//
	// // �������� ******************************************************
	// // Connection con = null;
	// try {
	//
	// // monitor.beginTask(Const.MSG_CONFIG_CHECK, 0);
	// ConfConnection _con = new ConfConnection(db.getAbsolutePath());
	// @SuppressWarnings("unused")
	// Connection con = _con.getConnection(); // makeConnection(true);
	// // con = db.getConnection(true);
	// //
	// // cfStructure.checkSructure(db);
	//
	// // if (loaderService.srv.load().linkTableFilled(con)) {
	// // db.setState(DbState.Loaded);
	// // // db.setLinkState(DbState.Loaded);
	// // } else
	// db.setState(DbState.Loaded);
	//
	// // if (!sign.check())
	// // if (!checkLisence(db))
	// // throw new LiscenseException();
	// //
	// // db.setState(DbState.Loaded);
	//
	// // } catch (LiscenseException e) {
	// // throw new InvocationTargetException(e, e.message);
	// // } catch (InterruptedException e) {
	// // throw new InvocationTargetException(new InterruptedException(),
	// // Const.ERROR_CONFIG_INTERRUPT);
	// // } catch (DbStructureException e) {
	// // throw new InvocationTargetException(e, e.getMessage());
	// } catch (Exception e) {
	// throw new InvocationTargetException(e, e.getMessage());
	// } finally {
	// // try {
	// // con.close();
	// // } catch (Exception e) {
	// // throw new InvocationTargetException(e,
	// // Const.ERROR_CONFIG_OPEN_DATABASE);
	// // }
	// // monitor.done();
	//
	// }
	//
	// }

	@Override
	public void update(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException {

		// �������� ******************************************************
		// boolean free = !pico.get(IAuthorize.class).check();
		// if (free) {
		// throw new InvocationTargetException(new DbLicenseException());
		// }
		File folder = db.getLoadPath().toFile();
		if (!folder.exists())
			throw new InvocationTargetException(new LoadConfigException(),
					Const.ERROR_CONFIG_PATH);

		File[] files;
		files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String extension = Utils.getExtension(pathname);
				return extension.equalsIgnoreCase("txt");
				// || extension.equalsIgnoreCase("meta");
			}
		});
		if (files.length == 0) {
			files = folder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					String extension = Utils.getExtension(pathname);
					return extension.equalsIgnoreCase("xml");
				}
			});
		}
		int length = files.length;
		if (length == 0)
			throw new InvocationTargetException(new LoadConfigException(),
					Const.ERROR_CONFIG_EMPTY);

		// �������� ******************************************************

		Connection con = null;
		try {

			ConfConnection _con = new ConfConnection(db.getAbsolutePath());

			con = _con.getConnection(); // makeConnection(true);

			if (!loaderService.srv.load().canLoad(con))
				throw new DbCantLoadException();

			monitor.beginTask("���������� ������������...", length);

			// loaderService.srv.load().clearLinkTable(con);
			loadFromDirectoryDoWork(con, monitor, files, db.getDoLog());

			// if (!sign.check())
			// if (!dbStructure.checkLisence(db))
			// throw new InvocationTargetException(
			// new InterruptedException(),
			// Const.ERROR_PRO_ACCESS_LOAD);

			db.setState(DbState.Loaded);

			if (db.getDeleteSourceFiles()) {
				monitor.beginTask("�������� ������...", length);
				monitor.subTask("");
				for (File f : files) {

					if (monitor.isCanceled()) {
						throw new InterruptedException();
					}

					f.delete();

					monitor.worked(1);
				}
			}
		} catch (InterruptedException e) {
			throw new InvocationTargetException(new InterruptedException(),
					Const.ERROR_CONFIG_INTERRUPT);
		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		} finally {
			// try {
			// con.close();
			// } catch (Exception e) {
			// throw new InvocationTargetException(e,
			// Const.ERROR_CONFIG_CREATE_DATABASE);
			// }
			monitor.done();
		}

	}

	@Override
	public void loadFromSQL(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException {
		// TODO Auto-generated method stub

	}

	// @Override
	// public void fillProcLinkTable(ListConfInfo db, IProgressMonitor monitor)
	// throws InvocationTargetException {
	//
	// // �������� ******************************************************
	//
	// if (db.getState() != DbState.Loaded)
	// throw new InvocationTargetException(new Exception(),
	// Const.ERROR_CONFIG_LOADED);
	//
	// // if (db.getLinkState() == DbState.Loaded)
	// // throw new InvocationTargetException(new LinksExistsException(),
	// // Const.ERROR_LINK_LOADED);
	//
	// // if (!sign.check()) {
	// // if (files.length > Const.DEFAULT_FREE_FILES_COUNT) {
	// // throw new InvocationTargetException(new LoadConfigException(),
	// // Const.ERROR_PRO_ACCESS_LOAD);
	// // }
	// // }
	//
	// // �������� ******************************************************
	//
	// Connection con = null;
	// try {
	//
	// if (monitor.isCanceled()) {
	// throw new InterruptedException();
	// }
	//
	// ConfConnection _con = new ConfConnection(db.getAbsolutePath());
	//
	// con = _con.getConnection(); // makeConnection(true);
	//
	// if (!loaderService.srv.load().canLoad(con))
	// throw new DbCantLoadException();
	//
	// monitor.beginTask("�������� ������ ������...", 0);
	// loaderService.srv.load().clearLinkTable(con);
	//
	// loaderService.fillProcLinkTableDoWork(con, monitor);
	//
	// // if (!sign.check())
	// // if (!cfStructure.checkLisence(db))
	// // throw new InvocationTargetException(
	// // new InterruptedException(),
	// // Const.ERROR_PRO_ACCESS_LOAD);
	//
	// db.setLinkState(DbState.Loaded);
	// _con.setExternalConnection(con);
	// _con.srv(null).setLinkState(DbState.Loaded);
	// _con.resetExternalConnection();
	//
	// } catch (InterruptedException e) {
	//
	// try {
	// loaderService.srv.load().clearLinkTable(con);
	//
	// } catch (Exception e1) {
	// throw new InvocationTargetException(e,
	// Const.ERROR_CONFIG_OPEN_DATABASE);
	// }
	//
	// throw new InvocationTargetException(new InterruptedException(),
	// Const.ERROR_CONFIG_INTERRUPT);
	//
	// } catch (Exception e) {
	// throw new InvocationTargetException(e, e.getMessage());
	// } finally {
	// // try {
	// //
	// // con.close();
	// // } catch (Exception e) {
	// // throw new InvocationTargetException(e,
	// // Const.ERROR_CONFIG_OPEN_DATABASE);
	// // }
	// monitor.done();
	//
	// }
	//
	// // if (_con != null && done)
	// // _con.srv().setLinkState(DbState.Loaded);
	// }

	// *********************************************************************

	private void loadFromDirectoryDoWork(Connection con,
			IProgressMonitor monitor, File[] files, boolean log)
			throws Exception {

		for (File f : files) {

			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}

			monitor.subTask(f.getName());

			if (!f.isDirectory()) {
				String extension = Utils.getExtension(f);

				if (extension.equalsIgnoreCase("txt")) {
					loaderService.loadTxtModuleFile(con, f, log);
				}

				if (extension.equalsIgnoreCase("xml")) {
					loaderService.loadXmlModuleFile(con, f, log);
				}
			}

			monitor.worked(1);
		}
	}

	// *******************************************************************

	HashMap<operationType, String> operationNames = new HashMap<operationType, String>();

	public LoaderManager() {

		operationNames.put(operationType.fromDirectory,
				Strings.value("operationType.fromDirectory"));
		operationNames.put(operationType.update,
				Strings.value("operationType.update"));
		// operationNames.put(operationType.fromDb,
		// Strings.get("operationType.fromDb"));
		// operationNames.put(operationType.fromSQL,
		// Strings.get("operationType.fromSQL"));
		// operationNames.put(operationType.fillProcLinkTable,
		// Strings.value("operationType.fillProcLinkTable"));

	}

	@Override
	public String getOperationName(operationType key) {
		String name = operationNames.get(key);
		return name;
	}

	@Override
	public void execute(ITreeItemInfo conf, final Shell shell) {

		final ListConfInfo db = (ListConfInfo) conf;
		// switch (db.getType()) {
		// // case fillProcLinkTable:
		// // sheduleFillProcLinkTableJob(db);
		// // return;
		// case fromDb:
		// BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// loadFromDb(db);
		// } catch (InvocationTargetException e) {
		//
		// db.setState(DbState.notLoaded);
		//
		// MessageDialog.openError(shell,
		// "������ ���������� ��������", e.getMessage());
		//
		// } catch (Exception e) {
		//
		// db.setState(DbState.notLoaded);
		// }
		// }
		// });
		// return;
		// default:
		// break;
		// }

		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {

				switch (db.getType()) {
				case fromDirectory:
					loadFromDirectory(db, monitor);
					// sheduleFillProcLinkTableJob(db);
					break;
				case update:
					update(db, monitor);
					// sheduleFillProcLinkTableJob(db);
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
			pShell.setText(Strings.title("ProgressMonitorTitle"));
			pShell.update();
			pmd.run(true, true, runnable);
			pShell.dispose();

		} catch (InvocationTargetException e) {

			db.setState(DbState.notLoaded);

			String msg = e.getMessage();
			String message = msg != null && !msg.isEmpty() ? msg : e
					.getTargetException().getMessage();
			MessageDialog.openError(shell, "������ ���������� ��������",
					message);

		} catch (Exception e) {

			db.setState(DbState.notLoaded);
		}

	}

	// private void sheduleFillProcLinkTableJob(ListConfInfo db) {
	//
	// // setting the progress monitor
	// IJobManager manager = Job.getJobManager();
	//
	// // ToolItem has the ID "statusbar" in the model
	// MToolControl element = (MToolControl) App.model.find(
	// Strings.model("model.id.statustool"), App.app);
	//
	// Object widget = element.getObject();
	// ((ProgressControl) widget).setDb(db);
	// final IProgressMonitor p = (IProgressMonitor) widget;
	// ProgressProvider provider = new ProgressProvider() {
	//
	// @Override
	// public IProgressMonitor createMonitor(Job job) {
	// return p;
	// }
	// };
	//
	// manager.setProgressProvider(provider);
	//
	// FillProcLinkTableJob job = new FillProcLinkTableJob(db);
	// job.setRule(new FillProcLinkTableJob.rule());
	// job.schedule();
	// }

}
