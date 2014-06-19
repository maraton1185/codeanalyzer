package ebook.module.conf.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ebook.core.interfaces.ITextParser.procEntity;

public class CfGetService {

	public int getProcCount(Connection con) throws SQLException {

		String SQL = "Select COUNT(ID) from PROCS";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(SQL);

		try {
			if (rs.next())
				return rs.getInt(1);
			else
				throw new SQLException();
		} finally {
			rs.close();
		}
	}

	public List<procEntity> getProcs(Connection con) throws SQLException {

		List<procEntity> list = new ArrayList<procEntity>();

		String SQL = "SELECT T.ID, T.NAME, T.GROUP1, T.GROUP2 FROM PROCS AS T";

		PreparedStatement prep = con.prepareStatement(SQL);
		ResultSet rs = prep.executeQuery();

		try {
			while (rs.next()) {

				procEntity item = new procEntity(false);
				item.id = rs.getInt(1);
				item.proc_name = rs.getString(2);
				item.group1 = rs.getString(3);
				item.group2 = rs.getString(4);

				list.add(item);
			}
		} finally {
			rs.close();
		}
		return list;
	}

	public String getProcText(Connection con, int id) throws SQLException,
			IOException {

		StringBuilder result = new StringBuilder();

		String SQL = "Select TEXT from PROCS_TEXT WHERE ID=?";
		PreparedStatement prep = con.prepareStatement(SQL);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		BufferedReader bufferedReader = null;

		try {
			if (rs.next()) {

				Reader in = rs.getCharacterStream(1);
				bufferedReader = new BufferedReader(in);
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					result.append(line + "\n");
				}
			}
		} finally {
			rs.close();
		}
		return result.toString();
	}
}
