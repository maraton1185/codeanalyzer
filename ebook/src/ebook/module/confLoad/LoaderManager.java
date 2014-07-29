package ebook.module.confLoad;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import org.eclipse.core.runtime.IProgressMonitor;

import ebook.auth.interfaces.IAuthorize;
import ebook.core.pico;
import ebook.core.exceptions.LoadConfigException;
import ebook.module.conf.ConfConnection;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confLoad.interfaces.ILoaderManager;
import ebook.module.confLoad.model.DbState;
import ebook.module.confLoad.services.LoaderService;
import ebook.utils.Const;
import ebook.utils.Utils;

public class LoaderManager implements ILoaderManager {

	IAuthorize sign = pico.get(IAuthorize.class);
	// CfStructure cfStructure = new CfStructure();
	LoaderService loaderService = new LoaderService();

	@Override
	public void loadFromDirectory(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {

		// œ–Œ¬≈– » ******************************************************

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

		// «¿√–”« ¿ ******************************************************

		Connection con = null;
		try {

			ConfConnection _con = new ConfConnection(db.getDbFullPath());

			// con = _con.getConnection();
			con = _con.makeConnection(true);

			monitor.beginTask("«‡„ÛÁÍ‡ ÍÓÌÙË„Û‡ˆËË...", length);

			loaderService.srv.load().clearTables(con);

			loadFromDirectoryDoWork(con, monitor, files, db.getDoLog());

			_con.setExternalConnection(con);
			_con.srv().setState(DbState.Loaded);
			_con.resetExternalConnection();
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

		// if (_con != null && done)
		// _con.srv().setState(DbState.Loaded);
	}

	@Override
	public void loadFromDb(ListConfInfo db) throws InvocationTargetException {

		// œ–Œ¬≈– » ******************************************************

		File folder = db.getDbFullPath().toFile();
		if (!folder.exists())
			throw new InvocationTargetException(new LoadConfigException(),
					Const.ERROR_CONFIG_PATH);

		// «¿√–”« ¿ ******************************************************
		Connection con = null;
		try {

			// monitor.beginTask(Const.MSG_CONFIG_CHECK, 0);
			ConfConnection _con = new ConfConnection(db.getDbFullPath());
			con = _con.makeConnection(true);
			// con = db.getConnection(true);
			//
			// cfStructure.checkSructure(db);

			if (loaderService.srv.load().linkTableFilled(con)) {
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
			// } catch (DbStructureException e) {
			// throw new InvocationTargetException(e, e.getMessage());
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
	public void update(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException {

		// œ–Œ¬≈– » ******************************************************

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

		// «¿√–”« ¿ ******************************************************

		Connection con = null;
		try {

			ConfConnection _con = new ConfConnection(db.getDbFullPath());

			con = _con.makeConnection(true);

			monitor.beginTask("Œ·ÌÓ‚ÎÂÌËÂ ÍÓÌÙË„Û‡ˆËË...", length);

			loaderService.srv.load().clearLinkTable(con);
			loadFromDirectoryDoWork(con, monitor, files, db.getDoLog());

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
	public void loadFromSQL(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillProcLinkTable(ListConfInfo db, IProgressMonitor monitor)
			throws InvocationTargetException {

		// œ–Œ¬≈– » ******************************************************

		if (db.getState() != DbState.Loaded)
			throw new InvocationTargetException(new Exception(),
					Const.ERROR_CONFIG_LOADED);

		// if (db.getLinkState() == DbState.Loaded)
		// throw new InvocationTargetException(new LinksExistsException(),
		// Const.ERROR_LINK_LOADED);

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

			ConfConnection _con = new ConfConnection(db.getDbFullPath());

			con = _con.makeConnection(true);

			monitor.beginTask("”‰‡ÎÂÌËÂ ÒÚ‡˚ı ‰‡ÌÌ˚ı...", 0);
			loaderService.srv.load().clearLinkTable(con);

			loaderService.fillProcLinkTableDoWork(con, monitor);

			// if (!sign.check())
			// if (!cfStructure.checkLisence(db))
			// throw new InvocationTargetException(
			// new InterruptedException(),
			// Const.ERROR_PRO_ACCESS_LOAD);

			db.setLinkState(DbState.Loaded);
			_con.setExternalConnection(con);
			_con.srv().setLinkState(DbState.Loaded);
			_con.resetExternalConnection();

		} catch (InterruptedException e) {

			try {
				loaderService.srv.load().clearLinkTable(con);

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

		// if (_con != null && done)
		// _con.srv().setLinkState(DbState.Loaded);
	}

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

}
