package codeanalyzer.module.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import codeanalyzer.core.exceptions.DbStructureException;
import codeanalyzer.core.interfaces.IDbStructure;
import codeanalyzer.utils.DbStructureChecker;
import codeanalyzer.utils.Strings;

public class DbStructure implements IDbStructure {

	@Override
	public void createStructure(Connection con) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Statement stat = con.createStatement();
		String SQL;
		PreparedStatement prep;
		int affectedRows;

		// create table
		try {

			stat.execute("DROP TABLE IF EXISTS USERS;");

			stat.execute("CREATE TABLE USERS (ID INTEGER AUTO_INCREMENT, "
					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(500), "
					// + "PASSWORD VARCHAR(500), "

					+ "FOREIGN KEY(PARENT) REFERENCES USERS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			SQL = "INSERT INTO USERS (TITLE, ISGROUP) VALUES (?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.get("initUserTitle"));
			prep.setBoolean(2, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// *****************************

			stat.execute("DROP TABLE IF EXISTS BOOKS;");

			stat.execute("CREATE TABLE BOOKS (ID INTEGER AUTO_INCREMENT, "
					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(500), "
					// + "PATH VARCHAR(500), "
					+ "ROLE INTEGER, "
					+ "FOREIGN KEY(ROLE) REFERENCES USERS(ID), "
					+ "FOREIGN KEY(PARENT) REFERENCES BOOKS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			SQL = "INSERT INTO BOOKS (TITLE, ISGROUP) VALUES (?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.get("initBookTitle"));
			prep.setBoolean(2, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			// con.close();
		}

	}

	@Override
	public void checkSructure(Connection con) throws DbStructureException,
			SQLException {

		// Connection con = null;
		boolean haveStructure;
		try {
			// con = db.getConnection(true);

			DatabaseMetaData metadata = con.getMetaData();

			DbStructureChecker ch = new DbStructureChecker();
			haveStructure = ch.checkColumns(metadata, "BOOKS",
					"PARENT, SORT, TITLE, ISGROUP, OPTIONS, ROLE")
					&& ch.checkColumns(metadata, "USERS",
							"PARENT, SORT, TITLE, ISGROUP, OPTIONS");

		} catch (Exception e) {
			throw new DbStructureException();
		} finally {
			// con.close();
		}

		if (!haveStructure)
			throw new DbStructureException();

	}
}
