package codeanalyzer.db;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;

import codeanalyzer.core.pico;
import codeanalyzer.core.exceptions.LoadConfigException;
import codeanalyzer.core.interfaces.IAuthorize;
import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.core.interfaces.IDb.DbState;
import codeanalyzer.core.interfaces.ILoaderManager;
import codeanalyzer.db.services.DbStructure;
import codeanalyzer.db.services.LoaderService;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Utils;

public class LoaderManager implements ILoaderManager {

	IAuthorize sign = pico.get(IAuthorize.class);
	DbStructure dbStructure = new DbStructure();
	LoaderService loaderService = new LoaderService();

	@Override
	public void loadFromDirectory(IDb db, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {

		// ПРОВЕРКИ ******************************************************

		File folder = db.getPath().toFile();
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

		// ЗАГРУЗКА ******************************************************

		Connection con = null;
		try {

			db.initDbPath();

			dbStructure.createStructure(db);
			con = db.getConnection(false);

			monitor.beginTask("Загрузка конфигурации...", length);

			loadFromDirectoryDoWork(con, monitor, files);

			// monitor.beginTask(Const.MSG_CONFIG_FILL_LINK_TABLE,
			// cfg.getProcCount(con));

			// fillProcLinkTableDoWork(monitor, cfg, con);
			if (!sign.check())
				if (!dbStructure.checkLisence(db))
					throw new InvocationTargetException(
							new InterruptedException(),
							Const.ERROR_PRO_ACCESS_LOAD);

			db.setState(DbState.Loaded);

			if (db.getDeleteSourceFiles()) {
				monitor.beginTask("Удаление файлов...", length);
				monitor.subTask("");
				for (File f : files) {
					f.delete();
					if (monitor.isCanceled()) {
						throw new InterruptedException();
					}
					monitor.worked(1);
				}
			}
		} catch (InterruptedException e) {
			throw new InvocationTargetException(new InterruptedException(),
					Const.ERROR_CONFIG_INTERRUPT);
		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				throw new InvocationTargetException(e,
						Const.ERROR_CONFIG_CREATE_DATABASE);
			}
			monitor.done();
		}

	}

	@Override
	public void loadFromDb(IDb db, IProgressMonitor monitor)
			throws InvocationTargetException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(IDb db, IProgressMonitor monitor)
			throws InvocationTargetException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadFromSQL(IDb db, IProgressMonitor monitor)
			throws InvocationTargetException {
		// TODO Auto-generated method stub

	}

	// *********************************************************************

	private void loadFromDirectoryDoWork(Connection con,
			IProgressMonitor monitor, File[] files) throws Exception {

		for (File f : files) {

			monitor.subTask(f.getName());

			if (!f.isDirectory()) {
				String extension = Utils.getExtension(f);

				if (extension.equalsIgnoreCase("txt")) {
					loaderService.loadTxtModuleFile(con, f);
				}

				if (extension.equalsIgnoreCase("xml")) {
					loaderService.loadXmlModuleFile(con, f);
				}
			}
			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}
			monitor.worked(1);
		}
	}

}
