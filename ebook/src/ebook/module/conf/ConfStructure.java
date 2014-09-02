package ebook.module.conf;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ebook.core.exceptions.DbStructureException;
import ebook.core.interfaces.IDbStructure;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.model.ConfOptions;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.db.DbOptions;
import ebook.utils.DbStructureChecker;
import ebook.utils.Strings;

public class ConfStructure implements IDbStructure {

	@Override
	public void updateSructure(Connection con) throws SQLException {

		Statement stat = con.createStatement();
		try {
			// stat.execute("ALTER TABLE OBJECTS ADD SORT INTEGER;");
			// stat.execute("ALTER TABLE PROCS ADD GROUP1 INTEGER;");
			// stat.execute("ALTER TABLE PROCS ADD GROUP2 INTEGER;");
			// stat.execute("ALTER TABLE PROCS ADD SORT INTEGER;");
			// stat.execute("ALTER TABLE PROCS ALTER COLUMN OBJECT RENAME TO PARENT;");

			// stat.execute("CREATE INDEX IDX_GROUP1 ON PROCS(GROUP1);"
			// + "CREATE INDEX IDX_GROUP2 ON PROCS(GROUP2);"
			// + "CREATE INDEX IDX_MODULE ON PROCS(MODULE);");
			stat.execute("DROP TABLE IF EXISTS BOOKMARKS;");
			stat.execute("CREATE TABLE BOOKMARKS (ID INTEGER AUTO_INCREMENT, "

					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(1500), "

					+ "FOREIGN KEY(PARENT) REFERENCES BOOKMARKS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");
		} catch (Exception e) {
			throw new SQLException("Ошибка обновления структуры базы данных.");
		}
	}

	@Override
	public void createStructure(Connection con) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Statement stat = con.createStatement();

		// create table
		try {

			stat.execute("CREATE TABLE INFO (ID INTEGER AUTO_INCREMENT, "
					+ "CANLOAD BOOLEAN, OPTIONS VARCHAR(1500), "
					+ "PRIMARY KEY (ID));");

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

			stat.execute("CREATE TABLE BOOKMARKS (ID INTEGER AUTO_INCREMENT, "

					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(1500), "

					+ "FOREIGN KEY(PARENT) REFERENCES BOOKMARKS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			// *****************************
			String SQL;
			PreparedStatement prep;
			int affectedRows;

			SQL = "INSERT INTO INFO (CANLOAD, OPTIONS) VALUES (?, ?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setBoolean(1, true);
			ConfOptions opt = new ConfOptions();
			prep.setString(2, DbOptions.save(opt));
			// prep.setBoolean(3, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			SQL = "INSERT INTO LISTS (TITLE, ISGROUP) VALUES (?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("confListRoot"));
			prep.setBoolean(2, false);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			SQL = "INSERT INTO CONTEXT (TITLE, ISGROUP, OPTIONS, LIST) VALUES (?,?,?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("contextRoot"));
			prep.setBoolean(2, true);
			ContextInfoOptions opt1 = new ContextInfoOptions();
			opt1.type = BuildType.root;
			prep.setString(3, DbOptions.save(opt1));
			prep.setNull(4, java.sql.Types.INTEGER);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			SQL = "INSERT INTO BOOKMARKS (TITLE, ISGROUP) VALUES (?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("bookmarkRoot"));
			prep.setBoolean(2, false);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// *****************************

			stat.execute("CREATE TABLE OBJECTS (ID INTEGER AUTO_INCREMENT, "

					+ "TITLE VARCHAR(500), "
					+ "LEVEL INTEGER, "
					+ "PARENT INTEGER, "
					+ "SORT INTEGER, "
					+ "OPTIONS VARCHAR(1500), "
					// + "CONTEXT INTEGER, "
					// + "TYPE INTEGER, "

					+ "FOREIGN KEY(PARENT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));"

					+ "CREATE INDEX IDX_LEVEL ON OBJECTS(LEVEL);"
					+ "CREATE INDEX IDX_TITLE ON OBJECTS(TITLE);");

			stat.execute("CREATE TABLE PROCS (ID INTEGER AUTO_INCREMENT, "
					+ "PARENT INTEGER, "
					+ "SORT INTEGER, "
					+ "NAME VARCHAR(200), TITLE VARCHAR(500), EXPORT BOOL, CONTEXT INTEGER, SECTION VARCHAR(200), "

					+ "GROUP1 INTEGER, GROUP2 INTEGER, MODULE INTEGER, "

					+ "FOREIGN KEY(PARENT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "

					+ "PRIMARY KEY (ID));"
					+ "CREATE INDEX IDX_NAME ON PROCS(NAME);"
					+ "CREATE INDEX IDX_GROUP1 ON PROCS(GROUP1);"
					+ "CREATE INDEX IDX_GROUP2 ON PROCS(GROUP2);"
					+ "CREATE INDEX IDX_MODULE ON PROCS(MODULE);");

			stat.execute("CREATE TABLE PROCS_TEXT (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, TEXT CLOB, HASH VARCHAR(500), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE PROCS_PARAMETERS (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, KEY VARCHAR(200), VALUE VARCHAR(200),"
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			// stat.execute("CREATE TABLE LINKS (ID INTEGER AUTO_INCREMENT, "
			// + "PROC INTEGER, CONTEXT VARCHAR(200), NAME VARCHAR(200), "
			// + "PRIMARY KEY (ID), "
			// +
			// "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			// *****************************

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

		haveStructure = ch.checkColumns(metadata, "OBJECTS",
				"PARENT, TITLE, OPTIONS, SORT")

				&& ch.checkColumns(metadata, "PROCS",
						"PARENT, NAME, TITLE, EXPORT, CONTEXT, SECTION, GROUP1, GROUP2, MODULE, SORT")

				&& ch.checkColumns(metadata, "PROCS_PARAMETERS", "KEY, VALUE")

				&& ch.checkColumns(metadata, "PROCS_TEXT", "PROC, TEXT, HASH")
				// && ch.checkColumns(metadata, "LINKS", "PROC, CONTEXT, NAME")

				&& ch.checkColumns(metadata, "INFO", "CANLOAD, OPTIONS")
				&& ch.checkColumns(metadata, "CONTEXT",
						"PARENT, SORT, TITLE, ISGROUP, OPTIONS, LIST")
				&& ch.checkColumns(metadata, "LISTS",
						"PARENT, SORT, TITLE, ISGROUP, OPTIONS")
				&& ch.checkColumns(metadata, "BOOKMARKS",
						"PARENT, SORT, TITLE, ISGROUP, OPTIONS");

		if (!haveStructure)
			throw new DbStructureException();

	}

	// public boolean checkLisence(Connection con) throws FileNotFoundException,
	// SQLException {
	// try {
	// ResultSet rs;
	// Statement stat = con.createStatement();
	// rs = stat.executeQuery("Select COUNT(ID) from OBJECTS");
	// try {
	// int count = 0;
	// if (rs.next())
	// count = rs.getInt(1);
	//
	// return count < Const.DEFAULT_FREE_FILES_COUNT;
	//
	// } finally {
	// rs.close();
	// }
	// } catch (Exception e) {
	// throw new SQLException();
	// } finally {
	// con.close();
	// }
	// }
}
