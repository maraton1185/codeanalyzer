package ebook.module.db;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;

import ebook.auth.interfaces.IAuthorize;
import ebook.core.App;
import ebook.core.pico;
import ebook.core.exceptions.DbLicenseException;
import ebook.core.exceptions.DbStructureException;
import ebook.core.exceptions.MakeConnectionException;
import ebook.core.interfaces.IDbConnection;
import ebook.core.interfaces.IDbStructure;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.Const;

public abstract class BaseDbConnection implements IDbConnection {

	protected boolean license = false;

	protected Connection externalConnection;

	public void setExternalConnection(Connection con) {
		externalConnection = con;
	}

	public void resetExternalConnection() {
		externalConnection = null;
	}

	protected IDbStructure dbStructure;

	// protected Connection con;

	protected BaseDbConnection(IDbStructure dbStructure) {
		this.dbStructure = dbStructure;
	}

	protected abstract IPath getConnectionPath();

	protected Connection connect(boolean exist, boolean editMode, IPath path)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, DbLicenseException {

		Class.forName("org.h2.Driver").newInstance();
		String ifExist = exist ? ";IFEXISTS=TRUE" : "";

		String mode = ";AUTO_SERVER=TRUE";// !editMode ? ";FILE_LOCK=SERIALIZED"
											// : "";

		if (license) {

			Properties prop = new Properties();
			prop.setProperty("user", "sa");
			prop.put("password", Const.FREE_DB_PASSWORD);

			String hash = ";PASSWORD_HASH=TRUE";
			boolean free = !pico.get(IAuthorize.class).check();
			if (free) {
				try {
					return DriverManager.getConnection(
							"jdbc:h2:" + path.toString() + ifExist + mode
									+ hash, prop);
				} catch (Exception e) {
					throw new DbLicenseException();
				}
			} else {

				try {
					return DriverManager.getConnection(
							"jdbc:h2:" + path.toString() + ifExist + mode,
							"sa", "");
				} catch (Exception e) {
					return DriverManager.getConnection(
							"jdbc:h2:" + path.toString() + ifExist + mode
									+ hash, prop);
				}

			}

		} else {
			return DriverManager.getConnection("jdbc:h2:" + path.toString()
					+ ifExist + mode, "sa", "");

		}

		// return DriverManager.getConnection("jdbc:h2:" + path.toString()
		// + ifExist + mode, "sa", "");
	}

	// *******************************************************************

	@Override
	public void create() throws InvocationTargetException {
		try {
			Connection con = null;
			try {
				con = makeConnection(false);
				dbStructure.createStructure(con);

			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}

	}

	@Override
	public void check() throws InvocationTargetException {
		try {
			Connection con = null;
			try {
				con = getConnection();// makeConnection(true);
			} catch (DbLicenseException e) {
				throw new DbLicenseException();
			} catch (Exception e) {
				// if (con != null)
				// con.close();
				throw new MakeConnectionException();
			}

			try {
				dbStructure.checkSructure(con);
			} catch (DbStructureException e) {

				dbStructure.updateSructure(con);
				dbStructure.checkSructure(con);

			}
		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		}

	}

	// @Override
	// public void openConnection() throws InvocationTargetException {
	//
	// HashMap<String, Connection> pull = App.getJetty().pull();
	// Connection pull_con = pull.get(getConnectionPath());
	//
	// try {
	// if (con != null)
	// throw new IllegalAccessException();
	//
	// con = connect(true, true, getConnectionPath());
	// } catch (Exception e) {
	// throw new InvocationTargetException(e, e.getMessage());
	// }
	// }

	@Override
	public Connection makeConnection(boolean exist)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, DbLicenseException {

		return connect(exist, false, getConnectionPath());

	}

	@Override
	public Connection getConnection() throws IllegalAccessException,
			DbLicenseException {

		if (externalConnection != null)
			return externalConnection;

		IPath path = getConnectionPath();
		HashMap<IPath, Connection> pull = App.getJetty().pull();
		Connection pull_con = pull.get(path);

		if (pull_con == null) {

			Connection con;
			try {
				con = connect(true, true, path);
			} catch (DbLicenseException e) {
				throw new DbLicenseException();
			} catch (Exception e) {
				throw new IllegalAccessException();
			}
			pull.put(path, con);
			return con;
		} else
			return pull_con;

	}

	public void closeConnection() {
		IPath path = getConnectionPath();
		HashMap<IPath, Connection> pull = App.getJetty().pull();
		Connection pull_con = pull.get(path);

		if (pull_con == null)
			return;
		try {

			pull_con.close();
			pull.remove(path);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public ITreeItemInfo getTreeItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWindowTitle() {
		return "";
	}

	// @Override
	// protected void finalize() {
	// closeConnection();
	// }
}
