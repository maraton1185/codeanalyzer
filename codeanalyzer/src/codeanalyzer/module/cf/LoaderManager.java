package codeanalyzer.module.cf;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;

import codeanalyzer.auth.interfaces.IAuthorize;
import codeanalyzer.core.pico;
import codeanalyzer.core.exceptions.DbStructureException;
import codeanalyzer.core.exceptions.LinksExistsException;
import codeanalyzer.core.exceptions.LoadConfigException;
import codeanalyzer.module.cf.interfaces.ICf;
import codeanalyzer.module.cf.interfaces.ILoaderManager;
import codeanalyzer.module.cf.interfaces.ICf.DbState;
import codeanalyzer.module.cf.services.LoaderService;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Utils;

public class LoaderManager implements ILoaderManager {

	IAuthorize sign = pico.get(IAuthorize.class);
	CfStructure cfStructure = new CfStructure();
	LoaderService loaderService = new LoaderService();

	@Override
	public void loadFromDirectory(ICf db, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {

		// œ–Œ¬≈– » ******************************************************

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

		// «¿√–”« ¿ ******************************************************

		Connection con = null;
		try {

			db.initDbPath();

			cfStructure.createStructure(db);
			con = db.getConnection(false);

			monitor.beginTask("«‡„ÛÁÍ‡ ÍÓÌÙË„Û‡ˆËË...", length);

			loadFromDirectoryDoWork(con, monitor, files);

			// if (!sign.check())
			// if (!dbStructure.checkLisence(db))
			// throw new InvocationTargetException(
			// new InterruptedException(),
			// Const.ERROR_PRO_ACCESS_LOAD);

			db.setState(DbState.Loaded);

			if (db.getDeleteSourceFiles()) {
				monitor.beginTask("”‰‡ÎÂÌËÂ Ù‡ÈÎÓ‚...", length);
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
	public void loadFromDb(ICf db) throws InvocationTargetException {

		// œ–Œ¬≈– » ******************************************************

		File folder = db.getDbPath().toFile();
		if (!folder.exists())
			throw new InvocationTargetException(new LoadConfigException(),
					Const.ERROR_CONFIG_PATH);

		// «¿√–”« ¿ ******************************************************
		Connection con = null;
		try {

			// monitor.beginTask(Const.MSG_CONFIG_CHECK, 0);

			con = db.getConnection(true);

			cfStructure.checkSructure(db);

			if (loaderService.linkTableFilled(con)) {
				db.setState(DbState.Loaded);
				db.setLinkState(DbState.Loaded);
			} else
				db.setState(DbState.Loaded);

			// if (!sign.check())
			// if (!checkLisence(db))
			// throw new LiscenseException();
			//
			// db.setState(DbState.Loaded);

			// } catch (LiscenseException e) {
			// throw new InvocationTargetException(e, e.message);
			// } catch (InterruptedException e) {
			// throw new InvocationTargetException(new InterruptedException(),
			// Const.ERROR_CONFIG_INTERRUPT);
		} catch (DbStructureException e) {
			throw new InvocationTargetException(e, e.getMessage());
		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				throw new InvocationTargetException(e,
						Const.ERROR_CONFIG_OPEN_DATABASE);
			}
			// monitor.done();

		}

	}

	@Override
	public void update(ICf db, IProgressMonitor monitor)
			throws InvocationTargetException {

		// œ–Œ¬≈– » ******************************************************

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

		// «¿√–”« ¿ ******************************************************

		Connection con = null;
		try {

			con = db.getConnection(true);

			cfStructure.checkSructure(db);

			loaderService.clearLinkTable(con);
			// db.initDbPath();
			//
			// dbStructure.createStructure(db);
			// con = db.getConnection(false);

			monitor.beginTask("Œ·ÌÓ‚ÎÂÌËÂ ÍÓÌÙË„Û‡ˆËË...", length);

			loadFromDirectoryDoWork(con, monitor, files);

			// if (!sign.check())
			// if (!dbStructure.checkLisence(db))
			// throw new InvocationTargetException(
			// new InterruptedException(),
			// Const.ERROR_PRO_ACCESS_LOAD);

			db.setState(DbState.Loaded);

			if (db.getDeleteSourceFiles()) {
				monitor.beginTask("”‰‡ÎÂÌËÂ Ù‡ÈÎÓ‚...", length);
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
	public void loadFromSQL(ICf db, IProgressMonitor monitor)
			throws InvocationTargetException {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillProcLinkTable(ICf db, IProgressMonitor monitor)
			throws InvocationTargetException {

		// œ–Œ¬≈– » ******************************************************

		if (db.getState() != DbState.Loaded)
			throw new InvocationTargetException(new Exception(),
					Const.ERROR_CONFIG_LOADED);

		if (db.getLinkState() == DbState.Loaded)
			throw new InvocationTargetException(new LinksExistsException(),
					Const.ERROR_LINK_LOADED);

		// if (!sign.check()) {
		// if (files.length > Const.DEFAULT_FREE_FILES_COUNT) {
		// throw new InvocationTargetException(new LoadConfigException(),
		// Const.ERROR_PRO_ACCESS_LOAD);
		// }
		// }

		// «¿√–”« ¿ ******************************************************

		Connection con = null;
		try {

			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}

			monitor.beginTask(Const.MSG_CONFIG_CHECK, 0);

			cfStructure.checkSructure(db);

			con = db.getConnection(true);

			loaderService.fillProcLinkTableDoWork(con, monitor);

			if (!sign.check())
				if (!cfStructure.checkLisence(db))
					throw new InvocationTargetException(
							new InterruptedException(),
							Const.ERROR_PRO_ACCESS_LOAD);

			db.setLinkState(DbState.Loaded);

		} catch (InterruptedException e) {

			try {
				loaderService.clearLinkTable(con);

			} catch (Exception e1) {
				throw new InvocationTargetException(e,
						Const.ERROR_CONFIG_OPEN_DATABASE);
			}

			throw new InvocationTargetException(new InterruptedException(),
					Const.ERROR_CONFIG_INTERRUPT);

		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				throw new InvocationTargetException(e,
						Const.ERROR_CONFIG_OPEN_DATABASE);
			}
			monitor.done();

		}

	}

	// *********************************************************************

	private void loadFromDirectoryDoWork(Connection con,
			IProgressMonitor monitor, File[] files) throws Exception {

		for (File f : files) {

			if (monitor.isCanceled()) {
				throw new InterruptedException();
			}

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

			monitor.worked(1);
		}
	}

}
