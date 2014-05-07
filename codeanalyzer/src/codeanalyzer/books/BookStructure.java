package codeanalyzer.books;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.core.exceptions.DbStructureException;
import codeanalyzer.utils.Strings;

public class BookStructure {

	public void createStructure(Connection con, BookInfo db)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		IPath path = db.getPath();
		if (!path.isValidPath(path.toString()))
			throw new IllegalAccessException();

		// Connection con = db.getConnection(false);

		Statement stat = con.createStatement();

		// create table
		try {

			stat.execute("CREATE TABLE INFO (ID INTEGER AUTO_INCREMENT, "
					+ "DESCRIPTION VARCHAR(500), SELECTED_SECTION INTEGER, EDIT_MODE BOOLEAN, "
					+ "PRIMARY KEY (ID));");

			stat.execute("CREATE TABLE SECTIONS (ID INTEGER AUTO_INCREMENT, "
					+ "PARENT INTEGER, SORT INTEGER, BLOCK BOOLEAN, OPTIONS TITLE VARCHAR(500), "
					+ "TITLE VARCHAR(500), "
					+ "FOREIGN KEY(PARENT) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			stat.execute("CREATE TABLE S_TEXT (ID INTEGER AUTO_INCREMENT, "
					+ "SECTION INTEGER, TEXT CLOB, HASH VARCHAR(500), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(SECTION) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE S_IMAGES (ID INTEGER AUTO_INCREMENT, "
					+ "SECTION INTEGER, DATA BINARY, "
					+ "TITLE VARCHAR(500), SORT INTEGER, EXPANDED BOOLEAN, "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(SECTION) REFERENCES SECTIONS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			// *****************************
			String SQL;
			PreparedStatement prep;
			int affectedRows;

			SQL = "INSERT INTO INFO (DESCRIPTION, SELECTED_SECTION, EDIT_MODE) VALUES (?, ?, ?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, db.getName());
			prep.setInt(2, 1);
			prep.setBoolean(3, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			SQL = "INSERT INTO SECTIONS (TITLE) VALUES (?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.get("initBookSectionTitle"));
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

		} catch (Exception e) {
			throw new SQLException();
		}
		// } finally {
		// con.close();
		// }

	}

	public void checkSructure(Connection con, BookInfo db)
			throws DbStructureException, SQLException, FileNotFoundException {

		final class checker {
			boolean checkColumns(DatabaseMetaData metadata, String table,
					String str_columns) throws SQLException {
				String[] clmns = str_columns.split(",");

				List<String> columns = new ArrayList<String>();
				ResultSet rs = metadata.getColumns(null, null, table, "%");
				while (rs.next())
					columns.add(rs.getString("COLUMN_NAME"));
				rs.close();

				boolean haveColumns = clmns.length != 0;
				for (String clmn : clmns) {
					haveColumns = haveColumns && columns.contains(clmn.trim());
				}

				return haveColumns;
			}
		}

		IPath path = db.getPath();

		if (!path.isValidPath(path.toString()))
			throw new FileNotFoundException();

		// Connection con = null;
		boolean haveStructure;
		// try {
		// con = db.getConnection(true);

		DatabaseMetaData metadata = con.getMetaData();

		checker ch = new checker();
		haveStructure = ch.checkColumns(metadata, "INFO",
				"DESCRIPTION, SELECTED_SECTION, EDIT_MODE")
				&& ch.checkColumns(metadata, "SECTIONS",
						"PARENT, SORT, TITLE, BLOCK, OPTIONS")
				&& ch.checkColumns(metadata, "S_TEXT", "TEXT")
				&& ch.checkColumns(metadata, "S_IMAGES",
						"DATA, TITLE, SORT, EXPANDED")

		;

		if (!haveStructure)
			throw new DbStructureException();

	}
}
