package ebook.module.conf;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ebook.core.exceptions.DbStructureException;
import ebook.core.interfaces.IDbStructure;
import ebook.core.models.DbOptions;
import ebook.utils.Const;
import ebook.utils.DbStructureChecker;
import ebook.utils.Strings;

public class ConfStructure implements IDbStructure {

	@Override
	public void createStructure(Connection con) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Statement stat = con.createStatement();

		// create table
		try {

			stat.execute("CREATE TABLE INFO (ID INTEGER AUTO_INCREMENT, "
					+ "OPTIONS VARCHAR(1500), " + "PRIMARY KEY (ID));");

			stat.execute("CREATE TABLE LISTS (ID INTEGER AUTO_INCREMENT, "

					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(1500), "

					+ "FOREIGN KEY(PARENT) REFERENCES LISTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			stat.execute("CREATE TABLE CONTEXT (ID INTEGER AUTO_INCREMENT, "

					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(1500), "
					+ "LIST INTEGER, "

					+ "FOREIGN KEY(LIST) REFERENCES LISTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "FOREIGN KEY(PARENT) REFERENCES CONTEXT(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			// *****************************
			String SQL;
			PreparedStatement prep;
			int affectedRows;

			SQL = "INSERT INTO INFO (OPTIONS) VALUES (?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			ConfOptions opt = new ConfOptions();
			prep.setString(1, DbOptions.save(opt));
			// prep.setBoolean(3, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			SQL = "INSERT INTO LISTS (TITLE, ISGROUP) VALUES (?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.get("initConfListTitle"));
			prep.setBoolean(2, false);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			SQL = "INSERT INTO CONTEXT (TITLE, ISGROUP, LIST) VALUES (?,?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.get("initConfContextTitle"));
			prep.setBoolean(2, true);
			// prep.setInt(3, 1);
			prep.setNull(3, java.sql.Types.INTEGER);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// *****************************

			stat.execute("CREATE TABLE OBJECTS (ID INTEGER AUTO_INCREMENT, "

					+ "TITLE VARCHAR(500), "
					+ "LEVEL INTEGER, "
					+ "PARENT INTEGER, "

					+ "OPTIONS VARCHAR(1500), "
					// + "CONTEXT INTEGER, "
					// + "TYPE INTEGER, "

					+ "FOREIGN KEY(PARENT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));"

					+ "CREATE INDEX IDX_LEVEL ON OBJECTS(LEVEL);"
					+ "CREATE INDEX IDX_TITLE ON OBJECTS(TITLE);");

			stat.execute("CREATE TABLE PROCS (ID INTEGER AUTO_INCREMENT, "
					+ "OBJECT INTEGER, "
					+ "NAME VARCHAR(200), TITLE VARCHAR(500), EXPORT BOOL, CONTEXT INTEGER, SECTION VARCHAR(200), "

					+ "FOREIGN KEY(OBJECT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "

					+ "PRIMARY KEY (ID));"
					+ "CREATE INDEX IDX_NAME ON PROCS(NAME);");

			stat.execute("CREATE TABLE PROCS_TEXT (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, TEXT CLOB, HASH VARCHAR(500), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE PROCS_PARAMETERS (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, KEY VARCHAR(200), VALUE VARCHAR(200),"
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE LINKS (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, CONTEXT VARCHAR(200), NAME VARCHAR(200), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			// *****************************

			// stat.execute("CREATE TABLE OBJECTS (ID INTEGER AUTO_INCREMENT, "
			// +
			// "GROUP1 VARCHAR(200), GROUP2 VARCHAR(200), MODULE VARCHAR(200), "
			// + "TYPE INTEGER, " + "TAG VARCHAR(200), PRIMARY KEY (ID));"
			// + "CREATE INDEX IDX_GROUP1 ON OBJECTS(GROUP1);"
			// + "CREATE INDEX IDX_GROUP2 ON OBJECTS(GROUP2);");
			//
			// stat.execute("CREATE TABLE OBJECT_TABLE (ID INTEGER AUTO_INCREMENT, "
			// + "OBJECT INTEGER, MODULE VARCHAR(200), KEY VARCHAR(200), "
			// + "TYPE INTEGER, "
			// +
			// "FOREIGN KEY(OBJECT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
			// + "PRIMARY KEY (ID));");

			// stat.execute("CREATE TABLE PROCS_SECTIONS (ID INTEGER AUTO_INCREMENT, "
			// + "SECTION CLOB, PRIMARY KEY (ID)");

			// stat.execute("CREATE TABLE PROCS_TEXT (ID INTEGER AUTO_INCREMENT, "
			// + "PROC INTEGER, TEXT CLOB, HASH VARCHAR(500), "
			// + "PRIMARY KEY (ID), "
			// +
			// "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");
			//
			// stat.execute("CREATE TABLE LINKS (ID INTEGER AUTO_INCREMENT, "
			// + "PROC INTEGER, CONTEXT VARCHAR(200), NAME VARCHAR(200), "
			// + "PRIMARY KEY (ID), "
			// +
			// "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");
			//

			// *****************************
			// String SQL;
			// PreparedStatement prep;
			// int affectedRows;
			//
			// SQL = "INSERT INTO INFO (OPTIONS) VALUES (?);";
			// prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);
			//
			// // prep.setString(1, "");// db.getName());
			// BookOptions opt = new BookOptions();
			// prep.setString(1, DbOptions.save(opt));
			// // prep.setBoolean(3, true);
			// affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

		} catch (Exception e) {
			throw new SQLException();
		}
		// } finally {
		// con.close();
		// }

	}

	@Override
	public void checkSructure(Connection con) throws DbStructureException,
			SQLException {

		boolean haveStructure;

		DatabaseMetaData metadata = con.getMetaData();

		DbStructureChecker ch = new DbStructureChecker();

		// haveStructure = ch.checkColumns(metadata, "OBJECTS",
		// "GROUP1, GROUP2, MODULE, TYPE, TAG")
		// && ch.checkColumns(metadata, "PROCS",
		// "OBJECT, GROUP1, GROUP2, MODULE, NAME, TITLE, EXPORT, CONTEXT, SECTION")
		// && ch.checkColumns(metadata, "PROCS_TEXT", "PROC, TEXT, HASH")
		// // && ch.checkColumns(metadata, "LINKS",
		// // "PROC1, NAME1, PROC2, NAME2")
		// && ch.checkColumns(metadata, "PROCS_PARAMETERS", "KEY, VALUE")
		// && ch.checkColumns(metadata, "OBJECT_TABLE",
		// "OBJECT, MODULE, KEY, TYPE")

		haveStructure = ch.checkColumns(metadata, "OBJECTS",
				"PARENT, TITLE, OPTIONS")

				&& ch.checkColumns(metadata, "PROCS",
						"OBJECT, NAME, TITLE, EXPORT, CONTEXT, SECTION")

				&& ch.checkColumns(metadata, "PROCS_PARAMETERS", "KEY, VALUE")

				&& ch.checkColumns(metadata, "PROCS_TEXT", "PROC, TEXT, HASH")
				&& ch.checkColumns(metadata, "LINKS", "PROC, CONTEXT, NAME")

				&& ch.checkColumns(metadata, "INFO", "OPTIONS")
				&& ch.checkColumns(metadata, "CONTEXT",
						"PARENT, SORT, TITLE, ISGROUP, OPTIONS, LIST")
				&& ch.checkColumns(metadata, "LISTS",
						"PARENT, SORT, TITLE, ISGROUP, OPTIONS");

		if (!haveStructure)
			throw new DbStructureException();

	}

	public boolean checkLisence(Connection con) throws FileNotFoundException,
			SQLException {
		try {
			ResultSet rs;
			Statement stat = con.createStatement();
			rs = stat.executeQuery("Select COUNT(ID) from OBJECTS");
			try {
				int count = 0;
				if (rs.next())
					count = rs.getInt(1);

				return count < Const.DEFAULT_FREE_FILES_COUNT;

			} finally {
				rs.close();
			}
		} catch (Exception e) {
			throw new SQLException();
		} finally {
			con.close();
		}
	}
}
