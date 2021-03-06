package ebook.module.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import ebook.core.exceptions.DbStructureException;
import ebook.core.interfaces.IDbStructure;
import ebook.utils.DbStructureChecker;
import ebook.utils.Strings;

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
					+ "ROOT BOOLEAN, "
					// + "PASSWORD VARCHAR(500), "

					+ "FOREIGN KEY(PARENT) REFERENCES USERS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			SQL = "INSERT INTO USERS (TITLE, ISGROUP, ROOT) VALUES (?,?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("userRoot"));
			prep.setBoolean(2, true);
			prep.setBoolean(3, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// *****************************

			stat.execute("DROP TABLE IF EXISTS BOOKS;");

			stat.execute("CREATE TABLE BOOKS (ID INTEGER AUTO_INCREMENT, "
					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "OPTIONS VARCHAR(500), "
					+ "PATH VARCHAR(1500), "
					+ "ROOT BOOLEAN, "
					+ "IMAGE BINARY, "
					// + "FOREIGN KEY(ROLE) REFERENCES USERS(ID), "
					+ "FOREIGN KEY(PARENT) REFERENCES BOOKS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			SQL = "INSERT INTO BOOKS (TITLE, ISGROUP, PATH, ROOT) VALUES (?,?,?,?);";
			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			prep.setString(1, Strings.value("bookListRoot"));
			prep.setBoolean(2, true);
			prep.setString(3, "");
			prep.setBoolean(4, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// ResultSet generatedKeys = prep.getGeneratedKeys();
			// // int id;
			// if (generatedKeys.next()) {
			// int id = generatedKeys.getInt(1);
			// } else
			// throw new SQLException();

			// SQL =
			// "INSERT INTO BOOKS (TITLE, ISGROUP, PATH, PARENT, IMAGE) VALUES (?,?,?,?,?);";
			// prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);
			//
			// prep.setString(1, Strings.value("aboutBookTitle"));
			// prep.setBoolean(2, false);
			// prep.setString(3, Utils.getAboutBookPath());
			// prep.setInt(4, id);
			//
			// ByteArrayOutputStream os = new ByteArrayOutputStream();
			//
			// ImageLoader loader = new ImageLoader();
			// loader.data = new ImageData[] { Utils.getImage("_help.png")
			// .getImageData() };
			// loader.save(os, SWT.IMAGE_PNG);
			// ByteArrayInputStream is = new
			// ByteArrayInputStream(os.toByteArray());
			// prep.setBinaryStream(5, is);
			//
			// affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

			// *****************************

			stat.execute("DROP TABLE IF EXISTS CONFS;");

			stat.execute("CREATE TABLE CONFS (ID INTEGER AUTO_INCREMENT, "
					+ "PARENT INTEGER, SORT INTEGER, ISGROUP BOOLEAN, "
					+ "TITLE VARCHAR(500), "
					+ "PATH VARCHAR(1500), "
					+ "ROOT BOOLEAN, "
					+ "OPTIONS VARCHAR(3000), "
					+ "FOREIGN KEY(PARENT) REFERENCES CONFS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			SQL = "INSERT INTO CONFS (TITLE, ISGROUP, ROOT) VALUES (?,?,?);";
			prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);

			prep.setString(1, Strings.value("confListRoot"));
			prep.setBoolean(2, true);
			prep.setBoolean(3, true);
			affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			// *****************************
			// Access Control List

			stat.execute("DROP TABLE IF EXISTS ACL;");

			stat.execute("CREATE TABLE ACL (ID INTEGER AUTO_INCREMENT, "
					+ "BOOK INTEGER, "
					+ "SECTION INTEGER, "
					+ "ROLE INTEGER, "
					+ "FOREIGN KEY(BOOK) REFERENCES BOOKS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "FOREIGN KEY(ROLE) REFERENCES USERS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			// SQL = "INSERT INTO CONFS (TITLE, ISGROUP) VALUES (?,?);";
			// prep = con.prepareStatement(SQL, Statement.CLOSE_CURRENT_RESULT);
			//
			// prep.setString(1, Strings.get("initConfTitle"));
			// prep.setBoolean(2, true);
			// affectedRows = prep.executeUpdate();
			// if (affectedRows == 0)
			// throw new SQLException();

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
					"PARENT, SORT, TITLE, ISGROUP, OPTIONS, PATH, IMAGE, ROOT")
					&& ch.checkColumns(metadata, "USERS",
							"PARENT, SORT, TITLE, ISGROUP, OPTIONS, ROOT")
					&& ch.checkColumns(metadata, "CONFS",
							"PARENT, SORT, TITLE, ISGROUP, OPTIONS, PATH, ROOT")
					&& ch.checkColumns(metadata, "ACL", "BOOK, SECTION, ROLE");

		} catch (Exception e) {
			throw new DbStructureException();
		} finally {
			// con.close();
		}

		if (!haveStructure)
			throw new DbStructureException();

	}

	@Override
	public void updateSructure(Connection con) throws SQLException {
		Statement stat = con.createStatement();
		try {

			stat.execute("ALTER TABLE BOOKS ADD ROOT BOOLEAN;");
			stat.execute("ALTER TABLE USERS ADD ROOT BOOLEAN;");
			stat.execute("ALTER TABLE CONFS ADD ROOT BOOLEAN;");

			stat.execute("UPDATE BOOKS SET ROOT=TRUE WHERE ID=1;");
			stat.execute("UPDATE USERS SET ROOT=TRUE WHERE ID=1;");
			stat.execute("UPDATE CONFS SET ROOT=TRUE WHERE ID=1;");

		} catch (Exception e) {
			throw new SQLException();
		}

	}

}
