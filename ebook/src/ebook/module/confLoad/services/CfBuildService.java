package ebook.module.confLoad.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ebook.module.confLoad.model.ELevel;

public class CfBuildService {

	public Integer get(Connection con, ELevel level, String title,
			Integer parent, List<String> proposals) throws SQLException {

		if (proposals != null)
			proposals.clear();

		Integer index = null;

		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select TITLE, ID from OBJECTS WHERE LEVEL=? AND "
				+ (parent == null ? "PARENT IS NULL" : "PARENT = ?");
		// + " AND UPPER(TITLE) REGEXP UPPER(?) ORDER BY ID";
		if (proposals == null)
			SQL = SQL.concat(" AND UPPER(TITLE)=UPPER(?)");
		else
			SQL = SQL.concat(" AND UPPER(TITLE) REGEXP UPPER(?)");
		SQL = SQL.concat(" ORDER BY ID");

		prep = con.prepareStatement(SQL);

		prep.setInt(1, level.toInt());
		if (parent == null)
			prep.setString(2, title);
		else {
			prep.setInt(2, parent);
			prep.setString(3, title);
		}

		rs = prep.executeQuery();
		int count = 0;
		try {
			while (rs.next()) {
				if (proposals != null)
					proposals.add(rs.getString(1));
				index = rs.getInt(2);
				count++;
			}

		} finally {
			rs.close();
		}

		return count > 1 ? null : index;

	}
}
