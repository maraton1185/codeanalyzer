package ebook.utils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbStructureChecker {

	public boolean checkColumns(DatabaseMetaData metadata, String table,
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
