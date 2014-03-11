package codeanalyzer.db.services;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.ITextParser;
import codeanalyzer.core.interfaces.ITextParser.Entity;
import codeanalyzer.core.interfaces.ITextParser.ProcCall;
import codeanalyzer.core.interfaces.ITextParser.procEntity;
import codeanalyzer.utils.AesCrypt;

public class DbLoadService {

	ITextParser parser = pico.get(ITextParser.class);

	// ADD *****************************************************

	public Integer addObject(Connection con, Entity line) throws SQLException {

		Integer index = 0;

		String SQL = "Select ID from OBJECTS WHERE GROUP1=? AND GROUP2=?";
		PreparedStatement prep = con.prepareStatement(SQL);
		prep.setString(1, line.group1);
		prep.setString(2, line.group2);
		ResultSet rs = prep.executeQuery();
		try {
			if (rs.next())
				return rs.getInt(1);
		} finally {
			rs.close();
		}

		SQL = "INSERT INTO OBJECTS (GROUP1, GROUP2, MODULE, TYPE) VALUES (?,?,?,?)";
		prep = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

		prep.setString(1, line.group1);
		prep.setString(2, line.group2);
		prep.setString(3, line.module);
		prep.setInt(4, line.type.getInt());

		ResultSet generatedKeys = null;
		try {
			int affectedRows = prep.executeUpdate();
			if (affectedRows == 0)
				throw new SQLException();

			generatedKeys = prep.getGeneratedKeys();
			if (generatedKeys.next())
				index = generatedKeys.getInt(1);
			else
				throw new SQLException();
		} finally {
			generatedKeys.close();
		}
		return index;
	}

	public void addProcedure(Connection con, procEntity line, Integer object)
			throws SQLException {

		String SQL = "INSERT INTO PROCS (OBJECT, GROUP1, GROUP2, MODULE, NAME, TITLE, EXPORT, CONTEXT, SECTION) VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement prep = con.prepareStatement(SQL,
				Statement.RETURN_GENERATED_KEYS);

		prep.setInt(1, object);
		prep.setString(2, line.group1);
		prep.setString(3, line.group2);
		prep.setString(4, line.module);
		prep.setString(5, line.proc_name.toUpperCase());
		prep.setString(6, line.proc_title);
		prep.setBoolean(7, line.export);
		prep.setInt(8, line.context.getInt());
		if (line.section.length() != 0)
			prep.setString(9, line.section.substring(0,
					line.section.length() > 199 ? 199 : line.section.length()));
		else
			prep.setString(9, line.section);

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

	public void addProcCalls(Connection con, procEntity line, Integer index)
			throws SQLException {

		if (line.calls != null)

			for (ProcCall call : line.calls) {
				String SQL = "INSERT INTO LINKS (PROC, CONTEXT, NAME) VALUES (?,?,?)";
				PreparedStatement prep = con.prepareStatement(SQL);

				prep.setInt(1, index);
				prep.setString(2, call.context);
				prep.setString(3, call.name);

				int affectedRows = prep.executeUpdate();
				if (affectedRows == 0)
					throw new SQLException();
			}
	}

	private void addProcInfo(Connection con, procEntity line, Integer index)
			throws SQLException {

		String SQL = "INSERT INTO PROCS_TEXT (PROC, TEXT, HASH) VALUES (?,?,?)";
		PreparedStatement prep = con.prepareStatement(SQL);

		prep.setInt(1, index);
		prep.setCharacterStream(2, new BufferedReader(new StringReader(
				line.text.toString())));
		prep.setString(3, AesCrypt.getHash(line.text.toString().getBytes()));

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

	public int deleteProcs(Connection con, Integer object) throws SQLException {

		String SQL = "DELETE FROM PROCS WHERE OBJECT=?";
		PreparedStatement prep = con.prepareStatement(SQL);

		prep.setInt(1, object);

		return prep.executeUpdate();

	}

	// LINK *************************************************************

	public boolean linkTableFilled(Connection con) throws SQLException {
		String SQL = "Select COUNT(ID) from LINKS";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(SQL);

		try {
			if (rs.next())
				return rs.getInt(1) != 0;
			else
				throw new SQLException();
		} finally {
			rs.close();
		}
	}

	public void clearLinkTable(Connection con) throws SQLException {
		String SQL = "DELETE FROM LINKS";
		PreparedStatement prep = con.prepareStatement(SQL);
		prep.executeUpdate();

	}

}
