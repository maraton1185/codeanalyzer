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

public class DbStructure{
	
	public void createStructure(IDb db) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
			
//			stat.execute("CREATE TABLE CONFIG_INFO (ID INTEGER AUTO_INCREMENT, "
//					+ "VERSION VARCHAR(36));");
			
			stat.execute("CREATE TABLE OBJECTS (ID INTEGER AUTO_INCREMENT, "
					+ "GROUP1 VARCHAR(200), GROUP2 VARCHAR(200),  "
					+ "TITLE1 VARCHAR(200), TITLE2 VARCHAR(200),  "
					+ "TAG VARCHAR(200), "
					+ "PRIMARY KEY (ID));"
					+ "CREATE INDEX IDXTITLE1 ON OBJECTS(TITLE1);"
					+ "CREATE INDEX IDXTITLE2 ON OBJECTS(TITLE2);"
					+ "CREATE INDEX IDXGROUP1 ON OBJECTS(GROUP1);"
					+ "CREATE INDEX IDXGROUP2 ON OBJECTS(GROUP2)");

			stat.execute("CREATE TABLE MODULES (ID INTEGER AUTO_INCREMENT, "
					+ "OBJECT INTEGER, NAME VARCHAR(200), TITLE VARCHAR(200),  "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(OBJECT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE);"
					+ "CREATE INDEX MODULE_NAME ON MODULES(NAME);");

			stat.execute("CREATE TABLE PROCS (ID INTEGER AUTO_INCREMENT, "
					+ "OBJECT INTEGER, MODULE INTEGER, NAME VARCHAR(200), TITLE VARCHAR(500), EXPORT BOOL, "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(OBJECT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "FOREIGN KEY(MODULE) REFERENCES MODULES(ID) ON UPDATE CASCADE ON DELETE CASCADE);"
					+ "CREATE INDEX PROC_NAME ON PROCS(NAME);"
					+ "CREATE INDEX PROC_OBJECT ON PROCS(OBJECT);"
					+ "CREATE INDEX PROC_MODULE ON PROCS(MODULE);"
					+ "CREATE INDEX PROC_EXPORT ON PROCS(EXPORT);"
					);

			stat.execute("CREATE TABLE PROCS_TEXT (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, TEXT CLOB, HASH VARCHAR(500), "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");
			
			stat.execute("CREATE TABLE PROCS_PARAMETERS (ID INTEGER AUTO_INCREMENT, "
					+ "PROC INTEGER, KEY VARCHAR(200), VALUE VARCHAR(200),"
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

			stat.execute("CREATE TABLE OBJECT_REFS (ID INTEGER AUTO_INCREMENT, "
					+ "MODULE INTEGER, OBJECT INTEGER, "
					+ "PRIMARY KEY (ID), "
					+ "FOREIGN KEY(OBJECT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
					+ "FOREIGN KEY(MODULE) REFERENCES MODULES(ID) ON UPDATE CASCADE ON DELETE CASCADE);"
					+ "CREATE INDEX REF_OBJECT ON OBJECT_REFS(OBJECT);"
					+ "CREATE INDEX REF_MODULE ON OBJECT_REFS(MODULE);");
			
//			stat.execute("CREATE TABLE PROCS_LINK (ID INTEGER AUTO_INCREMENT, "
//					+ "PROC1 INTEGER, NAME1 VARCHAR(200), PROC2 INTEGER, NAME2 VARCHAR(200), "
//					+ "OBJECT1 INTEGER, OBJECT2 INTEGER, MODULE1 INTEGER, MODULE2 INTEGER, LINE INTEGER, "
//					+ "PRIMARY KEY (ID), "
//					+ "FOREIGN KEY(OBJECT1) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
//					+ "FOREIGN KEY(MODULE1) REFERENCES MODULES(ID) ON UPDATE CASCADE ON DELETE CASCADE,"
//					+ "FOREIGN KEY(PROC1) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE,"
//					+ "FOREIGN KEY(OBJECT2) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE, "
//					+ "FOREIGN KEY(MODULE2) REFERENCES MODULES(ID) ON UPDATE CASCADE ON DELETE CASCADE,"
//					+ "FOREIGN KEY(PROC2) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE"
//					+ ")");
//
//			stat.execute("CREATE TABLE CONFIG (ID INTEGER AUTO_INCREMENT, "
//					+ "OBJECT INTEGER, "
//					+ "PROPERTY VARCHAR(200), "
//					+ "VALUE VARCHAR(200), "
//					+ "PRIMARY KEY (ID), "
//					+ "FOREIGN KEY(OBJECT) REFERENCES OBJECTS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");
			
//			stat.execute("CREATE TABLE HISTORY (ID INTEGER AUTO_INCREMENT, "
//					+ "PROC INTEGER, "
//					+ "DATE SMALLDATETIME, "
//					+ "PRIMARY KEY (ID), "
//					+ "FOREIGN KEY(PROC) REFERENCES PROCS(ID) ON UPDATE CASCADE ON DELETE CASCADE)");
			
		} catch (Exception e) {
			throw new SQLException();
		} finally {
			con.close();
		}

	}

	public void checkSructure(IDb db) throws FileNotFoundException, SQLException {

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
					"GROUP1, GROUP2, TAG")
					&& ch.checkColumns(metadata, "MODULES", "OBJECT, NAME")
					&& ch.checkColumns(metadata, "PROCS",
							"OBJECT, MODULE, NAME, TITLE, EXPORT")
					&& ch.checkColumns(metadata, "PROCS_TEXT", "TEXT, HASH")
					&& ch.checkColumns(metadata, "OBJECT_REFS", "MODULE, OBJECT")
//					&& ch.checkColumns(metadata, "PROCS_LINK",
//							"PROC1, PROC2, OBJECT1, OBJECT2, MODULE1, MODULE2")
//					&& ch.checkColumns(metadata, "CONFIG",
//							"OBJECT, PROPERTY, VALUE")
//					&& ch.checkColumns(metadata, "CONFIG_INFO",
//							"VERSION")
					&& ch.checkColumns(metadata, "PROCS_PARAMETERS",
							"KEY, VALUE");
			
			
		} catch (Exception e) {
			throw new SQLException();
		} finally {
			con.close();
		}

		if (!haveStructure)
			throw new SQLException();

	}

	public boolean checkLisence(IDb db) throws FileNotFoundException, SQLException {
		IPath path = db.getDbPath();
		if (!path.isValidPath(path.toString()))
			throw new FileNotFoundException();

		Connection con = null;		
		try {
			con = db.getConnection(true);
			ResultSet rs;
			Statement stat = con.createStatement();
			rs = stat.executeQuery("Select COUNT(ID) from MODULES");
			try {
				int count = 0;
				if (rs.next())
					count = rs.getInt(1);

				return count<Const.DEFAULT_FREE_FILES_COUNT;
				
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
