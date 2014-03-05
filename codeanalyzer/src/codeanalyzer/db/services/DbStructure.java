package codeanalyzer.db.services;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import codeanalyzer.core.interfaces.IDb;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Utils;

public class DbStructure {

	public void createStructure(IDb db) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		IPath path = db.getDbPath();
		if (!path.isValidPath(path.toString()))
			throw new IllegalAccessException();

		File f = path.toFile();

		File folder = f.getParentFile();
		File[] files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {

				String extension = Utils.getExtension(pathname);
				return extension.equalsIgnoreCase("db");
			}
		});
		for (File _f : files) {
			_f.delete();
		}

		Connection con = db.getConnection(false);

		Statement stat = con.createStatement();

		// create table
		try {

			// stat.execute("CREATE TABLE CONFIG_INFO (ID INTEGER AUTO_INCREMENT, "
			// + "VERSION VARCHAR(36));");

			stat.execute("CREATE TABLE OBJECTS (ID INTEGER AUTO_INCREMENT, "
					+ "GROUP1 VARCHAR(200), GROUP2 VARCHAR(200), MODULE VARCHAR(200), "
					+ "TYPE INTEGER, " + "TAG VARCHAR(200), PRIMARY KEY (ID));"
					+ "CREATE INDEX IDX_GROUP1 ON OBJECTS(GROUP1);"
					+ "CREATE INDEX IDX_GROUP2 ON OBJECTS(GROUP2);");

			stat.execute("CREATE TABLE OBJECT_TABLE (ID INTEGER AUTO_INCREMENT, "
					+ "OBJECT INTEGER, MODULE VARCHAR(200), KEY VARCHAR(200), "
					+ "TYPE INTEGER, "
					+ "FOREIGN KEY(OBJECT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "PRIMARY KEY (ID));");

			stat.execute("CREATE TABLE PROCS (ID INTEGER AUTO_INCREMENT, "
					+ "OBJECT INTEGER, GROUP1 VARCHAR(200), GROUP2 VARCHAR(200), MODULE VARCHAR(200), "
					+ "NAME VARCHAR(200), TITLE VARCHAR(500), EXPORT BOOL, CONTEXT INTEGER, SECTION VARCHAR(200), "
					+ "FOREIGN KEY(OBJECT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					// +
					// "FOREIGN KEY(SECTION) REFERENCES PROCS_SECTIONS(ID) ON UPDATE CASCADE, "
					+ "PRIMARY KEY (ID));"
					+ "CREATE INDEX IDX_NAME ON PROCS(NAME);");

			// stat.execute("CREATE TABLE PROCS_SECTIONS (ID INTEGER AUTO_INCREMENT, "
			// + "SECTION CLOB, PRIMARY KEY (ID)");

			stat.execute("CREATE TABLE PROCS_TEXT (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, TEXT CLOB, HASH VARCHAR(500), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE LINKS (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, CONTEXT VARCHAR(200), NAME VARCHAR(200), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE PROCS_PARAMETERS (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, KEY VARCHAR(200), VALUE VARCHAR(200),"
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			con.close();
		}

	}

	public void checkSructure(IDb db) throws FileNotFoundException,
			SQLException {

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

		IPath path = db.getDbPath();

		if (!path.isValidPath(path.toString()))
			throw new FileNotFoundException();

		Connection con = null;
		boolean haveStructure;
		try {
			con = db.getConnection(true);

			DatabaseMetaData metadata = con.getMetaData();

			checker ch = new checker();
			haveStructure = ch.checkColumns(metadata, "OBJECTS",
					"GROUP1, GROUP2, MODULE, TYPE, TAG")
					&& ch.checkColumns(metadata, "PROCS",
							"OBJECT, GROUP1, GROUP2, MODULE, NAME, TITLE, EXPORT, CONTEXT, SECTION")
					&& ch.checkColumns(metadata, "PROCS_TEXT",
							"PROC, TEXT, HASH")
					// && ch.checkColumns(metadata, "LINKS",
					// "PROC1, NAME1, PROC2, NAME2")
					&& ch.checkColumns(metadata, "PROCS_PARAMETERS",
							"KEY, VALUE")
					&& ch.checkColumns(metadata, "OBJECT_TABLE",
							"OBJECT, MODULE, KEY, TYPE");

		} catch (Exception e) {
			throw new SQLException();
		} finally {
			con.close();
		}

		if (!haveStructure)
			throw new SQLException();

	}

	public boolean checkLisence(IDb db) throws FileNotFoundException,
			SQLException {
		IPath path = db.getDbPath();
		if (!path.isValidPath(path.toString()))
			throw new FileNotFoundException();

		Connection con = null;
		try {
			con = db.getConnection(true);
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
