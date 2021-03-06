package ebook.module.book;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ebook.core.exceptions.DbStructureException;
import ebook.core.interfaces.IDbStructure;
import ebook.module.book.model.BookOptions;
import ebook.module.db.DbOptions;
import ebook.utils.DbStructureChecker;
import ebook.utils.Strings;

public class BookStructure implements IDbStructure {

	@Override
	public void updateSructure(Connection con) throws SQLException {

		Statement stat = con.createStatement();
		try {

			stat.execute("ALTER TABLE SECTIONS ADD ROOT BOOLEAN;");
			stat.execute("UPDATE SECTIONS SET ROOT=TRUE WHERE ID=1;");

			// stat.execute("ALTER TABLE BOOKMARKS ADD PATH VARCHAR(500);");
			// stat.execute("ALTER TABLE BOOKMARKS ADD PROC VARCHAR(500);");
			// stat.execute("ALTER TABLE BOOKMARKS ADD OFFSET INTEGER;");

			// stat.execute("DROP TABLE IF EXISTS PROCS_TEXT;");
			//
			// stat.execute("CREATE TABLE PROCS_TEXT (ID INTEGER AUTO_INCREMENT, "
			// + "PROC INTEGER, TEXT CLOB, HASH VARCHAR(500), "
			// + "PRIMARY KEY (ID), "
			// +
			// "FOREIGN KEY(PROC) REFERENCES CONTEXT(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			// stat.execute("DROP TABLE IF EXISTS BOOKMARKS;");
			//
			// stat.execute("CREATE TABLE BOOKMARKS (ID INTEGER AUTO_INCREMENT, "
			//
			// + "SECTION INTEGER, "
			// + "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
			// + "TITLE VARCHAR(500), "
			// + "OPTIONS VARCHAR(3000), "
			// + "PATH VARCHAR(500), "
			// + "PROC VARCHAR(500), "
			// + "OFFSET INTEGER, "
			//
			// +
			// "FOREIGN KEY(SECTION) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
			// +
			// "FOREIGN KEY(PARENT) REFERENCES BOOKMARKS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
			// + "PRIMARY KEY (ID));");

		} catch (Exception e) {
			throw new SQLException("������ ���������� ��������� ���� ������.");
		}
	}

	@Override
	public void createStructure(Connection con) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {

		Statement stat = con.createStatement();

		// create table
		try {

			// *****************************
			String SQL;
			PreparedStatement prep;
			int affectedRows;

			stat.execute("CREATE TABLE INFO (ID INTEGER AUTO_INCREMENT, "
			// + "DESCRIPTION VARCHAR(500), "
			// + " SELECTED_SECTION INTEGER, EDIT_MODE BOOLEAN, "
					+ "OPTIONS VARCHAR(500), " + "PRIMARY KEY (ID));");

			// SQL = "INSERT INTO CONTEXT (TITLE, ISGROUP) VALUES (?,?);";
			// prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);
			//
			// prep.setString(1, Strings.get("initBookContextTitle"));
			// prep.setBoolean(2, true);
			// affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

			stat.execute("CREATE TABLE SECTIONS (ID INTEGER AUTO_INCREMENT, "

					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(500), "
					+ "ROOT BOOLEAN, "

					+ "FOREIGN KEY(PARENT) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			stat.execute("CREATE TABLE S_TEXT (ID INTEGER AUTO_INCREMENT, "
					+ "SECTION INTEGER, TEXT CLOB, HASH VARCHAR(500), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(SECTION) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE S_IMAGES (ID INTEGER AUTO_INCREMENT, "
					+ "SECTION INTEGER, DATA BINARY, "
					+ "TITLE VARCHAR(500), SORT INTEGER, MIME VARCHAR(5), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(SECTION) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			// *****************************

			SQL = "INSERT INTO INFO (OPTIONS) VALUES (?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			// prep.setString(1, "");// db.getName());
			BookOptions opt = new BookOptions();
			prep.setString(1, DbOptions.save(opt));
			// prep.setBoolean(3, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			SQL = "INSERT INTO SECTIONS (TITLE, ISGROUP, ROOT) VALUES (?,?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("bookRoot"));
			prep.setBoolean(2, true);
			prep.setBoolean(3, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// *****************************

			stat.execute("CREATE TABLE CONTEXT (ID INTEGER AUTO_INCREMENT, "

					+ "SECTION INTEGER, "
					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(1500), "

					+ "FOREIGN KEY(SECTION) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "FOREIGN KEY(PARENT) REFERENCES CONTEXT(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			stat.execute("CREATE TABLE PROCS_TEXT (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, TEXT CLOB, HASH VARCHAR(500), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES CONTEXT(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE BOOKMARKS (ID INTEGER AUTO_INCREMENT, "

					+ "SECTION INTEGER, "
					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(3000), "
					+ "PATH VARCHAR(500), "
					+ "PROC VARCHAR(500), "
					+ "OFFSET INTEGER, "

					+ "FOREIGN KEY(SECTION) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "FOREIGN KEY(PARENT) REFERENCES BOOKMARKS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

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
		haveStructure = ch.checkColumns(metadata, "INFO", "OPTIONS")
				&& ch.checkColumns(metadata, "SECTIONS",
						"PARENT, SORT, TITLE, ISGROUP, OPTIONS, ROOT")
				&& ch.checkColumns(metadata, "CONTEXT",
						"SECTION, PARENT, SORT, TITLE, ISGROUP, OPTIONS")
				&& ch.checkColumns(metadata, "PROCS_TEXT", "PROC, TEXT, HASH")
				&& ch.checkColumns(metadata, "S_TEXT", "TEXT")
				&& ch.checkColumns(metadata, "S_IMAGES",
						"DATA, TITLE, SORT, MIME")
				&& ch.checkColumns(metadata, "BOOKMARKS",
						"SECTION, PARENT, SORT, TITLE, ISGROUP, OPTIONS, PATH, PROC, OFFSET");

		;

		if (!haveStructure)
			throw new DbStructureException();

	}
}
