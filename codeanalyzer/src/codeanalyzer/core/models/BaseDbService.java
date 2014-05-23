package codeanalyzer.core.models;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.core.runtime.IPath;

import codeanalyzer.core.interfaces.IDbService;
import codeanalyzer.core.interfaces.IDbStructure;

public abstract class BaseDbService implements IDbService {

	protected IDbStructure dbStructure;
	protected Connection con;

	protected BaseDbService(IDbStructure dbStructure) {
		this.dbStructure = dbStructure;
	}

	protected abstract IPath getConnectionPath();

	protected Connection connect(boolean exist, boolean editMode, IPath path)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		Class.forName("org.h2.Driver").newInstance();
		String ifExist = exist ? ";IFEXISTS=TRUE" : "";

		String mode = !editMode ? ";FILE_LOCK=SERIALIZED" : "";

		return DriverManager.getConnection("jdbc:h2:" + path.toString()
				+ ifExist + mode, "sa", "");
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
				con = makeConnection(true);
				dbStructure.checkSructure(con);

			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}

	}

	@Override
	public void openConnection() throws InvocationTargetException {

		try {
			if (con != null)
				throw new IllegalAccessException();

			con = connect(true, true, getConnectionPath());
		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		}
	}

	@Override
	public Connection makeConnection(boolean exist)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		return connect(exist, false, getConnectionPath());

	}

	@Override
	public Connection getConnection() throws IllegalAccessException {

		if (con == null)
			throw new IllegalAccessException();
		else
			return con;

	}

	public void closeConnection() {
		if (con == null)
			return;
		try {

			con.close();
			con = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() {
		closeConnection();
	}
}
