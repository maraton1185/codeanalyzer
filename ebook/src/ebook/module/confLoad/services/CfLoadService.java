package ebook.module.confLoad.services;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ebook.auth.crypt._AesCrypt;
import ebook.module.confLoad.model.ELevel;
import ebook.module.confLoad.model.Entity;
import ebook.module.confLoad.model.procEntity;

public class CfLoadService {

	// _ITextParser parser = pico.get(_ITextParser.class);

	// ADD *****************************************************

	public Integer addEntity(Connection con, Entity line) throws SQLException {

		Integer group1 = addObject(con, line.group1, ELevel.group1.toInt(),
				null, line.sort);
		line._group1 = group1;
		Integer group2 = addObject(con, line.group2, ELevel.group2.toInt(),
				group1, line.sort);
		line._group2 = group2;
		Integer module = addObject(con, line.module, ELevel.module.toInt(),
				group2, line.sort);
		line._module = module;
		return module;
	}

	public Integer addObject(Connection con, String title, Integer level,
			Integer parent, Integer sort) throws SQLException {

		Integer result = null;
		String SQL;
		PreparedStatement prep;
		ResultSet rs;

		SQL = "Select ID from OBJECTS WHERE LEVEL=? AND "
				+ (parent == null ? "PARENT IS NULL" : "PARENT = ?")
				+ " AND TITLE=?";
		prep = con.prepareStatement(SQL);

		prep.setInt(1, level);
		if (parent == null)
			prep.setString(2, title);
		else {
			prep.setInt(2, parent);
			prep.setString(3, title);
		}
		rs = prep.executeQuery();
		try {
			if (rs.next())
				result = rs.getInt(1);
		} finally {
			rs.close();
		}

		if (result == null) {
			SQL = "INSERT INTO OBJECTS (LEVEL, PARENT, TITLE, SORT) VALUES (?,?,?,?)";
			prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			prep.setInt(1, level);
			if (parent == null)
				prep.setNull(2, java.sql.Types.INTEGER);
			else
				prep.setInt(2, parent);

			prep.setString(3, title);
			prep.setInt(4, sort);

			ResultSet generatedKeys = null;
			try {
				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();

				generatedKeys = prep.getGeneratedKeys();
				if (generatedKeys.next())
					result = generatedKeys.getInt(1);
				else
					throw new SQLException();
			} finally {
				generatedKeys.close();
			}
		}

		return result;

	}

	public void addProcedure(Connection con, procEntity line, Integer module)
			throws SQLException {

		String SQL = "INSERT INTO PROCS (PARENT, NAME, TITLE, EXPORT, CONTEXT, SECTION, GROUP1, GROUP2, MODULE) VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement prep = con.prepareStatement(SQL,
				Statement.RETURN_GENERATED_KEYS);

		prep.setInt(1, module);
		prep.setString(2, line.proc_name.toUpperCase());
		prep.setString(3, line.proc_title);
		prep.setBoolean(4, line.export);
		prep.setInt(5, line.context.getInt());
		if (line.section.length() != 0)
			prep.setString(6, line.section.substring(0,
					line.section.length() > 199 ? 199 : line.section.length()));
		else
			prep.setString(6, line.section);

		prep.setInt(7, line._group1);
		prep.setInt(8, line._group2);
		prep.setInt(9, line._module);

		int affectedRows = prep.executeUpdate();
		if (affectedRows == 0)
			throw new SQLException();

		Integer index = 0;
		ResultSet generatedKeys = null;
		try {
			generatedKeys = prep.getGeneratedKeys();
			if (generatedKeys.next())
				index = generatedKeys.getInt(1);
			else
				throw new SQLException();
		} finally {
			generatedKeys.close();
		}

		addProcInfo(con, line, index);

	}

	// public void addProcCalls(Connection con, procEntity line, Integer index)
	// throws SQLException {
	//
	// if (line.calls != null)
	//
	// for (procCall call : line.calls) {
	// String SQL = "INSERT INTO LINKS (PROC, CONTEXT, NAME) VALUES (?,?,?)";
	// PreparedStatement prep = con.prepareStatement(SQL);
	//
	// prep.setInt(1, index);
	// prep.setString(2, call.context);
	// prep.setString(3, call.name);
	//
	// int affectedRows = prep.executeUpdate();
	// if (affectedRows == 0)
	// throw new SQLException();
	// }
	// }

	private void addProcInfo(Connection con, procEntity line, Integer index)
			throws SQLException {

		String SQL = "INSERT INTO PROCS_TEXT (PROC, TEXT, HASH) VALUES (?,?,?)";
		PreparedStatement prep = con.prepareStatement(SQL);

		prep.setInt(1, index);
		prep.setCharacterStream(2, new BufferedReader(new StringReader(
				line.text.toString())));
		prep.setString(3, _AesCrypt.getHash(line.text.toString().getBytes()));

		int affectedRows = prep.executeUpdate();
		if (affectedRows == 0)
			throw new SQLException();

		if (line.params != null)

			for (String parameter : line.params) {
				SQL = "INSERT INTO PROCS_PARAMETERS (PROC, KEY) VALUES (?,?)";
				prep = con.prepareStatement(SQL);

				prep.setInt(1, index);
				prep.setString(2, parameter.trim());

				affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();
			}

	}

	// DELETE *****************************************************

	public int deleteProcs(Connection con, Integer module) throws SQLException {

		String SQL = "DELETE FROM PROCS WHERE PARENT=?";
		PreparedStatement prep = con.prepareStatement(SQL);

		prep.setInt(1, module);

		return prep.executeUpdate();

	}

	// LINK *************************************************************

	// public boolean linkTableFilled(Connection con) throws SQLException {
	// String SQL = "Select COUNT(ID) from LINKS";
	// Statement stat = con.createStatement();
	// ResultSet rs = stat.executeQuery(SQL);
	//
	// try {
	// if (rs.next())
	// return rs.getInt(1) != 0;
	// else
	// throw new SQLException();
	// } finally {
	// rs.close();
	// }
	// }

	// public void clearLinkTable(Connection con) throws SQLException {
	// String SQL = "DELETE FROM LINKS";
	// PreparedStatement prep = con.prepareStatement(SQL);
	// prep.executeUpdate();
	//
	// }

	public void clearTables(Connection con) throws SQLException {
		String SQL;
		PreparedStatement prep;

		SQL = "DELETE FROM OBJECTS";
		prep = con.prepareStatement(SQL);
		prep.executeUpdate();

	}

	public boolean canLoad(Connection con) throws SQLException {

		boolean result = false;

		String SQL = "SELECT TOP 1 T.CANLOAD FROM INFO AS T";
		PreparedStatement prep = con.prepareStatement(SQL);

		ResultSet rs = prep.executeQuery();
		try {
			if (rs.next()) {

				result = rs.getBoolean(1);
			}
		} finally {
			rs.close();
		}

		return result;

	}

}
