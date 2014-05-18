package codeanalyzer.db;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import codeanalyzer.db.interfaces.IDbService;
import codeanalyzer.utils.Const;

public class DbService implements IDbService {

	DbStructure dbStructure = new DbStructure();
	private boolean exist;

	@Override
	public void init(boolean createNew) throws InvocationTargetException {

		try {
			openConnection();
			con = getConnection();
			if (createNew)
				dbStructure.createStructure(con);
			else if (exist)
				dbStructure.checkSructure(con);
			else
				dbStructure.createStructure(con);

		} catch (Exception e) {
			throw new InvocationTargetException(e, e.getMessage());
		}
		// finally {
		// try {
		// con.close();
		// } catch (Exception e) {
		// throw new InvocationTargetException(e,
		// Const.ERROR_CONFIG_OPEN_DATABASE);
		// }
		// }

	}

	// CONNECTION
	// *****************************************************************
	private Connection con;

	private void openConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			URISyntaxException {

		String root = System.getProperty("user.dir");
		File f = new File(root + "\\" + Const.SYSTEM_DB_NAME
				+ Const.DEFAULT_DB_EXTENSION);
		exist = f.exists();

		String ifExist = "";
		// String ifExist = exist ? ";IFEXISTS=TRUE" : "";

		boolean editMode = false;
		String mode = !editMode ? ";FILE_LOCK=SERIALIZED" : "";

		Class.forName("org.h2.Driver").newInstance();

		con = DriverManager.getConnection("jdbc:h2:" + Const.SYSTEM_DB_NAME
				+ ifExist + mode, "sa", "");
	}

	public Connection getConnection() throws IllegalAccessException {

		if (con == null)
			throw new IllegalAccessException();
		else
			return con;

	}

	private void closeConnection() {
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
