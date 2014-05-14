package codeanalyzer.core.db;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import codeanalyzer.core.exceptions.DbStructureException;
import codeanalyzer.core.interfaces.IDbStructure;
import codeanalyzer.utils.DbStructureChecker;

public class DbStructure implements IDbStructure {

	public void createStructure(Connection con) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Statement stat = con.createStatement();

		// create table
		try {

			stat.execute("DROP TABLE IF EXISTS BOOKS;");

			stat.execute("CREATE TABLE BOOKS (ID INTEGER AUTO_INCREMENT, "
					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, OPTIONS VARCHAR(500), "
					+ "TITLE VARCHAR(500), PATH VARCHAR(500), "
					+ "FOREIGN KEY(PARENT) REFERENCES BOOKS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			// con.close();
		}

	}

	public void checkSructure(Connection con) throws FileNotFoundException,
			DbStructureException, SQLException {

		// Connection con = null;
		boolean haveStructure;
		try {
			// con = db.getConnection(true);

			DatabaseMetaData metadata = con.getMetaData();

			DbStructureChecker ch = new DbStructureChecker();
			haveStructure = ch.checkColumns(metadata, "BOOKS",
					"PARENT, SORT, TITLE, GROUP, PATH, OPTIONS")

			;

		} catch (Exception e) {
			throw new DbStructureException();
		} finally {
			// con.close();
		}

		if (!haveStructure)
			throw new DbStructureException();

	}
}
